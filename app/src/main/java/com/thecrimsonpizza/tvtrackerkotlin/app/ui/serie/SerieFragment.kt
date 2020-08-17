package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.data.local.FirebaseDatabaseRealtime
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.FollowingSerie
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.following.FollowingViewModel
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.webview.WebViewActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_WEB_TV
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TEXT_PLAIN
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.URL_WEB_VIEW
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Status
import kotlinx.android.synthetic.main.fragment_serie.*

class SerieFragment(private val idS: Int, private val posterPath: String? = null) : Fragment() {

    private val seriesViewModel: SeriesViewModel by activityViewModels()
    private val followingViewModel: FollowingViewModel by activityViewModels()
    private var idSerie: Int = 0
    private val followingList = mutableListOf<SerieResponse.Serie>()
    private lateinit var mSerie: SerieResponse.Serie
    private lateinit var itemWeb: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel.init()
        idSerie = idS
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_serie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        ViewCompat.setTransitionName(posterImage, posterImage.transitionName)
// todo- comprobar que el objeto de la base de datos sea igual que el de la API - primero pasarle el followingData y luego comprobar

        progres.visibility = View.VISIBLE
        itemWeb = toolbar.menu.findItem(R.id.action_web)
        setToolbar()
        hideKeyboard()
        setViewPager()
        getSerie()
        getFollowingSeries()
    }

    private fun getFollowingSeries() {
        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { temp ->
            followingList.clear().also { temp.data?.let { list -> followingList.addAll(list) } }
//            followingList.addAll(temp)
            if (this::mSerie.isInitialized) setProgress()
        })
    }

    private fun getSerie() {
        seriesViewModel.getShow().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> progres.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    mSerie = it.data!!
                    if (mSerie.homepage.isNotEmpty()) itemWeb.isVisible = true
                    setProgress()
                }
                Status.ERROR ->
                    Snackbar.make(
                        requireView(),
                        "Error - ${it.message}",
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
            }
        })
    }

    private fun setProgress() {
//        mSerie.checkFav(followingList)
//        seriesViewModel.saveSerie(s)
//        followingList.forEachIndexed { index, follow ->
//            if (mSerie.hasSameId(follow) && mSerie.hasChanged(follow)) {
//                val new = mSerie.updateObject(follow)
//                if (new != null && new != follow) followingList[index] = new; changed = true
//            }
//        }
//        if (changed) followingList.saveToFirebase()
        if (mSerie.followingData == null) {
            val following = mSerie.getSerieFromFollowingList(followingList)
            if (following != null) {
                mSerie.followingData = following.followingData
                seriesViewModel.saveSerie(mSerie)
                // todo - si es distinta actualizar el objeto de la base de datos
                if (mSerie == following) Log.e("EEEEEE", "EEEEEE-IGUAL-EEEEEE")
            }
        }



        SerieAdapter(requireContext(), requireView(), mSerie).fillCollapseBar(posterPath)
        setFloatingButton()
        progres.visibility = View.GONE
    }

    private fun setFloatingButton() {
        fab.visibility = View.VISIBLE
        fab.setOnClickListener { viewFab: View? ->
            if (mSerie.followingData == null) {
                addFav()
                Snackbar.make(viewFab!!, R.string.add_fav, Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(viewFab!!, R.string.already_fav, Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete_fav) { deleteFav() }.show()
            }
        }
    }

    private fun addFav() {
        val new = mSerie
            .also { it.followingData = FollowingSerie() }
            .also { it.followingData?.added = true }
            .also { it.followingData?.episodesData = mSerie.episodesToMap() }

        followingList.add(new)
        FirebaseDatabaseRealtime.saveToFirebase(followingList)

//        seriesViewModel.saveSerie(mSerie)
    }

    private fun deleteFav() {
//        val old = mSerie.getFollowingSerieData(followingList)
        followingList.removeAll { it.id == mSerie.id }
        FirebaseDatabaseRealtime.saveToFirebase(followingList)
        // TODO - ver como actualizar la UI con el seguimiento
        mSerie.followingData = null
//            seriesViewModel.saveSerie(mSerie)
        Toast.makeText(activity, R.string.borrado_correcto, Toast.LENGTH_SHORT).show()
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