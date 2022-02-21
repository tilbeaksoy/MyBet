package com.aksoy.mybet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.aksoy.mybet.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        initView()
    }


    private fun initView() {


        login_button.setOnClickListener(this)
        sign_up_button.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v) {
            login_button -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.loginFragment)
            }
            sign_up_button -> {
                Navigation.findNavController(requireView())
                    .navigate(R.id.signUpFragment)
            }
        }
    }
    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            Navigation.findNavController(requireView())
                .navigate(R.id.dashboardFragment)
        }
    }

}