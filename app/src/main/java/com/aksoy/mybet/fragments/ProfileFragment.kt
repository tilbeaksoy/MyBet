package com.aksoy.mybet.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.aksoy.mybet.R
import com.aksoy.mybet.models.User
import com.aksoy.mybet.utils.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : BaseFragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference:DatabaseReference
    private lateinit var user : User
    private lateinit var uid: String

    companion object {
        fun newInstance() = ProfileFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if(uid.isNotEmpty()){
            startProgress()
            getUserData()
        }

    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dismissProgress()
              user = snapshot.getValue(User::class.java)!!
                tv_name.text = user.firstName
                tv_lastname.text = user.lastName
                tv_email.text = user.email

            }

            override fun onCancelled(error: DatabaseError) {
                dismissProgress()
                Toast.makeText(requireContext(),"Bir şeyler ters gitti", Toast.LENGTH_SHORT).show()
            }
        })
    }

}