package com.aksoy.mybet.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.aksoy.mybet.R
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
            Toast.makeText(requireContext(),"L??tfen ad??n??z?? yaz??n??z",Toast.LENGTH_SHORT).show()
        }else if(password.isEmpty()){
            Toast.makeText(requireContext(),"L??tfen ??ifrenizi yaz??n??z",Toast.LENGTH_SHORT).show()
        }else if(confirmPassword.isEmpty()){
            Toast.makeText(requireContext(),"??ifrenizi do??rulay??n??z",Toast.LENGTH_SHORT).show()
        }else if(password != confirmPassword){
            Toast.makeText(requireContext(),"??ifreler e??le??miyor",Toast.LENGTH_SHORT).show()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.error = "e-mail format??n?? kontrol ediniz"
        }else if(password.length < 6) {
            et_password.error = "??ifreniz en az 6 karakter olmal??d??r."
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
                Toast.makeText(requireContext(),"Kay??t ba??ar??s??z oldu ${e.message}",Toast.LENGTH_SHORT).show()

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
                    "Kayd??n??z Ba??ar??yla olu??turulmu??tur. Tekrar giri?? yapmak i??in Tamam'a t??klay??n??z.",
                    object : SelectListener<Boolean> {
                        override fun onClickListener(item: Boolean) {
                            Navigation.findNavController(requireView())
                                .navigate(R.id.dashboardFragment)
                        }
                    })

            }
            .addOnFailureListener{ e ->
                dismissProgress()
                Toast.makeText(requireContext(),"L??tfen bilgilerinizi kontrol ediniz ${e.message}",Toast.LENGTH_SHORT).show()

            }

    }
}