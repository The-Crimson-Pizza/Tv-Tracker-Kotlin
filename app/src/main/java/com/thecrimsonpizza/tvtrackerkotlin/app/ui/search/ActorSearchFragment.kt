package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImagePortrait
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.goToPersonActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Type
import kotlinx.android.synthetic.main.detail_fragment_search.*
import kotlinx.android.synthetic.main.list_series_basic.view.*

class ActorSearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val personList = mutableListOf<PersonResponse.Person>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setQueryListener()
        setPersonListListener()
    }

    private fun setAdapter() {
        rvSearch.setBaseAdapter(
            personList, R.layout.list_series_basic
        ) { person ->
            itemView.layoutParams.width = (requireView().width * 0.3).toInt()

            itemView.posterBasic.getImagePortrait(
                requireContext(), BASE_URL_IMAGES_POSTER + person.posterPath
            )
            itemView.ratingBasic.visibility = View.GONE
            itemView.titleBasic.text = person.name
            itemView.setOnClickListener {
                person.goToPersonActivity(
                    requireContext(),
                    it,
                    Type.PERSON
                )
            }
        }
    }

    private fun setQueryListener() {
        searchViewModel.setQuery(getString(R.string.empty))
        searchViewModel.getQuery().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                if (R.id.search_image == switcherSearch.nextView.id) switcherSearch.showNext()
            } else {
                if (R.id.rvSearch == switcherSearch.nextView.id) switcherSearch.showNext()
                searchViewModel.setPersonList(it)
            }
        })
    }

    private fun setPersonListListener() {
        searchViewModel.getPersonList().observe(viewLifecycleOwner, Observer { personResponse ->
            personList.clear()
            personResponse.results.let { personList.addAll(it) }
            rvSearch.adapter?.notifyDataSetChanged()
        })
    }
}