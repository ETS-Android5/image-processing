package com.university.ip.ui.editor

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.university.ip.repository.Operators
import com.university.ip.ui.base.BasePresenter

class EditorPresenter : BasePresenter<EditorContract.View>(), EditorContract.Presenter {

    private val operators = Operators()

    override fun brightness(bitmap: Bitmap, value: Int) {
        val result = operators.increaseBrightness(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun contrast(bitmap: Bitmap, value: Int) {
        val result = operators.increaseContrast(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun sepia(bitmap: Bitmap) {
        val result = operators.convertToSepia(bitmap)
        getView()?.setBitmap(result)
    }

    override fun grayscale(bitmap: Bitmap) {
        val result = operators.convertToGrayscale(bitmap)
        getView()?.setBitmap(result)
    }

    override fun adaptiveThreshold(bitmap: Bitmap, value: Int) {
        val result = operators.adaptiveThreshold(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun zoom(bitmap: Bitmap, value: Int) {
        val result = operators.zoom(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun rotateClockwise90(bitmap: Bitmap) {
        val result = operators.rotateClockwise90(bitmap)
        getView()?.setBitmap(result)
    }

    override fun rotateCounterClockwise90(bitmap: Bitmap) {
        val result = operators.rotateCounterClockwise90(bitmap)
        getView()?.setBitmap(result)
    }

    override fun flip(bitmap: Bitmap) {
        val result = operators.flip(bitmap)
        getView()?.setBitmap(result)
    }

    override fun gaussianBlur(bitmap: Bitmap, value: Int) {
        val result = operators.gaussianBlur(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun medianBlur(bitmap: Bitmap, value: Int) {
        val result = operators.medianBlur(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun bilateralFilter(bitmap: Bitmap, value: Int) {
        val result = operators.bilateralFilter(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun laplacian(bitmap: Bitmap, value: Int) {
        val result = operators.laplacian(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun unsharpMask(bitmap: Bitmap) {
        val result = operators.unsharpMask(bitmap)
        getView()?.setBitmap(result)
    }
}