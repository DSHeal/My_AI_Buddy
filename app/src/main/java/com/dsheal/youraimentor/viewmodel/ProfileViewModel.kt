package com.dsheal.youraimentor.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsheal.youraimentor.domain.models.User
import com.dsheal.youraimentor.domain.repository.UserRepository
import com.dsheal.youraimentor.utils.State
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    val isUserAuthorized = firebaseAuth.currentUser != null

    var profileStateMutableLiveData: MutableLiveData<State<User>> = MutableLiveData()
    val profileStateLiveData: LiveData<State<User>> = profileStateMutableLiveData

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount, activity: Activity) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    saveUserInDb(user)
                    updateUI()
                    updateUserInRemoteDb(user)
                } else {
                    Log.w(TAG, "Firebase authentication failed", task.exception)
                }
            }
    }

    fun updateUserInRemoteDb(currentUser: FirebaseUser?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.updateUserInRemoteDb(currentUser)
            }
        }
    }

    fun saveUserInDb(user: FirebaseUser?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.saveUserInDb(User(email = user?.email ?: ""))
            }
        }
    }

    fun updateUI() {
        viewModelScope.launch {
            userRepository.listenUserFromDb().collect { user ->
                profileStateMutableLiveData.value = State.Success<User>(user)
            }
        }
    }

    companion object {
        private const val TAG = "Profile ViewModel"
    }
}