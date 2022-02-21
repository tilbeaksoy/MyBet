package com.aksoy.mybet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.aksoy.mybet.R
import com.aksoy.mybet.models.response.SportsResponse
import com.aksoy.mybet.utils.SelectListener
import kotlinx.android.synthetic.main.rv_bet_list_item.view.*


class SportsCategoriesAdapter(
    val list: List<SportsResponse>,  var listener: SelectListener<SportsCategoriesSelectedItem>? = null) :
    RecyclerView.Adapter<SportsCategoriesAdapter.SportsCategoriesViewHolder>() {

    val sportsSelectedItem = MutableLiveData<SportsCategoriesSelectedItem>()

    class SportsCategoriesViewHolder(var view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsCategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_bet_list_item, parent, false)
        return SportsCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportsCategoriesViewHolder, position: Int) {
        val item = list.get(position)
        holder.view.tv_sportsCategory.text = item.title

        holder.view.setOnClickListener {

            var obj = SportsCategoriesSelectedItem(item?.key.toString(),
                item?.title.toString())

            sportsSelectedItem.value = obj
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}