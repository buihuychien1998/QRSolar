package com.hidero.qrsolar.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.hidero.qrsolar.R
import com.hidero.qrsolar.activities.MainActivity
import com.hidero.qrsolar.activities.MainActivity.Companion.opt
import com.hidero.qrsolar.adapters.MyQRAdapter
import com.hidero.qrsolar.databinding.FragmentMyQrBinding
import com.hidero.qrsolar.entities.MyQR
import com.hidero.qrsolar.entities.QRText
import com.hidero.qrsolar.viewmodels.EmailViewModel
import com.hidero.qrsolar.viewmodels.MyQRViewModel
import com.hidero.qrsolar.viewmodels.TextViewModel

class MyQRFragment : Fragment(), MyQRAdapter.OnItemClickListener {
    override fun onClick(view: View, data: MyQR) {
        when {
            data.species == "Text" -> {
                vm!!.setValue(QRText(data.id, data.title))
                replaceFragment(TextFragment())
            }
            data.species == "Email" -> {
                evm!!.setValue(data)
                replaceFragment(EmailFragment())
            }
            data.species == "Website" -> {
                evm!!.setValue(data)
                replaceFragment(WebsiteFragment())
            }
            else -> {
                Toast.makeText(activity, data.title, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var vm: TextViewModel? = null
    private var evm: EmailViewModel? = null
    private var viewModel: MyQRViewModel? = null
    private var TAG = MyQRFragment::class.java.simpleName
    private lateinit var binding: FragmentMyQrBinding
    private var myQRAdapter: MyQRAdapter? = null
    private var myQRs: ArrayList<MyQR> = ArrayList()
    private lateinit var deleteIcon: Drawable
    private lateinit var shareIcon: Drawable
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FFFFFF"))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        opt = -1
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_qr, container, false)
        binding.myqr = this
        return binding.root
    }

    private val itemTouchHelperCallBack =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                p0: RecyclerView,
                p1: RecyclerView.ViewHolder,
                p2: RecyclerView.ViewHolder
            ): Boolean {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                Log.e("Remove", "Remove data")

                myQRAdapter?.removeItem(viewHolder as MyQRAdapter.MyViewHolder)
                var removedPosition = myQRAdapter?.getRemovedPosition()
                var removedItem = myQRAdapter!!.getRemovedItem()
                var kt = true
                val snackbar = Snackbar.make(
                    viewHolder.itemView,
                    "item deleted",
                    2000
                ).setAction("Undo") {
                    kt = false
                    myQRs.add(removedPosition!!, removedItem)
                    myQRAdapter?.notifyItemInserted(removedPosition)
                    checkEmpty()

                }

                val layoutParams = CoordinatorLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    CoordinatorLayout.LayoutParams.WRAP_CONTENT
                )
                val margin =
                    resources.getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material)
//        val margin = 56
                layoutParams.gravity = Gravity.BOTTOM
                layoutParams.setMargins(0, 0, 0, margin)
                snackbar.view.layoutParams = layoutParams
                snackbar.setActionTextColor(Color.RED)
// Changing action button text color
                val sbView = snackbar.view
                val textView =
                    sbView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.YELLOW)

                var handler = Handler()
                handler.postDelayed({
                    snackbar.show()

                    if (kt) {

                        Log.e(TAG, removedItem.id.toString())
                        vm!!.delete(QRText(removedItem.id, removedItem.title))
                        viewModel!!.delete(removedItem)
//                        Toast.makeText(activity!!, "Deleted", Toast.LENGTH_LONG).show()
                    }

                }, 2000)


                checkEmpty()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView

                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX > 0) {
                    swipeBackground.setBounds(
                        itemView.left,
                        itemView.top,
                        dX.toInt(),
                        itemView.bottom
                    )
                    deleteIcon.setBounds(
                        itemView.left + iconMargin,
                        itemView.top + iconMargin,
                        itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconMargin
                    )
                } else {
                    swipeBackground.setBounds(
                        itemView.left + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    deleteIcon.setBounds(
                        itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                        itemView.top + iconMargin,
                        itemView.right - iconMargin,
                        itemView.bottom - iconMargin
                    )
                }
                swipeBackground.draw(c)
                c.save()
                if (dX > 0) {
                    c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                } else {
                    c.clipRect(
                        itemView.left + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                }
                deleteIcon.draw(c)
                c.restore()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }


    override fun onResume() {
        super.onResume()
        vm!!.setValue(null)
        MainActivity.binding.bottomNavigation.visibility = View.VISIBLE
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(R.color.colorbg)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        myQRs.add(History(ObservableField(2), ObservableField("Test"), ObservableField(R.drawable.ic_phone_number), ObservableField("04/06/2019 13:26")))
        myQRAdapter = MyQRAdapter(activity!!, myQRs)
        binding.rvMyQR.adapter = myQRAdapter

//        Swiped
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.rvMyQR)
        deleteIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_delete)!!
        shareIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_share)!!
        checkEmpty()
        myQRAdapter!!.setOnItemClickListener(this)

        evm = ViewModelProviders.of(activity!!).get(EmailViewModel::class.java)

    }

    fun onMyClick(view: View) {
        replaceFragment(CreateFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        if (opt == 0 || opt == 1 || opt == 2 || opt == 3 || opt == 4 || opt == 5 || opt == 6 || opt == 7 || opt == 8 || opt == 9 || opt == 10 || opt == 11) {
            MainActivity.binding.bottomNavigation.visibility = View.INVISIBLE
        } else {
            MainActivity.binding.bottomNavigation.visibility = View.VISIBLE
        }
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.content, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyQRViewModel::class.java)
        vm = ViewModelProviders.of(activity!!).get(TextViewModel::class.java)

        viewModel!!.getAll().observe(this, Observer {
            myQRAdapter!!.setList((it as ArrayList<MyQR>?)!!)
            myQRs = myQRAdapter!!.getList()
            if (it != null && it.isNotEmpty()) {
                binding.rvMyQR.visibility = View.VISIBLE
                binding.emptyMyQRLayout.visibility = View.INVISIBLE
                binding.btnCreate.visibility = View.VISIBLE
            }

//            Toast.makeText(activity!!, myQRAdapter!!.itemCount.toString(), Toast.LENGTH_SHORT)
//                .show()
        })

    }

    fun checkEmpty() {
        if (myQRs.isEmpty()) {
            binding.btnCreate.visibility = View.INVISIBLE
            binding.rvMyQR.visibility = View.INVISIBLE
            binding.emptyMyQRLayout.visibility = View.VISIBLE
        } else {
            binding.btnCreate.visibility = View.VISIBLE
            binding.rvMyQR.visibility = View.VISIBLE
            binding.emptyMyQRLayout.visibility = View.INVISIBLE
        }
    }

}
