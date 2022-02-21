package com.aksoy.mybet.fragments

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
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment(), View.OnClickListener {

    private lateinit var firebaseAuth:FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        initView()
    }

    private fun initView() {
       sign_up_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v)  {
            sign_up_button -> {
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
        }else if(password.length < 6) {
            et_password.error = "Şifreniz en az 6 karakter olmalıdır."
        }else {
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
       firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
           val firebaseUser = firebaseAuth.currentUser
           val email = firebaseUser?.email
           Toast.makeText(requireContext(),"Kayıt Yapıldı: $email", Toast.LENGTH_SHORT).show()
           Navigation.findNavController(requireView())
               .navigate(R.id.dashboardFragment)
       }.addOnFailureListener {
           Toast.makeText(requireContext(),"Kayıt başarısız oldu", Toast.LENGTH_SHORT).show()

       }
    }
}