package com.thecrimsonpizza.tvtrackerkotlin.app.ui.actor

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.thecrimsonpizza.tvtrackerkotlin.R
import com.thecrimsonpizza.tvtrackerkotlin.app.domain.actor.PersonResponse
import com.thecrimsonpizza.tvtrackerkotlin.app.ui.webview.WebViewActivity
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_INSTAGRAM
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_INSTAGRAM_U
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.BASE_URL_WEB_PERSON
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.COM_INSTAGRAM_ANDROID
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.ID_ACTOR
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.URL_WEBVIEW
import kotlinx.android.synthetic.main.fragment_actor.*

class ActorFragment : Fragment() {

    private var idActor: Int = 0

    private lateinit var actor: PersonResponse.Person
    private val viewModel: ActorViewModel by lazy {
        ViewModelProvider(this@ActorFragment).get(ActorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            idActor = requireArguments().getInt(ID_ACTOR)
        }
        return inflater.inflate(R.layout.fragment_actor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_actor.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar_actor.setNavigationOnClickListener { requireActivity().onBackPressed() }
        toolbar_actor.menu.findItem(R.id.action_insta).isVisible = false
        toolbar_actor.setOnMenuItemClickListener {
            when (it!!.itemId) {
                R.id.action_share -> shareContent()
                R.id.action_insta -> goToInstagram()
            }
            true
        }

        viewModel.getActor(idActor)?.observe(viewLifecycleOwner, Observer {
            // Here we observe livedata
            ActorAdapter(requireContext(), requireView(), it).fillActor()
            actor = it
            if (!it.externalIds?.instagramId.isNullOrEmpty())
                toolbar_actor.menu.findItem(R.id.action_insta).isVisible = true
        })


    }

    private fun goToInstagram() {
        val uri =
            Uri.parse(BASE_URL_INSTAGRAM_U + actor.externalIds?.instagramId)
        val likeIng = Intent(Intent.ACTION_VIEW, uri)
        likeIng.setPackage(COM_INSTAGRAM_ANDROID)
        try {
            startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(requireContext(), WebViewActivity::class.java)
                    .putExtra(
                        URL_WEBVIEW,
                        BASE_URL_INSTAGRAM + actor.externalIds?.instagramId
                    )
            )
        }
    }

    private fun shareContent() {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TITLE, actor.name)
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sharing))
        sendIntent.putExtra(Intent.EXTRA_TEXT, BASE_URL_WEB_PERSON + actor.id)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, null))
    }
}