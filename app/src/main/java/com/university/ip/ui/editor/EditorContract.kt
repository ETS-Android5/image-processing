package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.ui.base.BaseContract

interface EditorContract {

    interface View : BaseContract.View {
        //view functions for each change of activity
        fun setBitmap(bitmap: Bitmap)
    }

    interface Presenter {
        //functions that are going to use our library
        fun brightness(bitmap: Bitmap, value: Int)

        fun contrast(bitmap: Bitmap, value: Int)

        fun grayscale(bitmap: Bitmap)

        fun sepia(bitmap: Bitmap)

        fun adaptiveThreshold(bitmap: Bitmap, value: Int)

        fun zoom(bitmap: Bitmap, value: Int)

        fun rotateClockwise90(bitmap: Bitmap)

        fun rotateCounterClockwise90(bitmap: Bitmap)

        fun flip(bitmap: Bitmap)

        fun gaussianBlur(bitmap: Bitmap, value: Int)

        fun medianBlur(bitmap: Bitmap, value: Int)

        fun bilateralFilter(bitmap: Bitmap, value: Int)

        fun laplacian(bitmap: Bitmap, value: Int)

        fun unsharpMask(bitmap: Bitmap)
    }
}