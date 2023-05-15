package com.dsheal.youraimentor.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dsheal.youraimentor.R
import com.dsheal.youraimentor.databinding.FragmentProfileBinding
import com.dsheal.youraimentor.utils.State
import com.dsheal.youraimentor.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    var mBinding: FragmentProfileBinding? = null
    val binding
        get() = mBinding!!

    private lateinit var googleSignInClient: GoogleSignInClient

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.btnSignIn.setOnClickListener {
            signInWithGoogle()
        }

        if (viewModel.isUserAuthorized) {
            viewModel.updateUI()
        } else {
            binding.layoutUnauthorized.isVisible = true
            binding.layoutAuthorized.isVisible = false
        }

        viewModel.profileStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Success -> {
                    binding.layoutUnauthorized.isVisible = false
                    binding.layoutAuthorized.isVisible = true
                    binding.textEmail.text = state.data?.email ?: ""
                }
                is State.Loading -> {}
                is State.Failure -> {}
            }
        }
    }

    val signInActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    viewModel.firebaseAuthWithGoogle(account, requireActivity())
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign-in failed", e)
                }
            }
        }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        signInActivityResultLauncher.launch(signInIntent)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "Profile Fragment"
    }
}