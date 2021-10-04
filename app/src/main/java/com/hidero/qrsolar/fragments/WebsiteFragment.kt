package com.hidero.qrsolar.fragments


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.WriterException

import com.hidero.qrsolar.R
import com.hidero.qrsolar.activities.MainActivity
import com.hidero.qrsolar.databinding.FragmentTextBinding
import com.hidero.qrsolar.databinding.FragmentWebsiteBinding
import com.hidero.qrsolar.entities.MyQR
import com.hidero.qrsolar.entities.QRText
import com.hidero.qrsolar.viewmodels.MyQRViewModel
import com.hidero.qrsolar.viewmodels.TextViewModel
import kotlinx.android.synthetic.main.fragment_text.*
import kotlinx.android.synthetic.main.fragment_text.btnHome
import kotlinx.android.synthetic.main.fragment_text.btnSave
import kotlinx.android.synthetic.main.fragment_text.tvCount
import kotlinx.android.synthetic.main.fragment_website.*
import java.io.ByteArrayOutputStream

class WebsiteFragment : Fragment(), TextViewBindingAdapter.OnTextChanged {

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textViewModel.setData(s.toString())
    }

    private var TAG = WebsiteFragment::class.java.simpleName
    private var vm: TextViewModel? = null
    private lateinit var textViewModel: TextViewModel
    private lateinit var myQRViewModel: MyQRViewModel
    private lateinit var binding: FragmentWebsiteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.hidero.qrsolar.R.layout.fragment_website, container, false)
        binding.website = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.opt = 3
        textViewModel = ViewModelProviders.of(this).get(TextViewModel::class.java)
        textViewModel.getData().observe(viewLifecycleOwner, Observer<String> { it ->
            if (it != null) {
                tvCount.text = "${it.length}/255"
            } else {
                tvCount.text = "0/255"
            }
        })

        myQRViewModel = ViewModelProviders.of(activity!!).get(MyQRViewModel::class.java)
//        View model share data MyQRFragment
        vm = ViewModelProviders.of(activity!!).get(TextViewModel::class.java)
        vm!!.getValue().observe(activity!!, Observer {
            if (it == null) {
//                Toast.makeText(activity, "Null", Toast.LENGTH_SHORT).show()
            } else {
//                Log.e(TAG, it.content)
                binding.etWebsite.setText(it.content)
                binding.tvCount.text = it.content.length.toString()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        MainActivity.binding.bottomNavigation.visibility = View.GONE
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(com.hidero.qrsolar.R.id.content, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun myClick(view: View) {
        when (view) {
            btnHome -> {
                vm!!.setValue(null)
                if (activity!!.supportFragmentManager.backStackEntryCount > 0) {
                    activity!!.supportFragmentManager.popBackStack()
                } else {
                    replaceFragment(CreateFragment())
                }
            }
            btnSave -> {
////                Find screen size
//                var manager: WindowManager = activity!!.getSystemService(WINDOW_SERVICE) as WindowManager
//                var display = manager.defaultDisplay
//                var point = Point()
//                display.getSize(point)
//                var width = point.x
//                var height = point.y
//                var smallerDimension = if(width < height)  width else height
//                smallerDimension *= 3/4
////              Encode with a QR Code Image
//


                try {
                    if (TextUtils.isEmpty(binding.etWebsite.text.toString())) {
                        Toast.makeText(activity!!, "Bạn phải nhập đầy đủ thông tin!!!", Toast.LENGTH_SHORT).show()
                    } else {

                        try {


//                            QRGen
                            val bmp =
                                net.glxn.qrgen.android.QRCode.from(etWebsite.text.toString()).withSize(512, 512).bitmap()

                            var byte = convertToByteArray(bmp)
                            if (vm!!.getValue().value == null) {
                                myQRViewModel.insert(
                                    MyQR(
                                        0,
                                        binding.etWebsite.text.toString(),
                                        byte,
                                        "Website"
                                    )
                                )
                            } else {
                                var id = vm!!.getValue().value!!.id
                                Toast.makeText(activity!!, "$id", Toast.LENGTH_SHORT).show()
                                myQRViewModel.update(
                                    MyQR(
                                        id,
                                        binding.etWebsite.text.toString(),
                                        byte,
                                        "Website"
                                    )
                                )
                            }
                        } catch (e: WriterException) {
                            e.printStackTrace()
                        }

                        vm!!.setValue(null)
                        replaceFragment(MyQRFragment())
                    }

                } catch (e: Exception) {
                    Toast.makeText(activity, "Error " + e.message, Toast.LENGTH_SHORT).show()
//                    Log.e(TAG, e.message)
                }
            }
        }
    }

    /**
     * Convert bitmap to byte array using ByteBuffer.
     */
    fun convertToByteArray(bitmap: Bitmap): ByteArray {
//        //minimum number of bytes that can be used to store this bitmap's pixels
//        val size = bitmap.byteCount
//
//        //allocate new instances which will hold bitmap
//        val buffer = ByteBuffer.allocate(size)
//        val bytes = ByteArray(size)
//
//        //copy the bitmap's pixels into the specified buffer
//        bitmap.copyPixelsToBuffer(buffer)
//
//        //rewinds buffer (buffer position is set to zero and the mark is discarded)
//        buffer.rewind()
//
//        //transfer bytes from buffer into the given destination array
//        buffer.get(bytes)
//
//        //return bitmap's pixels
//        return bytes
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return byteArray
    }
}
