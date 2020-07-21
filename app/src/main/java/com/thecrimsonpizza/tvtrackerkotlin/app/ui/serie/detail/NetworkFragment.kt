package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.BasicResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImage
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_NETWORK
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_NETWORK
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_SERIE
import kotlinx.android.synthetic.main.fragment_network.*
import kotlinx.android.synthetic.main.lista_series_basic_vertical.view.*

class NetworkFragment : Fragment() {

    private val seriesViewModel: SeriesViewModel by activityViewModels()

    private lateinit var mNetwork: SerieResponse.Serie.Network
    private val seriesByNetwork: MutableList<BasicResponse.SerieBasic> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNetwork = requireArguments().getParcelable(ID_NETWORK)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        network_name.text = mNetwork.name
        network_icon.getImage(requireContext(), BASE_URL_IMAGES_NETWORK + mNetwork?.logoPath)
        setAdapter()
        getSeriesByNetwork()
    }

    private fun getSeriesByNetwork() {
        seriesViewModel.getShowsByNetwork(mNetwork.id)
            .observe(viewLifecycleOwner, Observer<BasicResponse> {
                seriesByNetwork.clear()
                seriesByNetwork.addAll(it.basicSeries)
                rvNetworks.adapter?.notifyDataSetChanged()
            })
    }

    private fun setAdapter() {
        rvNetworks.setBaseAdapter(
            seriesByNetwork,
            R.layout.lista_series_basic_vertical,
            GridLayoutManager(activity, 3)
        ) { serie ->
            itemView.posterBasicVertical.getImage(requireContext(), GlobalConstants.BASE_URL_IMAGES_POSTER +serie.posterPath)
            itemView.titleBasicVertical.text = serie.name
            itemView.ratingBasicVertical.text = serie.voteAverage.toString()

            itemView.ratingBasicVertical.visibility = View.GONE
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(ID_SERIE, serie.id)
                Navigation.findNavController(it)
                    .navigate(R.id.action_global_navigation_series, bundle)
            }
        }
    }
}