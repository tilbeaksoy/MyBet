package com.aksoy.mybet.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.aksoy.mybet.R
import com.aksoy.mybet.adapters.SportsCategoriesAdapter
import com.aksoy.mybet.adapters.SportsCategoriesSelectedItem
import com.aksoy.mybet.models.response.SportsResponse
import com.aksoy.mybet.utils.BaseFragment
import com.aksoy.mybet.utils.OddsHelper
import com.aksoy.mybet.utils.SelectListener
import com.aksoy.mybet.viewmodels.CategoriesViewModel
import kotlinx.android.synthetic.main.fragment_categories.*
import java.util.*


class CategoriesFragment : BaseFragment() {

    private lateinit var viewModel: CategoriesViewModel
    private lateinit var suplementAdapter: SportsCategoriesAdapter
    private var categoriesList: ArrayList<SportsResponse>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]
        setObservers()
        viewModel.getSports()
        initView()


    }

    private fun initView() {
        et_search_categories.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text.length > 1) {
                    val list =
                        categoriesList?.filter { k ->
                            (k.title.lowercase(
                                Locale(
                                    "tr",
                                    "TR"
                                )
                            )).contains(
                                text.lowercase(
                                    Locale("tr", "TR")
                                )
                            )
                        }?.toList()
                    if (list?.isNotEmpty() == true)
                        setCategoriesAdapter(list)
                } else {
                    if (categoriesList != null) {
                        setCategoriesAdapter(categoriesList!!)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setCategoriesAdapter(list: List<SportsResponse>) {
        view?.hideKeyboard()
        val adapter = SportsCategoriesAdapter(list,
            object : SelectListener<SportsCategoriesSelectedItem> {
                override fun onClickListener(item: SportsCategoriesSelectedItem) {
                    et_search_categories.setText(item.sport_tittle)
                    OddsHelper.setKeyNTittle(item.sport_key, item.sport_tittle!!)
                    Navigation.findNavController(requireView())
                        .navigate(R.id.categoriesDetailFragment)
                }
            })
        dismissProgress()
        rv_bet_list.adapter = adapter
    }

    private fun setObservers() {
        startProgress()
        viewModel.sportsResponse.observe(viewLifecycleOwner, { response ->
            if (!response.isNullOrEmpty()) {
                dismissProgress()
                categoriesList = ArrayList(response)
                suplementAdapter = SportsCategoriesAdapter(response)
                rv_bet_list.adapter = suplementAdapter
                suplementAdapter.sportsSelectedItem.observe(viewLifecycleOwner, { response ->
                    response?.let {
                        if (!response.sport_key.isNullOrEmpty() || !response.sport_tittle.isNullOrEmpty()) {
                            OddsHelper.setKeyNTittle(response.sport_key, response.sport_tittle!!)
                            Navigation.findNavController(requireView())
                                .navigate(R.id.categoriesDetailFragment)
                        }

                    }
                })
            }
        })
    }


}