package com.hidero.qrsolar.fragments


import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Camera
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.support.v7.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.Result
import com.hidero.qrsolar.activities.GallerySample
import com.hidero.qrsolar.activities.MainActivity
import com.hidero.qrsolar.entities.History
import com.hidero.qrsolar.viewmodels.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_blank.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.*


class BlankFragment : Fragment(), ZXingScannerView.ResultHandler {
    private var mScannerView: ZXingScannerView? = null
    private var mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK
    private var historyViewModel: HistoryViewModel? = null

    override fun handleResult(p0: Result?) {
        try {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
            if (sharedPref.getBoolean("sound", false)) {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(context, notification)
                r.play()
// Vibrate for 500 milliseconds

            }
            if (sharedPref.getBoolean("vibrate", false)) {
                val v = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v!!.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    //deprecated in API 26
                    v!!.vibrate(500)
                }
            } else {

            }
            Toast.makeText(context, "Scan Completed \n" + p0!!.text + "", Toast.LENGTH_LONG)
                .show()

            historyViewModel = ViewModelProviders.of(activity!!).get(HistoryViewModel::class.java)
            var history: History
            if (p0.text.contains("http://") || p0.text.contains("http://")) {
                history =
                    History(0, p0.text, com.hidero.qrsolar.R.drawable.ic_web, Calendar.getInstance().time.toString())
            } else {
                history =
                    History(0, p0.text, com.hidero.qrsolar.R.drawable.ic_text, Calendar.getInstance().time.toString())
            }

            if (sharedPref.getBoolean("vibrate", false)) {
                historyViewModel!!.insert(history)
            } else {

            }
            // If you would like to resume scanning, call this method below:
            var dialog: AlertDialog? = AlertDialog.Builder(activity).setTitle("Bạn có muốn tiếp tục?")
                .setPositiveButton("Có", DialogInterface.OnClickListener { dialog, which ->
                    mScannerView!!.resumeCameraPreview(this)
                }).setNegativeButton("Không", null).setIcon(android.R.drawable.btn_star).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        MainActivity.binding.bottomNavigation.visibility = View.VISIBLE
        super.onResume()
        mScannerView!!.setResultHandler(this)
        mScannerView!!.startCamera(mCameraId)
        //to set flash
        mScannerView!!.flash = false
        //to set autoFocus
        mScannerView!!.setAutoFocus(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mScannerView = ZXingScannerView(activity)
        content_frame.addView(mScannerView)
        var kt = false
        btnFlash.setOnClickListener {
            kt = !kt
            mScannerView!!.flash = kt
        }
        btnGallery.setOnClickListener {
            startActivity(Intent(activity!!, GallerySample::class.java))
        }
        btnFlipCamera.setOnClickListener {
            mScannerView!!.stopCamera()
            val id = (if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
                Camera.CameraInfo.CAMERA_FACING_FRONT
            else
                Camera.CameraInfo.CAMERA_FACING_BACK)
//            if (mCameraId == -1) {
//                mCameraId = 0
//            } else {
//                mCameraId = -1
//            }

            mScannerView!!.startCamera(id)
        }
    }

    override fun onStop() {
        super.onStop()
        mScannerView!!.stopCamera();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.hidero.qrsolar.R.layout.fragment_blank, container, false)
    }


}
