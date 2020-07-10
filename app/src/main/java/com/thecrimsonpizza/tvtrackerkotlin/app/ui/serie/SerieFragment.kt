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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.webview.WebViewActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.saveToFirebase
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_WEB_TV
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TEXT_PLAIN
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.URL_WEBVIEW
import kotlinx.android.synthetic.main.fragment_serie.*
import java.util.*

class SerieFragment : Fragment() {

    private var idSerie: Int = 0
    private val seriesViewModel: SeriesViewModel by activityViewModels()
    private val followingList = mutableListOf<SerieResponse.Serie>()
    private lateinit var serie: SerieResponse.Serie
    val itemWeb: MenuItem = toolbar.menu.findItem(R.id.action_web)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            idSerie = arguments?.getInt(ID_SERIE)!!
        }
        return inflater.inflate(R.layout.fragment_serie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        hideKeyboard()
        setViewPager()
        getFollowingSeries()
    }

    private fun getFollowingSeries() {
        seriesViewModel.getFollowingShows()?.observe(viewLifecycleOwner, Observer {
            followingList.clear()
            followingList.addAll(it)
            setSerie()
        })
    }

    private fun getSerie() {
        seriesViewModel.getShowData(idSerie).observe(viewLifecycleOwner, Observer {
            serie = it
            if (serie.homepage != null && serie.homepage!!.isNotEmpty()) {
                itemWeb.isVisible = true
            }
            setProgress(serie)
        })
    }


    private fun setSerie() {
        if (serie == null) {
            getSerie()
        } else {
            setProgress(serie)
            if (serie.homepage != null && serie.homepage!!.isNotEmpty()) {
                itemWeb.isVisible = true
            }
        }
    }

    private fun setFloatingButton() {
        fab.visibility = View.VISIBLE
        fab.setOnClickListener { viewFab: View? ->
            if (!serie.added) {
                addFav()
                Snackbar.make(viewFab!!, R.string.add_fav, Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(viewFab!!, R.string.already_fav, Snackbar.LENGTH_LONG)
                    .setAction(R.string.delete_fav) { deleteFav() }.show()
            }
        }
    }

    private fun setProgress(s: SerieResponse.Serie) {
        s.checkFav(followingList)
//        RxBus.getInstance().publish(s)
        seriesViewModel.saveSerie(s)
        SerieAdapter(requireContext(), requireView(), s).fillCollapseBar()
        setFloatingButton()
    }

    private fun addFav() {
        serie.added = true
        serie.addedDate = Date()
        followingList.add(serie)
        followingList.saveToFirebase()
        seriesViewModel.saveSerie(serie)
//        RxBus.getInstance().publish(serie)
    }

    private fun deleteFav() {
        val pos = serie.getPosition(followingList)
        if (pos != -1) {
            followingList.removeAt(pos)
            followingList.saveToFirebase()
            serie.added = false
            serie.addedDate = null
            seriesViewModel.saveSerie(serie)
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
                    ).putExtra(URL_WEBVIEW, serie.homepage)
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
        sendIntent.putExtra(Intent.EXTRA_TITLE, serie.name)
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sharing_url))
        sendIntent.putExtra(Intent.EXTRA_TEXT, BASE_URL_WEB_TV + serie.id)
        sendIntent.type = TEXT_PLAIN
        return sendIntent
    }
}