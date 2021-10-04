package com.hidero.qrsolar.fragments


import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.hidero.qrsolar.R
import com.hidero.qrsolar.activities.GallerySample
import com.hidero.qrsolar.activities.MainActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.sql.Timestamp

class ScanFragment : Fragment(), SurfaceHolder.Callback,
    View.OnClickListener {
    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var flipCamera: Button? = null
    private var flashCameraButton: Button? = null
    private var captureImage: Button? = null
    private var cameraId: Int = 0
    private var flashmode = false
    private var rotation: Int = 0
    lateinit var cameraSource: CameraSource
    lateinit var barcodeDetector: BarcodeDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraId = Camera.CameraInfo.CAMERA_FACING_BACK
        flipCamera = getView()!!.findViewById(R.id.flipCamera)
        flashCameraButton = getView()!!.findViewById(R.id.flash)
        captureImage = getView()!!.findViewById(R.id.captureImage)
        surfaceView = getView()!!.findViewById(R.id.surfaceView)
        surfaceHolder = surfaceView!!.holder


        surfaceHolder!!.addCallback(this)
        flipCamera!!.setOnClickListener(this)


        captureImage!!.setOnClickListener(this)
        flashCameraButton!!.setOnClickListener(this)
        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (Camera.getNumberOfCameras() > 1) {
            flipCamera!!.visibility = View.VISIBLE
        }
        if (!activity!!.baseContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH
            )
        ) {
            flashCameraButton!!.visibility = View.GONE
        }

        barcodeDetector =
            BarcodeDetector.Builder(activity)
                .setBarcodeFormats(Barcode.DATA_MATRIX or Barcode.QR_CODE)
                .build()
        if (!barcodeDetector.isOperational) {
            Toast.makeText(activity!!, "Loading", Toast.LENGTH_LONG).show()
        }
        cameraSource = CameraSource.Builder(activity, barcodeDetector)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(640, 480)
            .build()

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode?> {
            override fun release() {
            }

            override fun receiveDetections(p0: Detector.Detections<Barcode?>?) {
                val barcodes = p0!!.detectedItems
                if (barcodes.size() != 0) {
//                    var mHandler = Handler()
//                    var runable = Runnable {
                    Toast.makeText(activity, barcodes.valueAt(0)?.displayValue, Toast.LENGTH_SHORT).show()
//                    }
//                    mHandler.post(runable)
                } else {
                    Toast.makeText(activity, "Could not set up the detector! ?", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        MainActivity.binding.bottomNavigation.visibility = View.VISIBLE
//            MainActivity.binding.mainLayout.background = activity!!.getDrawable(R.color.colorbg)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!openCamera(Camera.CameraInfo.CAMERA_FACING_BACK)) {
            alertCameraDialog()
            try {
                if ((ContextCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED)
                ) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        requestPermissions(

                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            ),
                            1
                        )
                    }
                }
                cameraSource.start(surfaceView!!.holder)
            } catch (ie: IOException) {
                ie.message?.let { Log.e("CAMERA SOURCE", it) };
            }
        }

    }

    private fun openCamera(id: Int): Boolean {
        var result = false
        cameraId = id
        releaseCamera()
        try {
            camera = Camera.open(cameraId)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (camera != null) {
            try {
                setUpCamera(camera)
                camera!!.setErrorCallback { error, camera -> }
                camera!!.setPreviewDisplay(surfaceHolder)
                camera!!.startPreview()
                result = true
            } catch (e: IOException) {
                e.printStackTrace()
                result = false
                releaseCamera()
            }

        }
        return result
    }

    private fun setUpCamera(c: Camera?) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        rotation = activity!!.getWindowManager().getDefaultDisplay().getRotation()
        var degree = 0
        when (rotation) {
            Surface.ROTATION_0 -> degree = 0
            Surface.ROTATION_90 -> degree = 90
            Surface.ROTATION_180 -> degree = 180
            Surface.ROTATION_270 -> degree = 270

            else -> {
            }
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330
            rotation = (360 - rotation) % 360
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360
        }
        c!!.setDisplayOrientation(rotation)
        val params = c!!.parameters

        showFlashButton(params)

        val focusModes = params.supportedFlashModes
        if (focusModes != null) {
            if (focusModes!!
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
            ) {
                params.flashMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
            }
        }

        params.setRotation(rotation)
    }

    private fun showFlashButton(params: Camera.Parameters) {
        val showFlash = (activity!!.getPackageManager().hasSystemFeature(
            PackageManager.FEATURE_CAMERA_FLASH
        ) && params.flashMode != null
                && params.supportedFlashModes != null
                && params.supportedFocusModes.size > 1)

        flashCameraButton!!.visibility = if (showFlash)
            View.VISIBLE
        else
            View.INVISIBLE

    }

    private fun releaseCamera() {
        try {
            if (camera != null) {
                camera!!.setPreviewCallback(null)
                camera!!.setErrorCallback(null)
                camera!!.stopPreview()
                camera!!.release()
                camera = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("error", e.toString())
            camera = null
        }

    }

    override fun surfaceChanged(
        holder: SurfaceHolder, format: Int, width: Int,
        height: Int
    ) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        cameraSource.stop()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.flash -> flashOnButton()
            R.id.flipCamera -> flipCamera()
            R.id.captureImage -> {
//                takeImage()
                startActivity(Intent(activity, GallerySample::class.java))
            }

            else -> {
            }
        }
    }

    private fun takeImage() {
        camera!!.takePicture(null, null, object : Camera.PictureCallback {

            private var imageFile: File? = null

            override fun onPictureTaken(data: ByteArray, camera: Camera) {
                try {
                    // convert byte array into bitmap
                    var loadedImage: Bitmap? = null
                    var rotatedBitmap: Bitmap? = null
                    loadedImage = BitmapFactory.decodeByteArray(
                        data, 0,
                        data.size
                    )

                    // rotate Image
                    val rotateMatrix = Matrix()
                    rotateMatrix.postRotate(rotation.toFloat())
                    rotatedBitmap = Bitmap.createBitmap(
                        loadedImage!!, 0, 0,
                        loadedImage!!.width, loadedImage!!.height,
                        rotateMatrix, false
                    )
                    val state = Environment.getExternalStorageState()
                    var folder: File? = null
                    if (state.contains(Environment.MEDIA_MOUNTED)) {
                        folder = File(
                            (Environment
                                .getExternalStorageDirectory()).toString() + "/Demo"
                        )
                    } else {
                        folder = File(
                            ((Environment
                                .getExternalStorageDirectory()).toString() + "/Demo")
                        )
                    }

                    var success = true
                    if (!folder!!.exists()) {
                        success = folder!!.mkdirs()
                    }
                    if (success) {
                        val date = java.util.Date()
                        imageFile = File(
                            (folder!!.absolutePath
                                    + File.separator
                                    + Timestamp(date.time).toString()
                                    + "Image.jpg")
                        )

                        imageFile!!.createNewFile()
                    } else {
                        Toast.makeText(
                            activity!!.getBaseContext(), "Image Not saved",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val ostream = ByteArrayOutputStream()

                    // save image into gallery
                    rotatedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, ostream)

                    val fout = FileOutputStream(imageFile)
                    fout.write(ostream.toByteArray())
                    fout.close()
                    val values = ContentValues()

                    values.put(
                        MediaStore.Images.Media.DATE_TAKEN,
                        System.currentTimeMillis()
                    )
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    values.put(
                        MediaStore.MediaColumns.DATA,
                        imageFile!!.absolutePath
                    )

                    activity!!.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun flipCamera() {
        val id = (if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
            Camera.CameraInfo.CAMERA_FACING_FRONT
        else
            Camera.CameraInfo.CAMERA_FACING_BACK)
        if (!openCamera(id)) {
            alertCameraDialog()
        }
    }

    private fun alertCameraDialog() {
        val dialog = createAlert(
            activity!!,
            "Camera info", "error to open camera"
        )
        dialog.setNegativeButton("OK", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        dialog.show()
    }

    private fun createAlert(context: Context, title: String?, message: String): AlertDialog.Builder {

        val dialog = AlertDialog.Builder(
            ContextThemeWrapper(
                context,
                android.R.style.Theme_Holo_Light_Dialog
            )
        )
        dialog.setIcon(android.R.drawable.btn_star)
        if (title != null)
            dialog.setTitle(title)
        else
            dialog.setTitle("Information")
        dialog.setMessage(message)
        dialog.setCancelable(false)
        return dialog

    }

    private fun flashOnButton() {
        if (camera != null) {
            try {
                val param = camera!!.parameters
                param.flashMode = if (!flashmode)
                    Camera.Parameters.FLASH_MODE_TORCH
                else
                    Camera.Parameters.FLASH_MODE_OFF
                camera!!.parameters = param
                flashmode = !flashmode
            } catch (e: Exception) {
                // TODO: handle exception
            }

        }
    }


}
