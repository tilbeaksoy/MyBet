package com.aksoy.mybet.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.aksoy.mybet.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.login_button
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private var email = ""
    private var password = ""


    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        initView()

    }
    private fun initView() {
        login_button.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
       when(v) {
           login_button -> {
               validateData()
           }
       }
    }
    private fun validateData() {
        email = et_email.text.toString().trim()
        password = et_password.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.error = "e-mail formatını kontrol ediniz"
        }else if(TextUtils.isEmpty(password)){
            et_password.error = "Lütfen şifrenizi giriniz."
        }else {
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
       firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
           val firebaseUser = firebaseAuth.currentUser
           val email = firebaseUser?.email
           Toast.makeText(requireContext(),"Giriş Yapıldı: $email", Toast.LENGTH_SHORT).show()
           Navigation.findNavController(requireView())
               .navigate(R.id.dashboardFragment)

       }.addOnFailureListener {
           Toast.makeText(requireContext(),"Giriş başarısız oldu", Toast.LENGTH_SHORT).show()
       }
    }


}