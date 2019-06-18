package com.hidero.qrsolar.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.daimajia.swipe.util.Attributes
import com.hidero.qrsolar.R
import com.hidero.qrsolar.activities.MainActivity
import com.hidero.qrsolar.activities.MainActivity.Companion.opt
import com.hidero.qrsolar.adapters.HistoryAdapter
import com.hidero.qrsolar.databinding.FragmentHistoryBinding
import com.hidero.qrsolar.entities.History
import com.hidero.qrsolar.utils.SnapHelperOneByOne
import com.hidero.qrsolar.viewmodels.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_history.*


class HistoryFragment : Fragment() {
    private lateinit var historyBinding: FragmentHistoryBinding
    lateinit var histories: ArrayList<History>
    var historyAdapter: HistoryAdapter? = null
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FFFFFF"))
    private lateinit var deleteIcon: Drawable
    private var historyViewModel: HistoryViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        historyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        historyBinding.history = this@HistoryFragment
        return historyBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        opt = -1
        histories = ArrayList()
        historyAdapter = HistoryAdapter(histories)
        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        historyViewModel!!.getAll().observe(this, Observer {
            if (it == null || it.isEmpty()) {
                historyAdapter!!.setList(it as ArrayList<History>)
                histories = it
                Toast.makeText(activity!!, "Bạn chưa scan một qr nào...", Toast.LENGTH_SHORT).show()
            } else {
                historyAdapter!!.setList(it as ArrayList<History>)
                histories = it
            }
            checkEmptyHistory()
        })
        historyBinding.rvHistory.adapter = historyAdapter
//        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
//        itemTouchHelper.attachToRecyclerView(historyBinding.rvHistory)
        deleteIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_delete)!!

        checkEmptyHistory()

        rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && MainActivity.binding.bottomNavigation.isShown()) {
                    MainActivity.binding.bottomNavigation.visibility = View.GONE
                } else if (dy < 0) {
                    MainActivity.binding.bottomNavigation.visibility = View.VISIBLE

                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)
            }
        })
//        Set one swipe at a time
        historyAdapter!!.mode = Attributes.Mode.Single
        historyAdapter!!.setOnItemClickListener(listener = mClick)
        historyAdapter!!.setOnSwipeItem(listener = object : HistoryAdapter.OnSwipeItem {
            override fun onSwipeLeft(item: History) {

            }

            override fun onSwipeRight(item: History) {
            }

            override fun onSwipeTop(item: History) {
            }

            override fun onSwipeBottom(item: History) {
            }

            override fun onClickItem(item: History) {
                Toast.makeText(activity, "${item.title}", Toast.LENGTH_SHORT).show()
            }

        })
        var snapHelper = SnapHelperOneByOne()
        snapHelper.attachToRecyclerView(rvHistory)
    }

    var mClick = object : HistoryAdapter.OnItemClickListener {
        override fun onClick(view: View, data: History) {
            when {
                data.image == R.drawable.ic_web -> {
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(data.title))
                    startActivity(intent)
                }
                data.image == R.drawable.ic_email -> {
                    var email = Intent(Intent.ACTION_SEND)
                    email.setType("text/plain")
                    email.putExtra(Intent.EXTRA_EMAIL, data.title)
                    startActivity(Intent.createChooser(email, "Send mail..."))
                }
                else -> Toast.makeText(activity, "${data.title}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        MainActivity.binding.bottomNavigation.visibility = View.VISIBLE
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(R.color.colorbg)
    }

//    private val itemTouchHelperCallBack =
//        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
//            override fun onMove(
//                p0: RecyclerView,
//                p1: RecyclerView.ViewHolder,
//                p2: RecyclerView.ViewHolder
//            ): Boolean {
////                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
//                Log.e("Remove", "Remove data")
//                historyAdapter?.removeItem(viewHolder as HistoryAdapter.MyViewHolder)
//                var removedPosition = historyAdapter!!.getRemovedPosition()
//                var removedItem = historyAdapter!!.getRemovedItem()
//                var kt = true
//                val snackbar = Snackbar.make(
//                    viewHolder.itemView,
//                    "item deleted",
//                    2000
//                ).setAction("Undo") {
//                    kt = false
//                    histories.add(removedPosition, removedItem)
//                    historyAdapter?.notifyItemInserted(removedPosition)
////                    Kiểm tra khi undo
//                    checkEmptyHistory()
//                }
//                val layoutParams = CoordinatorLayout.LayoutParams(
//                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
//                    CoordinatorLayout.LayoutParams.WRAP_CONTENT
//                )
//                val margin =
//                    resources.getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material)
////        val margin = 56
//                layoutParams.gravity = Gravity.BOTTOM
//                layoutParams.setMargins(0, 0, 0, margin)
//                snackbar.view.layoutParams = layoutParams
//                snackbar.setActionTextColor(Color.RED)
//// Changing action button text color
//                val sbView = snackbar.view
//                val textView =
//                    sbView.findViewById(android.support.design.R.id.snackbar_text) as TextView
//                textView.setTextColor(Color.YELLOW)

//                var handler = Handler()
//                handler.postDelayed({
    //                snackbar.show()
//                    if (kt) {
//                        historyViewModel!!.delete(removedItem)
//                    }
//                    checkEmptyHistory()
//
//                }, 2000)
//
//                checkEmptyHistory()
//            }
//
//            override fun onChildDraw(
//                c: Canvas,
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                dX: Float,
//                dY: Float,
//                actionState: Int,
//                isCurrentlyActive: Boolean
//            ) {
//                val itemView = viewHolder.itemView
//
//                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
//
//                if (dX > 0) {
//                    swipeBackground.setBounds(
//                        itemView.left,
//                        itemView.top,
//                        dX.toInt(),
//                        itemView.bottom
//                    )
//                    deleteIcon.setBounds(
//                        itemView.left + iconMargin,
//                        itemView.top + iconMargin,
//                        itemView.left + iconMargin + deleteIcon.intrinsicWidth,
//                        itemView.bottom - iconMargin
//                    )
//                } else {
//                    swipeBackground.setBounds(
//                        itemView.left + dX.toInt(),
//                        itemView.top,
//                        itemView.right,
//                        itemView.bottom
//                    )
//                    deleteIcon.setBounds(
//                        itemView.right - iconMargin - deleteIcon.intrinsicWidth,
//                        itemView.top + iconMargin,
//                        itemView.right - iconMargin,
//                        itemView.bottom - iconMargin
//                    )
//                }
//                swipeBackground.draw(c)
//                c.save()
//                if (dX > 0) {
//                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
//                } else {
//                    c.clipRect(
//                        itemView.left + dX.toInt(),
//                        itemView.top,
//                        itemView.right,
//                        itemView.bottom
//                    )
//                }
//                deleteIcon.draw(c)
//                c.restore()
//                super.onChildDraw(
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//            }
//        }


    fun checkEmptyHistory() {
        if (historyAdapter!!.itemCount == 0 || historyAdapter == null) {
            historyBinding.btnClearAll.visibility = View.INVISIBLE
            historyBinding.emptyHistoryLayout.visibility = View.VISIBLE
            historyBinding.rvHistory.visibility = View.INVISIBLE
        } else {
            historyBinding.btnClearAll.visibility = View.VISIBLE
            historyBinding.emptyHistoryLayout.visibility = View.INVISIBLE
            historyBinding.rvHistory.visibility = View.VISIBLE
        }
    }

    fun myClick(view: View) {
        if (view.id == R.id.btnClearAll) {
            historyViewModel!!.deleteAll()
            historyAdapter!!.notifyDataSetChanged()
            checkEmptyHistory()
        }
    }
}
