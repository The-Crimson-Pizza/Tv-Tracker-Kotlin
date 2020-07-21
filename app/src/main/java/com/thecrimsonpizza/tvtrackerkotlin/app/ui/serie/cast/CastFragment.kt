package com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.cast

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.Credits
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.serie.SerieResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor.PersonActivity
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.serie.SeriesViewModel
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.getImagePortrait
import com.thecrimsonpizza.tvtrackerkotlin.core.extensions.setBaseAdapter
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ACTOR_TRANSITION
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_IMAGES_PORTRAIT
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASIC_PERSON
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_ACTOR
import kotlinx.android.synthetic.main.fragment_cast.*
import kotlinx.android.synthetic.main.lista_cast_vertical.view.*


class CastFragment : Fragment() {

    private val serieViewModel: SeriesViewModel by activityViewModels()

    private var mSerie: SerieResponse.Serie? = null
    private val mCast: MutableList<Credits.Cast> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cast, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridCasting.setBaseAdapter(
            mCast, R.layout.lista_cast_vertical,
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        ) { cast ->
            itemView.actor_name.text = cast.name
            itemView.character_name.text = cast.character
            itemView.profile_image.getImagePortrait(requireContext(), BASE_URL_IMAGES_PORTRAIT +cast.profilePath.toString())
            itemView.setOnClickListener { v ->
//                val bundle = Bundle()
//                bundle.putInt(ID_ACTOR, cast.id)
//                Navigation.findNavController(v).navigate(R.id.action_series_to_actores, bundle)

                val intent = Intent(activity, PersonActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putExtra(ID_ACTOR, cast.id)
                        putParcelable(BASIC_PERSON, cast)
                    })
                }
                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    Pair(view.profile_image, ACTOR_TRANSITION)
                )

                ActivityCompat.startActivity(requireContext(), intent, activityOptions.toBundle())
            }
        }

        serieViewModel.getShow().observe(viewLifecycleOwner, Observer {
            mCast.clear()
            it.credits.cast.let { it1 -> mCast.addAll(it1) }
            gridCasting.adapter?.notifyDataSetChanged()

            if (R.id.gridCasting == switcher_cast.nextView.id) switcher_cast.showNext()
        })
    }
}