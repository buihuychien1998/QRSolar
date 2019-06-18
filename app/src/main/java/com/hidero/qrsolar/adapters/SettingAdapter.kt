//package com.hidero.qrsolar.adapters
//
//import android.content.Context
//import android.support.v7.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import com.hidero.qrsolar.R
//import com.hidero.qrsolar.entities.SettingMenu
//import kotlinx.android.synthetic.main.item_setting.view.*
//import java.util.*
//
//
////class SettingAdapter(
////    private var context: Context,
////    private var layout: Int,
////    private var settings: ArrayList<SettingMenu>
////) :
////    RecyclerView.Adapter<SettingAdapter.MyViewHolder>() {
////    private lateinit var onItemClickListener: OnItemClickListener
////
////    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
////        setOnItemClickListener(onItemClickListener)
////        var inflater: LayoutInflater = LayoutInflater.from(context)
////        var binding = DataBindingUtil.inflate<ItemSettingBinding>(inflater, layout, viewGroup, false)
////        return MyViewHolder(binding, onItemClickListener)
////    }
////
////    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
////        myViewHolder.setBinding(settings[i])
////        myViewHolder.getBinding().item_container.setOnClickListener({
////            onItemClickListener.itemClick(settings[i])
////        })
////    }
////
////
////    override fun getItemCount(): Int {
////        return settings.size
////    }
////
////    inner class MyViewHolder(
////        private var binding: ItemSettingBinding,
////        private var onItemClickListener: OnItemClickListener
////    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
////
////        private var titleMain = ObservableField<String>()
////        private var image = ObservableField<Int>()
////        private var status = ObservableField<Boolean>()
////        private val setting: SettingMenu? = null
////
////        init {
////            binding.root.setOnClickListener(this@MyViewHolder)
////        }
////
////        fun setBinding(setting: SettingMenu) {
////            if (binding.viewHolder == null){
////                binding.viewHolder = this@MyViewHolder
////            }
////            titleMain = setting.titleMain
////            image = setting.image
////            status = setting.status
////
////        }
////
////        fun getBinding(): ItemSettingBinding {
////            return binding
////        }
////        override fun onClick(v: View) {
////            this.onItemClickListener.itemClick(setting)
////        }
////    }
////
////    interface OnItemClickListener {
////        fun itemClick(setting: SettingMenu?)
////    }
////
////    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
////        this.onItemClickListener = onItemClickListener
////    }
////}
//
//class SettingAdapter( val context: Context, var dataList: ArrayList<SettingMenu>) :
//    RecyclerView.Adapter<SettingAdapter.MyViewHolder>() {
//    var listener: OnItemClickListener? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        setOnItemClickListener(listener)
//        val layoutInflater = LayoutInflater.from(context)
//        var view = layoutInflater.inflate(R.layout.item_setting, parent, false)
//        return MyViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val data = dataList[position]
//        holder.tv.text = data.title
//        holder.switch.isChecked = data.status
//        holder.iv.setImageResource(data.image)
//        holder.itemView.setOnClickListener {
//            listener?.onClick(it, data)
//        }
//        holder.switch.setOnCheckedChangeListener { _, isChecked ->
//            Toast.makeText(context, data.title, Toast.LENGTH_SHORT).show()
//            data.status = isChecked
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size
//    }
//
//    interface OnItemClickListener {
//        fun onClick(view: View, data: SettingMenu)
//    }
//
//    private fun setOnItemClickListener(listener: OnItemClickListener?) {
//        this.listener = listener
//    }
//
//    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        //        var switch = itemView.findViewById<Switch>(R.id.sStatus)
//        var tv = itemView.tv
//        var switch = itemView.sStatus
//        var iv = itemView.iv
//
//    }
//}