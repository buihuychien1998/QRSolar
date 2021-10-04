package com.hidero.qrsolar.adapters

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hidero.qrsolar.R
import com.hidero.qrsolar.databinding.ItemCreateBinding
import com.hidero.qrsolar.entities.CreateMenu
import java.util.*

class CreateAdapter(var dataList: ArrayList<CreateMenu>) :
    RecyclerView.Adapter<CreateAdapter.MyViewHolder>() {
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        setOnItemClickListener(listener)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemCreateBinding>(
            layoutInflater,
            R.layout.item_create,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.data = data
        holder.binding.itemContainer.setOnClickListener { it ->
            listener?.onClick(it, data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface OnItemClickListener {
        fun onClick(view: View, data: CreateMenu)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    class MyViewHolder(var binding: ItemCreateBinding) : RecyclerView.ViewHolder(binding.root)
}