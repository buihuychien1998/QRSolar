package com.hidero.qrsolar.fragments


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hidero.qrsolar.interfaces.GetImagePathFromGallery
import kotlinx.android.synthetic.main.fragment_preview.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PreviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.hidero.qrsolar.R.layout.fragment_preview, container, false)
    }

    override fun onResume() {
        super.onResume()
        Log.e("ABCD", "ok : $result")
    }
    var isCircleCrop = true
    var result: Bitmap? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var pathImage = activity as GetImagePathFromGallery
        var file: File = File(pathImage!!.getPath(true))
        if (file.exists()) {
            Glide.with(this).load(file).into(ivPhoto)


            btnDone.setOnClickListener {
                saveBitmapToFile(MyAsyncTask().execute(file).get())
                Toast.makeText(activity, "Save successful", Toast.LENGTH_SHORT).show()
            }
            btnCircle.setOnClickListener {
                isCircleCrop = true
                Glide.with(this).load(file).circleCrop().into(ivPhoto)
            }
            btnSquare.setOnClickListener {
                isCircleCrop = false
                Glide.with(this).load(file).centerCrop().into(ivPhoto)
            }

        } else {
            ivPhoto.setImageResource(com.hidero.qrsolar.R.drawable.girl)
        }
    }

    inner class MyAsyncTask() : AsyncTask<File, Void, Bitmap>() {
        override fun doInBackground(vararg params: File?): Bitmap {
            if(isCircleCrop){
                return Glide.with(context!!).asBitmap()
                    .load(params[0])
                    .apply(
                        RequestOptions.circleCropTransform()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    ).submit()
                    .get()
            }else{
                return Glide.with(context!!).asBitmap()
                    .load(params[0])
                    .apply(
                        RequestOptions.centerCropTransform()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                    ).submit()
                    .get()
            }

        }

    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)

        return bitmap
    }

    fun saveBitmapToFile(bitmap: Bitmap?) {
        // Assume block needs to be inside a Try/Catch block.
        val path = Environment.getExternalStorageDirectory().toString()
        var fOut: OutputStream? = null
        val date = Date(0)
        val sdf = SimpleDateFormat("yyyyMMddHHmmss")
        var filename = sdf.format(date)

        val file = File(
            path,
            "$filename.jpg"
        ) // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        fOut = FileOutputStream(file) as OutputStream?

        bitmap!!.compress(
            Bitmap.CompressFormat.JPEG,
            85,
            fOut
        ) // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        fOut!!.flush() // Not really required
        fOut!!.close() // do not forget to close the stream

        MediaStore.Images.Media.insertImage(
            activity!!.contentResolver,
            file.getAbsolutePath(),
            file.getName(),
            file.getName()
        )
    }


    private fun saveBitmap(bitmap: Bitmap?, path: String) {
        if (bitmap != null) {
            try {
                var outputStream: FileOutputStream? = null
                try {
                    outputStream =
                        FileOutputStream(path) //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        outputStream
                    ) // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream!!.close()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}
