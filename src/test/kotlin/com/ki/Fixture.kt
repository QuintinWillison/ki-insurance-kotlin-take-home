package com.ki

import java.io.File
import java.io.FileReader
import java.io.IOException

object Fixture {
    fun getPath(filename: String): String {
        var selfPath: String? = null
        try {
            selfPath = File(".").canonicalPath
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "$selfPath/src/test/fixtures/$filename"
    }

    fun createCSVFileReader(filename: String): FileReader {
        return FileReader(getPath("$filename.csv"))
    }
}
