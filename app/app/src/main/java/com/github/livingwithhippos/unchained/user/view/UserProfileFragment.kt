package com.github.livingwithhippos.unchained.user.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.github.livingwithhippos.unchained.databinding.FragmentUserProfileBinding
import com.github.livingwithhippos.unchained.user.viewmodel.UserProfileViewModel
import com.github.livingwithhippos.unchained.utilities.openExternalWebPage
import dagger.hilt.android.AndroidEntryPoint


const val REFERRAL_LINK = "http://real-debrid.com/?id=78841"
const val PREMIUM_LINK = "https://real-debrid.com/premium"

/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private val viewModel: UserProfileViewModel by viewModels()

    val args: UserProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.saveToken(args.token)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val userBinding = FragmentUserProfileBinding.inflate(inflater, container, false)

        viewModel.fetchUserInfo()

        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {

            if (it != null) {
                userBinding.user = it
            }

            //todo: manage null
        })

        userBinding.bPremium.setOnClickListener {
            //todo: ask user and either load the referral link
            // or the premium page, add to settings fragment
            openExternalWebPage(REFERRAL_LINK)
        }
        return userBinding.root
    }
}