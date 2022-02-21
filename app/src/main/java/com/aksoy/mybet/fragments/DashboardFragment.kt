package com.aksoy.mybet.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.aksoy.mybet.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var categoriesFragment = CategoriesFragment()
    private var cartFragment = CartFragment()
    private var profileFragment = ProfileFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.sports_category -> replaceFragment(categoriesFragment)
                R.id.cart_categoory -> replaceFragment(cartFragment)
                R.id.profile_category -> replaceFragment(profileFragment)
            }
            true
        }
        bottom_navigation.selectedItemId = R.id.sports_category;

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()

        }
    }
    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            Navigation.findNavController(requireView())
                .navigate(R.id.loginFragment)
        }
    }

}