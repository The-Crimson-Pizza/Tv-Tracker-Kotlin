package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

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
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImagePortrait
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_ACTOR
import kotlinx.android.synthetic.main.fragment_actor_search.*
import kotlinx.android.synthetic.main.lista_series_basic_vertical.view.*

class ActorSearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val personList = mutableListOf<PersonResponse.Person>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_actor_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setQueryListener()
        setPersonListListener()
    }

    private fun setAdapter() {
        rvActores.setBaseAdapter(
            personList, R.layout.lista_series_basic_vertical,
            GridLayoutManager(activity, 3)
        ) {
            itemView.layoutParams.width = (requireView().width * 0.3).toInt()

            itemView.posterBasicVertical.getImagePortrait(
                requireContext(), BASE_URL_IMAGES_POSTER + it.profilePath
            )
            itemView.ratingBasicVertical.visibility = View.GONE
            itemView.titleBasicVertical.text = it.name
            itemView.setOnClickListener { view ->
                goToFragment(view, it.id)
            }
        }
    }

    private fun setQueryListener() {
        searchViewModel.setQuery(getString(R.string.empty))
        searchViewModel.getQuery().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                if (R.id.search_image == switcherSearchActor.nextView.id) switcherSearchActor.showNext()
            } else {
                if (R.id.rvActores == switcherSearchActor.nextView.id) switcherSearchActor.showNext()
                searchViewModel.setPersonList(it)
            }
        })
    }

    private fun setPersonListListener() {
        searchViewModel.getPersonList().observe(viewLifecycleOwner, Observer { personResponse ->
            personList.clear()
            personResponse.results.let { personList.addAll(it) }
            rvActores.adapter?.notifyDataSetChanged()
        })
    }

    private fun goToFragment(v: View, id: Int) {
        val bundle = Bundle()
        bundle.putInt(ID_ACTOR, id)
        Navigation.findNavController(v).navigate(R.id.action_search_to_actores, bundle)
    }
}