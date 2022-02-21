package com.aksoy.mybet.fragments

import android.os.Binder
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
import com.aksoy.mybet.models.User
import com.aksoy.mybet.utils.AlertDialogType
import com.aksoy.mybet.utils.BaseFragment
import com.aksoy.mybet.utils.SelectListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseFragment(), View.OnClickListener {

    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var email = ""
    private var password = ""
    private var firstName = ""
    private var lastName = ""
    private var confirmPassword = ""
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
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
        firstName = et_name.text.toString().trim()
        lastName = et_surname.text.toString().trim()
        email = et_email.text.toString().trim()
        password = et_password.text.toString().trim()
        confirmPassword = et_password_again.text.toString().trim()
        if(firstName.isEmpty() && lastName.isEmpty()){
            Toast.makeText(requireContext(),"Lütfen adınızı yazınız",Toast.LENGTH_SHORT).show()
        }else if(password.isEmpty()){
            Toast.makeText(requireContext(),"Lütfen şifrenizi yazınız",Toast.LENGTH_SHORT).show()
        }else if(confirmPassword.isEmpty()){
            Toast.makeText(requireContext(),"Şifrenizi doğrulayınız",Toast.LENGTH_SHORT).show()
        }else if(password != confirmPassword){
            Toast.makeText(requireContext(),"Şifreler eşleşmiyor",Toast.LENGTH_SHORT).show()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.error = "e-mail formatını kontrol ediniz"
        }else if(password.length < 6) {
            et_password.error = "Şifreniz en az 6 karakter olmalıdır."
        }else {
            createUserAccount()
        }
    }

    private fun createUserAccount() {
        startProgress()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener { e ->
                dismissProgress()
                Toast.makeText(requireContext(),"Kayıt başarısız oldu ${e.message}",Toast.LENGTH_SHORT).show()

            }

    }

    private fun updateUserInfo() {
        val timestamp = System.currentTimeMillis()
        val uid = firebaseAuth.uid
        val hashMap: HashMap<String,Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["firstName"] = firstName
        hashMap["lastName"] = lastName
        hashMap["email"] = email
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                dismissProgress()
                openAlertDialog(
                    AlertDialogType.Info,
                    "Kaydınız Başarıyla oluşturulmuştur. Tekrar giriş yapmak için Tamam'a tıklayınız.",
                    object : SelectListener<Boolean> {
                        override fun onClickListener(item: Boolean) {
                            Navigation.findNavController(requireView())
                                .navigate(R.id.dashboardFragment)
                        }
                    })

            }
            .addOnFailureListener{ e ->
                dismissProgress()
                Toast.makeText(requireContext(),"Lütfen bilgilerinizi kontrol ediniz ${e.message}",Toast.LENGTH_SHORT).show()

            }

    }
}