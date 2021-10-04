package com.hidero.qrsolar.activities

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.hidero.qrsolar.databinding.ActivityMainBinding
import com.hidero.qrsolar.fragments.*
import com.hidero.qrsolar.interfaces.GetImagePathFromGallery


class MainActivity : AppCompatActivity(), GetImagePathFromGallery {
    override fun getPath(kt: Boolean): String {
//        return if(kt){
//            var pathImage: String = ""
//            getPathImage(pathImage)
//            pathImage!!
//        }else{
//            ""
//        }
        var bundle = intent.extras
        var img = ""
        if (bundle != null) {
            img = bundle.getString("pathImage")!!
        }
        Log.e(TAG, img)
        return img

    }
    private var TAG = MainActivity::class.java.simpleName
    companion object {
        var myBundle: Bundle? = null

        var opt = -1
        var qrs = arrayOf(
            com.hidero.qrsolar.R.drawable.ic_yellow_qr,
            com.hidero.qrsolar.R.drawable.ic_purple_qr,
            com.hidero.qrsolar.R.drawable.ic_pink_qr,
            com.hidero.qrsolar.R.drawable.ic_blue_qr,
            com.hidero.qrsolar.R.drawable.ic_green_qr
        )
        lateinit var binding: ActivityMainBinding
    }

    private var currentFragment: Fragment? = null
    //    private var settingFragment = SettingFragment()
    private var intentIntegrator: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, com.hidero.qrsolar.R.layout.activity_main)
        binding.main = this@MainActivity
//        replaceFragment(PhotoEditorFragment())
        replaceFragment(VisionFragment())
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            mOnNavigationItemSelectedListener(menuItem)
        }

    }

//    private fun getPathImage(img: String?): Unit {
//        var bundle = getIntent().extras
//        img = ""
//        if (bundle != null) {
//            img = bundle.getString("pathImage")
//        }
//    }

    private fun replaceFragment(fragment: Fragment) {
        if (opt == 1 || opt == 2 || opt == 3 || opt == 4 || opt == 5 || opt == 6 || opt == 7 || opt == 8 || opt == 9 || opt == 10 || opt == 11) {
            binding.bottomNavigation.visibility = View.INVISIBLE
        } else {
            binding.bottomNavigation.visibility = View.VISIBLE
        }
        currentFragment = fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(com.hidero.qrsolar.R.id.content, fragment)
        if (fragment is CreateFragment) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()

    }

    fun mOnNavigationItemSelectedListener(item: MenuItem): Boolean {
        when (item.itemId) {
            com.hidero.qrsolar.R.id.navScan -> {
//                intentIntegrator = IntentIntegrator(this@MainActivity)
//                intentIntegrator!!.initiateScan()
//                Tranh replace cung 1 fragment 2 lan
//                if (currentFragment !is ScanFragment)
//                    replaceFragment(ScanFragment())
                binding.bottomNavigation.background = getDrawable(android.R.color.black)
//                if (currentFragment !is BlankFragment)
//                    replaceFragment(BlankFragment())
                if (currentFragment !is VisionFragment)
                    replaceFragment(VisionFragment())
                return true
            }
            com.hidero.qrsolar.R.id.navMyQR -> {
                binding.bottomNavigation.background = getDrawable(com.hidero.qrsolar.R.color.colorbg)

                if (currentFragment !is MyQRFragment)
                    replaceFragment(MyQRFragment())
                return true
            }
            com.hidero.qrsolar.R.id.navHistory -> {
                binding.bottomNavigation.background = getDrawable(com.hidero.qrsolar.R.color.colorbg)

                if (currentFragment !is HistoryFragment)
                    replaceFragment(HistoryFragment())
                return true
            }

            com.hidero.qrsolar.R.id.navSetting -> {
                binding.bottomNavigation.background = getDrawable(com.hidero.qrsolar.R.color.colorbg)

                if (currentFragment !is SettingFragment) {
//                    Log.e("SaveInstanceState", "Bundle is null " + (myBundle == null))
//                    if (supportFragmentManager.findFragmentByTag("setting") == null) {
//                        currentFragment = settingFragment
//                        val fragmentTransaction = supportFragmentManager.beginTransaction()
//                        fragmentTransaction.replace(com.hidero.qrsolar.R.id.content, settingFragment, "setting")
//                        fragmentTransaction.commit()
//                    } else {
//                        settingFragment = supportFragmentManager.findFragmentByTag("setting") as SettingFragment
//                    }
                    replaceFragment(SettingFragment())
                }

                return true
            }

        }
        return false

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Scan successful", Toast.LENGTH_SHORT).show()
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
//        if(opt == 1 || opt == 2 || opt == 3 || opt == 4 || opt == 5 || opt == 6 || opt == 7 || opt == 8 || opt == 9 || opt == 10 || opt == 11){
//            MainActivity.binding.bottomNavigation.visibility = View.GONE
////            binding.mainLayout.background = getDrawable(android.R.color.white)
//        }else {
//            MainActivity.binding.bottomNavigation.visibility = View.VISIBLE
////            binding.mainLayout.background = getDrawable(R.color.colorbg)
//        }
    }
}
