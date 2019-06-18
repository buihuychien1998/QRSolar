package com.hidero.qrsolar.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hidero.qrsolar.R
import com.hidero.qrsolar.activities.MainActivity

class TwitterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_twiter, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (MainActivity.opt == 0 || MainActivity.opt == 1 || MainActivity.opt == 2 || MainActivity.opt == 3 || MainActivity.opt == 4 || MainActivity.opt == 5 || MainActivity.opt == 6 || MainActivity.opt == 7 || MainActivity.opt == 8 || MainActivity.opt == 9 || MainActivity.opt == 10 || MainActivity.opt == 11) {
            MainActivity.binding.bottomNavigation.visibility = View.GONE
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(android.R.color.white)
        } else {
            MainActivity.binding.bottomNavigation.visibility = View.VISIBLE
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(R.color.colorbg)
        }
    }
}
