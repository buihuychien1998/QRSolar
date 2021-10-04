package com.hidero.qrsolar.adapters

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.hidero.qrsolar.databinding.ItemHistoryBinding
import com.hidero.qrsolar.entities.History


class HistoryAdapter(var dataList: ArrayList<History>) :
    RecyclerSwipeAdapter<HistoryAdapter.MyViewHolder>() {
    override fun getSwipeLayoutResourceId(position: Int): Int {
        return com.hidero.qrsolar.R.id.swipeLayout
    }

    var preswipes: SwipeLayout? = null

    private val TAG = "HistoryAdapter"
    private var recyclerView: RecyclerView? = null
    private var mListener: OnSwipeItem? = null
    var listener: OnItemClickListener? = null

    interface OnSwipeItem {
        fun onSwipeLeft(item: History)
        fun onSwipeRight(item: History)
        fun onSwipeTop(item: History)
        fun onSwipeBottom(item: History)
        fun onClickItem(item: History)
    }

    private var removedPosition: Int = 0
    fun getRemovedPosition(): Int {
        return removedPosition
    }

    private var removedItem: History? = null
    fun getRemovedItem(): History {
        return removedItem!!
    }

    fun setList(history: ArrayList<History>) {
        dataList = history
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        setOnItemClickListener(listener)
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemHistoryBinding>(
            layoutInflater,
            com.hidero.qrsolar.R.layout.item_history,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.data = data
        holder.binding.itemContainer.setOnClickListener {
            listener?.onClick(it, data)
        }
        holder.bindItem(data, position)
        mItemManger.bindView(holder.binding.root, position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface OnItemClickListener {
        fun onClick(view: View, data: History)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun setOnSwipeItem(listener: OnSwipeItem?) {
        this.mListener = listener
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        Log.e(TAG, "Remove item")
        removedPosition = viewHolder.adapterPosition
        removedItem = dataList.get(viewHolder.adapterPosition)
        dataList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    inner class MyViewHolder(var binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private val swipeLayout: SwipeLayout = binding.swipeLayout

        init {
            swipeLayout.addDrag(SwipeLayout.DragEdge.Right, binding.rightLayout)
//            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, binding.leftLayout)
        }

        fun close() {
            swipeLayout.close()
        }

        fun bindItem(item: History, position: Int) {

            // Set swipe style
            swipeLayout.showMode = SwipeLayout.ShowMode.PullOut

            swipeLayout.addRevealListener(
                com.hidero.qrsolar.R.id.rightLayout,
                SwipeLayout.OnRevealListener { child, edge, fraction, distance ->
                    Toast.makeText(child.context, position.toString(), Toast.LENGTH_SHORT).show()


                })
            swipeLayout.findViewById<ImageView>(com.hidero.qrsolar.R.id.ivShare).setOnClickListener {
                Toast.makeText(swipeLayout.context, "Share", Toast.LENGTH_SHORT).show()
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    item.title
                )
                startActivity(
                    swipeLayout.context,
                    Intent.createChooser(shareIntent, "Share"),
                    Bundle.EMPTY
                )
                close()
            }
            swipeLayout.findViewById<ImageView>(com.hidero.qrsolar.R.id.ivDelete).setOnClickListener {
                close()
            }
//            swipeLayout.surfaceView.setOnClickListener {
//                when {
//                    item.image == com.hidero.qrsolar.R.drawable.ic_web -> {
//                        var intent = Intent(Intent.ACTION_VIEW)
//                        intent.setData(Uri.parse(item.title))
//                        startActivity(swipeLayout.context, intent, Bundle.EMPTY)
//                    }
//                    item.image == com.hidero.qrsolar.R.drawable.ic_email -> {
//                        var email = Intent(Intent.ACTION_SEND)
//                        email.setType("text/plain")
//                        email.putExtra(Intent.EXTRA_EMAIL, item.title)
//                        startActivity(swipeLayout.context, Intent.createChooser(email, "Send mail..."), Bundle.EMPTY)
//                    }
//                    else -> Toast.makeText(swipeLayout.context, "${item.title}", Toast.LENGTH_SHORT).show()
//                }
//            }

//             Set listener
            mItemManger.bindView(binding.root, position)
            swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onClose(layout: SwipeLayout) {
                }

                override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}

                override fun onStartOpen(layout: SwipeLayout) {
                    mItemManger.closeAllExcept(layout)
//                    if (preswipes == null) {
//                        preswipes = layout;
//                    } else {
//                        preswipes!!.close(true);
//                        preswipes = layout;
//                    }
//                    for(i in dataList.indices){
//                        if(i == position){
//                            continue
//                        }else{
//                            mItemManger.closeItem(i)
//                        }
//                    }
                }

                override fun onOpen(layout: SwipeLayout) {
                }

                override fun onStartClose(layout: SwipeLayout) {}

                override fun onHandRelease(layout: SwipeLayout, xvel: Float, yvel: Float) {
//                    val edge = layout.dragEdge.name
//                    if (layout.openStatus.toString() !== "Close") {
//                        when (edge) {
//                            SwipeLayout.DragEdge.Right.name -> {
//                                // Drag RIGHT
//                                mListener?.onSwipeRight(item)
//
//                            }
//                            SwipeLayout.DragEdge.Left.name -> {
//                                // Drag LEFT
//                                mListener?.onSwipeLeft(item)
//                            }
//                            SwipeLayout.DragEdge.Top.name -> {
//                                // Drag TOP
//                                mListener?.onSwipeTop(item)
//                            }
//                            SwipeLayout.DragEdge.Bottom.name -> {
//                                // Drag BOTTOM
//                                mListener?.onSwipeBottom(item)
//                            }
//                        }
//                    }
                }
            })
        }
    }
}