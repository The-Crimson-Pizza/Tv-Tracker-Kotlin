package com.thecrimsonpizza.tvtrackerkotlin.app.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.core.base.BaseActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImagePortrait
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_POSTER
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_ACTOR
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.TYPE_FRAGMENT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.Type
import kotlinx.android.synthetic.main.fragment_actor_search.*
import kotlinx.android.synthetic.main.lista_series_basic.view.*

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
            personList, R.layout.lista_series_basic,
            GridLayoutManager(activity, 3)
        ) {
            itemView.layoutParams.width = (requireView().width * 0.3).toInt()

            itemView.posterBasic.getImagePortrait(
                requireContext(), BASE_URL_IMAGES_POSTER + it.profilePath
            )
            itemView.ratingBasic.visibility = View.GONE
            itemView.titleBasic.text = it.name
            itemView.setOnClickListener { view -> goToPersonActivity(view, it) }
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

    private fun goToPersonActivity(v: View, person: PersonResponse.Person) {

        val intent = Intent(activity, BaseActivity::class.java).apply {
            putExtras(Bundle().apply {
                putExtra(TYPE_FRAGMENT, Type.PERSON)
                putExtra(ID_ACTOR, person.id)
//                    putParcelable(GlobalConstants.BASIC_PERSON_POSTER_PATH, person)
            })
        }
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            Pair(v.posterBasic, v.posterBasic.transitionName)
        )

        ActivityCompat.startActivity(requireContext(), intent, activityOptions.toBundle())

    }
}