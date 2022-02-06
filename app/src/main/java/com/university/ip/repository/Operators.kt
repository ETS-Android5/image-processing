package com.university.ip.repository

import android.graphics.Bitmap
import okhttp3.internal.Util
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.core.Core.BORDER_DEFAULT
import org.opencv.core.Core.FLAGS_NONE
import org.opencv.imgproc.Imgproc




class Operators {
    fun increaseBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, 1.0, value.toDouble())
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun increaseContrast(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, value.toDouble(), 1.0)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun convertToGrayscale(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_GRAY2RGB, 4)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun convertToSepia(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val kernel = Mat(4, 4, CvType.CV_32F)
        kernel.put(
                0, 0,
                0.393, 0.769, 0.189, 0.0,
                0.349, 0.686, 0.168, 0.0,
                0.272, 0.534, 0.131, 0.0,
                0.0, 0.0, 0.0, 1.0
        )

        val destination = src
        Core.transform(src, destination, kernel);
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(destination, result)
        return result
    }

    fun adaptiveThreshold(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)
        Imgproc.adaptiveThreshold(src, src, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 40.0)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun zoom(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val factor: Double = value / 255.0 + 1
        val size = src.size()
        size.height = size.height * factor
        size.width = size.width * factor
        Imgproc.resize(src, src, size, 0.0, 0.0, Imgproc.INTER_LINEAR)
        val centerRegion = Rect(0, 0, bitmap.width, bitmap.height)
        val resultMat = src.submat(centerRegion)
        val result = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(resultMat, result)
        return result
    }

    fun rotateClockwise90(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Core.rotate(src, src, Core.ROTATE_90_CLOCKWISE)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun rotateCounterClockwise90(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Core.rotate(src, src, Core.ROTATE_90_COUNTERCLOCKWISE)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun flip(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Core.flip(src, src, 0)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun gaussianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val ksize = Size(value.toDouble()*2 + 1, value.toDouble()*2 + 1)
        Imgproc.GaussianBlur(src, src, ksize, 0.0, 0.0, Core.BORDER_DEFAULT)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun medianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        if(value % 2 == 0){
            Imgproc.medianBlur(src, src, value + 1)
        }
        else {
            Imgproc.medianBlur(src, src, value)
        }

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun bilateralFilter(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var dst = src.clone()
        Imgproc.cvtColor(dst,src,Imgproc.COLOR_BGRA2BGR)
        dst = src.clone()
        Imgproc.bilateralFilter(src, dst,5, 75.0+value.toDouble(), 75.0+value.toDouble())
        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result
    }

    fun laplacian(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.Laplacian(src, src, CvType.CV_8U, 3, value.toDouble(), 0.0, Core.BORDER_DEFAULT)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun unsharpMask(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst = Mat(src.rows(), src.cols(), src.type())
        Imgproc.GaussianBlur(src, dst, Size(0.0, 0.0), 10.0)
        Core.addWeighted(src, 1.5, dst, -0.5, 0.0, dst)
        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result
    }
}