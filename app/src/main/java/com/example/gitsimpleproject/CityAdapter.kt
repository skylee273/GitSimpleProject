package com.example.gitsimpleproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_city.view.*

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private val cities = listOf(
        "Seoul" to "SEO",
        "Tokyo" to "TOK",
        "Mountain" to "MTV",
        "Singapore" to "SIN",
        "New York" to "NYC"
    )

    class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (city, code) = cities[position]

        with(holder.itemView) {
            tv_city_name.text = city
            tv_city_code.text = code
        }
    }

    override fun getItemCount(): Int = cities.size


}