package com.ki.io

import com.opencsv.CSVReaderBuilder
import java.io.FileReader

internal abstract class CSVFileReader(private val fileReader: FileReader) {
    abstract fun onHeaderRow(fieldNames: List<String>)
    abstract fun onBodyRow(fieldValues: List<String>)

    fun readAll() {
        val reader = CSVReaderBuilder(fileReader).build()

        var hadHeaderRow = false
        var isFinished = false
        do {
            val fieldArray = reader.readNext()
            if (null == fieldArray) {
                isFinished = true
            } else {
                val fieldValues = fieldArray.toList()
                if (hadHeaderRow) {
                    onBodyRow(fieldValues)
                } else {
                    hadHeaderRow = true
                    onHeaderRow(fieldValues)
                }
            }
        } while (!isFinished)
    }
}
