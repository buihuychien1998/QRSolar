package com.hidero.qrsolar.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.hidero.qrsolar.R
import com.hidero.qrsolar.activities.GallerySample
import com.hidero.qrsolar.entities.History
import com.hidero.qrsolar.viewmodels.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_vision.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class VisionFragment : Fragment() {

    var check: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vision, container, false)
    }

    private val REQUEST_CAMERA = 1
    private var mCameraPreview: SurfaceView? = null
    private var mBarcodeDetector: BarcodeDetector? = null
    private var mCameraSource: CameraSource? = null
    private var historyViewModel: HistoryViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA
            )
            return
        }

        mCameraPreview = getView()!!.findViewById(com.hidero.qrsolar.R.id.camera_view)

        mBarcodeDetector =
            BarcodeDetector.Builder(activity).setBarcodeFormats(Barcode.QR_CODE).build()

        mCameraSource = CameraSource.Builder(activity, mBarcodeDetector!!).setFacing(
            CameraSource.CAMERA_FACING_BACK
        )
            .setRequestedFps(35.0f)
            .setAutoFocusEnabled(true)
            .build()

        mCameraPreview!!.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    mCameraSource!!.start(mCameraPreview!!.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {

            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                mCameraSource!!.stop()
            }
        })

        mBarcodeDetector!!.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            @SuppressLint("MissingPermission")
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (!check) {
                    if (barcodes != null && barcodes.size() > 0) {
                        activity?.runOnUiThread {
                            //mBarcodeDetector!!.release()
                            check = true
                            try {
                                val sharedPref =
                                    PreferenceManager.getDefaultSharedPreferences(activity)
                                if (sharedPref.getBoolean("sound", false)) {
                                    val notification =
                                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                                    val r = RingtoneManager.getRingtone(context, notification)
                                    r.play()

                                }
                                // Vibrate for 500 milliseconds

                                if (sharedPref.getBoolean("vibrate", false)) {
                                    val v =
                                        activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        v?.vibrate(
                                            VibrationEffect.createOneShot(
                                                500,
                                                VibrationEffect.DEFAULT_AMPLITUDE
                                            )
                                        )
                                    } else {
                                        //deprecated in API 26
                                        v?.vibrate(500)
                                    }
                                } else {

                                }

                                historyViewModel = ViewModelProviders.of(activity!!)
                                    .get(HistoryViewModel::class.java)
                                val history: History
                                when {
                                    barcodes.valueAt(0).displayValue.contains("http://") || barcodes.valueAt(
                                        0
                                    ).displayValue.contains(
                                        "https://"
                                    ) -> history =
                                        History(
                                            0,
                                            barcodes.valueAt(0).displayValue,
                                            R.drawable.ic_web,
                                            SimpleDateFormat("dd/MM/yyyy   hh:mm").format(Calendar.getInstance().time)
                                        )
                                    barcodes.valueAt(0).displayValue.contains("@gmail.com") || barcodes.valueAt(
                                        0
                                    ).displayValue.contains(
                                        "@yahoo.com"
                                    ) || barcodes.valueAt(0).displayValue.contains("@solarapp.asia") -> history =
                                        History(
                                            0,
                                            barcodes.valueAt(0).displayValue,
                                            R.drawable.ic_email,
                                            SimpleDateFormat("dd/MM/yyyy   hh:mm").format(Calendar.getInstance().time)
                                        )
                                    else -> history =
                                        History(
                                            0,
                                            barcodes.valueAt(0).displayValue,
                                            R.drawable.ic_text,
                                            SimpleDateFormat("dd/MM/yyyy   hh:mm").format(Calendar.getInstance().time)
                                        )
                                }

                                if (sharedPref.getBoolean("vibrate", false)) {
                                    historyViewModel?.insert(history)
                                } else {

                                }

                                Toast.makeText(
                                    activity, barcodes.valueAt(0).displayValue,
                                    Toast.LENGTH_SHORT
                                ).show()
                                // If you would like to resume scanning, call this method below:
                                AlertDialog.Builder(activity)
                                    .setTitle("Bạn có muốn tiếp tục?")
                                    .setMessage("${barcodes.valueAt(0).displayValue}")
                                    .setPositiveButton(
                                        "Có"
                                    ) { dialog, which ->

                                        check = false
                                    }.setNegativeButton("Không") { _, _ ->
                                        check = false
                                    }
                                    .setIcon(android.R.drawable.btn_star).show()
                            } catch (e: Exception) {
                                e.printStackTrace()
                                e.message?.let { Log.e("AAAA", it) }
                            }

                        }
                    }
                }
            }
        })
        var flip = false
        btnFlipCamera.setOnClickListener {
            mCameraSource!!.stop()
            val builder = CameraSource.Builder(getActivity(), mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 480);
            if (flip) {
                builder.setFacing(CameraSource.CAMERA_FACING_BACK)
            } else {
                builder.setFacing(CameraSource.CAMERA_FACING_FRONT)
            }
            mCameraSource = builder.build()
            flip = !flip
            mCameraSource!!.start(mCameraPreview!!.holder)
        }

        btnGallery.setOnClickListener {
            startActivity(Intent(activity, GallerySample::class.java))
        }
        btnFlash.setOnClickListener {
            flashOnButton()
        }

    }

    private var camera: Camera? = null
    var flashmode = false


    private fun flashOnButton() {
        camera = mCameraSource?.let { getCamera(it) }
        if (camera != null) {
            try {
                val param = camera!!.getParameters()
                param.setFlashMode(if (!flashmode) Camera.Parameters.FLASH_MODE_TORCH
                else Camera.Parameters.FLASH_MODE_OFF)
                camera!!.setParameters(param)
                flashmode = !flashmode
                if (flashmode) {
                    Toast.makeText(activity, "Flash Switched ON", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Flash Switched Off", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun getCamera(cameraSource: CameraSource): Camera? {
        val declaredFields = CameraSource::class.java.declaredFields

        for (field in declaredFields) {
            if (field.type === Camera::class.java) {
                field.isAccessible = true
                try {
                    return field.get(cameraSource) as Camera
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

                break
            }
        }
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                mCameraSource!!.start(mCameraPreview!!.holder)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}
