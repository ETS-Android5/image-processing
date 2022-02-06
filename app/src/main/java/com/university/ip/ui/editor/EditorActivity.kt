package com.university.ip.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.university.ip.R
import com.university.ip.ui.main.MainActivity
import com.university.ip.util.files.FileSaver.Companion.IMAGE_MIME_TYPE
import com.university.ip.util.files.FileSaverLegacy
import org.opencv.android.OpenCVLoader

class EditorActivity : AppCompatActivity(), EditorContract.View, View.OnClickListener,
    FiltersAdapter.ItemClickListener, SeekBar.OnSeekBarChangeListener {

    override fun appContext(): Context = applicationContext
    private val TAG = "EditorActivity"

    private lateinit var backButton: ImageView
    private lateinit var saveButton: TextView
    private lateinit var imageView: ImageView
    private lateinit var filterList: RecyclerView
    private lateinit var seekBar: SeekBar

    private lateinit var adapter: FiltersAdapter

    private lateinit var fileSaver: FileSaverLegacy
    private lateinit var bitmap: Bitmap
    private lateinit var selectedFilter: String
    private lateinit var presenter: EditorPresenter

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        //list initialization
        val layoutManager = LinearLayoutManager(appContext(), LinearLayoutManager.HORIZONTAL, false)
        filterList = findViewById(R.id.filters_list)
        filterList.layoutManager = layoutManager
        adapter = FiltersAdapter(appContext(), this)
        adapter.setMediaList(FILTERS_ARRAY);
        filterList.adapter = adapter

        seekBar = findViewById(R.id.seek_bar_editor)

        backButton = findViewById(R.id.back_editor)
        backButton.setOnClickListener(this)

        imageView = findViewById(R.id.image_edited)

        fileSaver = FileSaverLegacy(appContext())
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener(this)
        presenter = EditorPresenter()
        presenter.bindView(this)
        //image load
        loadImage()
        openCvInit()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun openCvInit() {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV not loaded");
        } else {
            Log.e(TAG, "OpenCV loaded");
        }
    }

    private fun loadImage() {
        val data = intent.getBundleExtra(INTENT_EXTRAS)
        val requestCode = intent.getIntExtra(REQUEST_CODE, 2)
        val resultCode = intent.getIntExtra(RESULT_CODE, Activity.RESULT_CANCELED)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = data.get("data") as Bitmap
                    bitmap = selectedImage
                    imageView.setImageBitmap(selectedImage)
                }
                1 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = intent.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(
                        selectedImage,
                        filePathColumn, null, null, null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        bitmap = BitmapFactory.decodeFile(picturePath)
                        imageView.setImageBitmap(bitmap)
                        cursor.close()
                    }

                }
            }
        }
    }

    companion object {
        const val INTENT_EXTRAS: String = "INTENT_EXTRAS"
        const val REQUEST_CODE: String = "REQUEST_CODE"
        const val RESULT_CODE: String = "RESULT_CODE"
        val FILTERS_ARRAY: List<String> = listOf("Brightness", "Contrast", "Zoom", "Grayscale", "Sepia", "Adaptive Threshold", "Rotate 90+", "Rotate -90", "Flip", "Gaussian Blur", "Median Blur", "Bilateral Filter", "Laplacian", "Unsharp Mask")
        val FILTERS_SLIDER_ARRAY: List<String> = listOf("Brightness", "Contrast", "Zoom", "", "", "Adaptive Threshold", "", "", "", "Gaussian Blur", "Median Blur", "Bilateral Filter", "Laplacian", "")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_editor -> {
                finish()
            }
            R.id.save_button -> {
                val uri = fileSaver.getFileUri(IMAGE_MIME_TYPE) ?: return
                appContext().contentResolver.openOutputStream(uri)?.use { stream ->
                    imageView.invalidate()
                    imageView.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
                startActivity(Intent(appContext(), MainActivity::class.java))
            }
        }
    }

    override fun onItemClick(filter: String) {
        selectedFilter = filter

        when (FILTERS_ARRAY.indexOf(selectedFilter)) {
            0, 1, 2, 5, 9, 10, 11, 12-> {
                seekBar.visibility = View.VISIBLE
                seekBar.setOnSeekBarChangeListener(this)
            }
            3 -> {
                seekBar.visibility = View.GONE
                presenter.grayscale(bitmap)
                return
            }
            4 -> {
                seekBar.visibility = View.GONE
                presenter.sepia(bitmap)
                return
            }
            6 -> {
                seekBar.visibility = View.GONE
                presenter.rotateClockwise90(bitmap)
                imageView.invalidate()
                bitmap = imageView.drawable.toBitmap()
                return
            }
            7 -> {
                seekBar.visibility = View.GONE
                presenter.rotateCounterClockwise90(bitmap)
                imageView.invalidate()
                bitmap = imageView.drawable.toBitmap()
                return
            }
            8 -> {
                seekBar.visibility = View.GONE
                presenter.flip(bitmap)
                imageView.invalidate()
                bitmap = imageView.drawable.toBitmap()
                return
            }
            13 -> {
                seekBar.visibility = View.GONE
                presenter.unsharpMask(bitmap)
                return
            }
            else -> return
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter)) {
            0 -> {
                presenter.brightness(bitmap, progress)
                return
            }
            1 -> {
                presenter.contrast(bitmap, progress)
                return
            }
            2 -> {
                presenter.zoom(bitmap, progress)
                return
            }
            5 -> {
                presenter.adaptiveThreshold(bitmap, progress)
                return
            }
            9 -> {
                presenter.gaussianBlur(bitmap, progress)
                return
            }
           10 -> {
                presenter.medianBlur(bitmap, progress)
                return
            }
            11 -> {
                presenter.bilateralFilter(bitmap, progress)
                return
            }
            12 -> {
                presenter.laplacian(bitmap, progress)
                return
            }
            else -> return
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    override fun setBitmap(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}