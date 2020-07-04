package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImagePortrait
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_ACTOR
import kotlinx.android.synthetic.main.fragment_actor_search.*
import kotlinx.android.synthetic.main.lista_series_basic_vertical.view.*

class ActorSearchFragment : Fragment() {
    private val personList = mutableListOf<PersonResponse.Person>()
    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this@ActorSearchFragment).get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_actor_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvActores.setBaseAdapter(
            personList, R.layout.lista_series_basic_vertical,
            GridLayoutManager(activity, 3)
        ) {
            itemView.posterBasicTutorial.getImagePortrait(
                requireContext(), it.profilePath.toString()
            )
            itemView.titleBasicVertical.text = it.name
            itemView.setOnClickListener { view ->
                goToFragment(view, it.id)
            }
        }

        searchViewModel.setQuery(getString(R.string.empty))
        searchViewModel.getQuery()?.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                if (R.id.search_image == switcherSearchActor.nextView.id) switcherSearchActor.showNext()
            } else {
                if (R.id.rvActores == switcherSearchActor.nextView.id) switcherSearchActor.showNext()
                searchViewModel.initPersonSearch(it)
            }
        })

        searchViewModel.getPersonList().observe(viewLifecycleOwner, Observer {
            personList.clear()
            it.results?.let { it1 -> personList.addAll(it1) }
            rvActores.adapter?.notifyDataSetChanged()
        })


    }

    private fun goToFragment(
        v: View, id: Int
    ) {
        val bundle = Bundle()
        bundle.putInt(ID_ACTOR, id)
        Navigation.findNavController(v).navigate(R.id.action_search_to_actores, bundle)
    }
}