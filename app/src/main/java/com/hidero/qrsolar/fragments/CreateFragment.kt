package com.hidero.qrsolar.fragments


import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hidero.qrsolar.R
import com.hidero.qrsolar.activities.MainActivity
import com.hidero.qrsolar.activities.MainActivity.Companion.opt
import com.hidero.qrsolar.adapters.CreateAdapter
import com.hidero.qrsolar.databinding.FragmentCreateBinding
import com.hidero.qrsolar.entities.CreateMenu
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create.*


class CreateFragment : Fragment(), CreateAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCreateBinding
    private var standardAdapter: CreateAdapter? = null
    private var specialAdapter: CreateAdapter? = null
    private var standardMenus: ArrayList<CreateMenu>? = null
    private var specialMenus: ArrayList<CreateMenu>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.hidero.qrsolar.R.layout.fragment_create, container, false)
        binding.create = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        opt = 0
        standardMenus = ArrayList()
        standardMenus!!.add(
            CreateMenu(
                id = ObservableField(1),
                title = ObservableField("Website"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_web)
            )
        )
        standardMenus!!.add(
            CreateMenu(
                id = ObservableField(2),
                title = ObservableField("Phone number"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_phone_number)
            )
        )
        standardMenus!!.add(
            CreateMenu(
                id = ObservableField(3),
                title = ObservableField("QRText"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_text)
            )
        )
        standardMenus!!.add(
            CreateMenu(
                id = ObservableField(4),
                title = ObservableField("Location"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_location)
            )
        )
        standardMenus!!.add(
            CreateMenu(
                id = ObservableField(5),
                title = ObservableField("Email"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_email)
            )
        )
        standardMenus!!.add(
            CreateMenu(
                id = ObservableField(6),
                title = ObservableField("Contact-vCard"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_contact_mecard)
            )
        )
        standardMenus!!.add(
            CreateMenu(
                id = ObservableField(7),
                title = ObservableField("SMS"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_sms)
            )
        )
        standardMenus!!.add(
            CreateMenu(
                id = ObservableField(8),
                title = ObservableField("Event"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_event)
            )
        )
        standardAdapter = CreateAdapter(standardMenus!!)
        binding.rvStandard.adapter = standardAdapter
        specialMenus = ArrayList<CreateMenu>()
        specialMenus!!.add(
            CreateMenu(
                id = ObservableField(9),
                title = ObservableField("Skype"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_skype)
            )
        )
        specialMenus!!.add(
            CreateMenu(
                id = ObservableField(10),
                title = ObservableField("Twitter"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_twitter)
            )
        )
        specialMenus!!.add(
            CreateMenu(
                id = ObservableField(11),
                title = ObservableField("Facebook"),
                image = ObservableField(com.hidero.qrsolar.R.drawable.ic_fb)
            )
        )
        specialAdapter = CreateAdapter(specialMenus!!)
        binding.rvSpecial.adapter = specialAdapter
        standardAdapter!!.listener = this
        specialAdapter!!.listener = this
        rvStandard.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && MainActivity.binding.bottomNavigation.isShown) {
                    MainActivity.binding.bottomNavigation.visibility = View.GONE
                } else if (dy < 0) {
                    MainActivity.binding.bottomNavigation.visibility = View.VISIBLE

                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        rvSpecial.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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



    }


    fun myClick(view: View){
        if (view.id == com.hidero.qrsolar.R.id.btnHome){
            if (activity!!.supportFragmentManager.backStackEntryCount > 0) {
                activity!!.supportFragmentManager.popBackStack()
            }else{
                replaceFragment(MyQRFragment())
            }
        }
    }

    override fun onClick(view: View, data: CreateMenu) {
        when (data.id.get()) {
            1 -> {
                replaceFragment(WebsiteFragment())
            }
            2 -> {
                replaceFragment(PhonenumberFragment())
            }

            3 -> {
                replaceFragment(TextFragment())
            }
            4 -> {
                replaceFragment(LocationFragment())
            }

            5 -> {
                replaceFragment(EmailFragment())
            }
            6 -> {
                replaceFragment(ContactvcardFragment())
            }
            7 -> {
                replaceFragment(SMSFragment())
            }

            8 -> {
                replaceFragment(EventFragment())
            }
            9 -> {
                replaceFragment(SkypeFragment())
            }
            10 -> {
                replaceFragment(TwitterFragment())
            }
            11 -> {
                replaceFragment(FacebookFragment())
            }
            else -> {
                replaceFragment(TextFragment())
                Toast.makeText(activity, "" + data.id.get(), Toast.LENGTH_SHORT).show()
            }
        }



    }



    override fun onResume() {
        super.onResume()
//        if(opt == 0 || opt == 1 || opt == 2 || opt == 3 || opt == 4 || opt == 5 || opt == 6 || opt == 7 || opt == 8 || opt == 9 || opt == 10 || opt == 11){
//            MainActivity.binding.bottomNavigation.visibility = View.GONE
//            MainActivity.binding.bottomNavigation.background = activity!!.getDrawable(android.R.color.transparent)
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(android.R.color.transparent)
//        }else{
            MainActivity.binding.bottomNavigation.visibility = View.GONE
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(R.color.colorbg)
//        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(com.hidero.qrsolar.R.id.content, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }
}
