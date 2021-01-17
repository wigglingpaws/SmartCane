package com.example.smartcane.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.smartcane.R
import com.example.smartcane.holder.MainViewHolder
import com.example.smartcane.model.ModelContact
import java.util.*

class MainAdapter(private val context: Context,
                  private val daftarContact: ArrayList<ModelContact?>?) : RecyclerView.Adapter<MainViewHolder>() {
    private val listener: FirebaseDataListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val generator = ColorGenerator.MATERIAL //custom color
        val color = generator.randomColor
        holder.view.setCardBackgroundColor(color)
        holder.namaContact.text = "Name   : " + (daftarContact?.get(position)?.name)
        holder.relationContact.text = "Relation     : " + daftarContact?.get(position)?.relation
        holder.numberrContact.text = "Number   : " + daftarContact?.get(position)?.number
        holder.view.setOnClickListener { listener.onDataClick(daftarContact?.get(position), position) }
    }

    override fun getItemCount(): Int {

        return daftarContact?.size!!
    }

    //interface data listener
    interface FirebaseDataListener {
        fun onDataClick(contact: ModelContact?, position: Int)
    }

    init {
        listener = context as FirebaseDataListener
    }
}