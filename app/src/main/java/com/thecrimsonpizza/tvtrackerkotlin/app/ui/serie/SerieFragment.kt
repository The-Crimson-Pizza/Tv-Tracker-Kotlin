package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.webview.WebViewActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.saveToFirebase
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_WEB_TV
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TEXT_PLAIN
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.URL_WEB_VIEW
import kotlinx.android.synthetic.main.fragment_serie.*
import java.util.*

class SerieFragment(private val posterPath: String? = null) : Fragment() {

    private val seriesViewModel: SeriesViewModel by activityViewModels()
    private val followingViewModel: FollowingViewModel by activityViewModels()
    private var idSerie: Int = 0
    private val followingList = mutableListOf<SerieResponse.Serie>()
    private lateinit var mSerie: SerieResponse.Serie
    private lateinit var itemWeb: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        idSerie = intent.getInt(GlobalConstants.ID_SERIE, 0)
//        idSerie = requireArguments().getInt(GlobalConstants.ID_SERIE, 0)
//        seriesViewModel.getShowData(idSerie)
        return inflater.inflate(R.layout.fragment_serie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(posterImage, posterImage.transitionName)

        progres.visibility = View.VISIBLE
        itemWeb = toolbar.menu.findItem(R.id.action_web)
        setToolbar()
        hideKeyboard()
        setViewPager()
        getSerie()
        getFollowingSeries()
    }

    private fun getFollowingSeries() {
        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer {
            followingList.clear()
            followingList.addAll(it)
            if (this::mSerie.isInitialized) setProgress(mSerie)
        })
    }

    private fun getSerie() {
        seriesViewModel.getShow().observe(viewLifecycleOwner, Observer {
            mSerie = it
            if (mSerie.homepage.isNotEmpty()) itemWeb.isVisible = true
            setProgress(mSerie)
        })
    }

    private fun setProgress(serie: SerieResponse.Serie) {
        progres.visibility = View.VISIBLE
        serie.checkFav(followingList)
//        seriesViewModel.saveSerie(s)
        var changed = false
        followingList.forEachIndexed { index, follow ->
            val new = serie.updateObject(follow)
            if (new != null && new != follow) followingList[index] = new; changed = true
        }
        if (changed) followingList.saveToFirebase()

        SerieAdapter(requireContext(), requireView(), serie).fillCollapseBar(posterPath)
        setFloatingButton()
        progres.visibility = View.GONE
    }

    private fun setFloatingButton() {
        fab.visibility = View.VISIBLE
        fab.setOnClickListener { viewFab: View? ->
            if (!mSerie.followingData.added) {
                addFav()
                Snackbar.make(viewFab!!, R.string.add_fav, Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(viewFab!!, R.string.already_fav, Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete_fav) { deleteFav() }.show()
            }
        }
    }

    private fun addFav() {
        mSerie.followingData.added = true
        mSerie.followingData.addedDate = Date()
        followingList.add(mSerie)
        followingList.saveToFirebase()
        seriesViewModel.saveSerie(mSerie)
    }

    private fun deleteFav() {
        val pos = mSerie.getPosition(followingList)
        if (pos != -1) {
            followingList.removeAt(pos)
            followingList.saveToFirebase()
            mSerie.followingData.added = false
            mSerie.followingData.addedDate = null
            seriesViewModel.saveSerie(mSerie)
//            RxBus.getInstance().publish(serie)
            Toast.makeText(activity, R.string.borrado_correcto, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setToolbar() {
        itemWeb.isVisible = false
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        toolbar.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.action_share) {
                startActivity(Intent.createChooser(setIntent(), null))
                return@setOnMenuItemClickListener true
            } else if (item.itemId == R.id.action_web) {
                startActivity(
                    Intent(
                        requireContext(), WebViewActivity::class.java
                    ).putExtra(URL_WEB_VIEW, mSerie.homepage)
                )
            }
            false
        }
    }

    private fun setViewPager() {
        view_pager.offscreenPageLimit = 3
        view_pager.adapter = SerieTabLayoutAdapter(this)
        val tabArray = arrayOf(
            getString(R.string.sinopsis),
            getString(R.string.reparto),
            getString(R.string.temporadas)
        )
        TabLayoutMediator(
            tabs, view_pager, false,
            TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = tabArray[position]
            }
        ).attach()
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun setIntent(): Intent {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TITLE, mSerie.name)
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sharing_url))
        sendIntent.putExtra(Intent.EXTRA_TEXT, BASE_URL_WEB_TV + mSerie.id)
        sendIntent.type = TEXT_PLAIN
        return sendIntent
    }
}