package com.hidero.qrsolar.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.hidero.qrsolar.viewmodels.PhotoEditorViewModel
import com.hidero.qrsolar.R
import com.hidero.qrsolar.filters.FilterListener
import com.hidero.qrsolar.filters.FilterViewAdapter
import com.hidero.qrsolar.interfaces.GetImagePathFromGallery
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.android.synthetic.main.photo_editor_fragment.*
import java.io.File


class PhotoEditorFragment : Fragment(), FilterListener {
    override fun onFilterSelected(photoFilter: PhotoFilter?) {

        mPhotoEditor!!.setFilterEffect(photoFilter)
    }
    private var pathImage: GetImagePathFromGallery? = null
    var mPhotoEditor: PhotoEditor? = null
    var mPhotoEditorView: PhotoEditorView? = null
    private val mFilterViewAdapter = FilterViewAdapter(this)

    companion object {
        fun newInstance() = PhotoEditorFragment()
    }

    private lateinit var viewModel: PhotoEditorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photo_editor_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPhotoEditorView = getView()!!.findViewById(R.id.ivPhoto)
        super.onViewCreated(view, savedInstanceState)
        mPhotoEditor = PhotoEditor.Builder(activity, mPhotoEditorView)
            .setPinchTextScalable(true) // set flag to make text scalable when pinch
            //.setDefaultTextTypeface(mTextRobotoTf)
            //.setDefaultEmojiTypeface(mEmojiTypeFace)
            .build() // build photo editor sdk
//        mPhotoEditor!!.setOnPhotoEditorListener(this)
        pathImage = activity as GetImagePathFromGallery
        var file: File = File(pathImage!!.getPath(true))
        if (file.exists()){
            Glide.with(this).load(file).circleCrop().into(ivGallery)

        }else{
            ivGallery.setImageResource(R.drawable.girl)
        }

        val llmFilters = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvFilter.setLayoutManager(llmFilters)
        rvFilter.setAdapter(mFilterViewAdapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PhotoEditorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
