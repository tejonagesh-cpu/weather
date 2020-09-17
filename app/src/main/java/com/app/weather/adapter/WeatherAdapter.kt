package com.app.weather.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.weather.R
import com.app.weather.db.entity.WeatherDetails
import com.app.weather.extension.getDateFromTimeStamp
import kotlinx.android.synthetic.main.item_weather.view.*

class WeatherAdapter(val context : Context) : RecyclerView.Adapter<WeatherAdapter.MyViewHolder>() {
    private var details = ArrayList<WeatherDetails>()

    fun setWeatherDetails(details : List<WeatherDetails>) {
        this.details = details as ArrayList<WeatherDetails>
        notifyDataSetChanged()
    }
    class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bindView(context: Context, details: ArrayList<WeatherDetails>, position: Int) {
            view.tvDate.text = details[position].dt.getDateFromTimeStamp()
            view.tvTemp.text = details[position].temp + context.getString(R.string.degree_celsius)
            view.tvTempType.text =  details[position].weather
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
         return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_weather,parent,false))
    }

    override fun getItemCount(): Int {
         return details.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(context,details,position)
    }
}