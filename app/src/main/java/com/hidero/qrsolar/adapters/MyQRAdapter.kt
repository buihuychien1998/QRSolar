package com.hidero.qrsolar.adapters

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hidero.qrsolar.R
import com.hidero.qrsolar.databinding.ItemMyqrBinding
import com.hidero.qrsolar.entities.MyQR
import kotlinx.android.synthetic.main.item_myqr.view.*

class MyQRAdapter(var context: Context, var dataList: ArrayList<MyQR>) :
    RecyclerView.Adapter<MyQRAdapter.MyViewHolder>() {
    var listener: OnItemClickListener? = null
    private var removedPosition: Int = 0
    private var TAG = "MyQRAdapter"
    fun getRemovedPosition(): Int {
        return removedPosition
    }

    private var removedItem: MyQR? = null
    fun getRemovedItem(): MyQR {
        return removedItem!!
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        Log.e(TAG, "Remove item")
        removedPosition = viewHolder.adapterPosition
        removedItem = dataList[viewHolder.adapterPosition]
        dataList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        setOnItemClickListener(listener)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemMyqrBinding>(
            layoutInflater,
            R.layout.item_myqr,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.data = data
        if (data.image == null) {
            Log.e("AAAAAAAAAAAAA", "null")
        }
//        holder.iv.setImageBitmap(convertToBitmap(data.image))
        val bitmap = BitmapFactory.decodeByteArray(data.image, 0, data.image.size)
        holder.iv.setImageBitmap(bitmap)
//        Glide.with(context)
//            .load(data.image)
//            .into(holder.iv)
        holder.binding.itemContainer.setOnClickListener {
            listener?.onClick(it, data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface OnItemClickListener {
        fun onClick(view: View, data: MyQR)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun convertToBitmap(bytes: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    class MyViewHolder(var binding: ItemMyqrBinding) : RecyclerView.ViewHolder(binding.root) {
        var iv: ImageView = itemView.iv

    }

    fun setList(list: ArrayList<MyQR>) {
        this.dataList = list
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<MyQR> {
        return dataList
    }
}