package com.ki.io

import com.ki.models.Payment
import java.io.FileReader

private val COMMON_FIELD_NAMES = listOf("customer_id", "date", "amount")

internal abstract class PaymentsCSVFileReader(
    fileReader: FileReader,
    additionalFieldNames: List<String>,
) : CSVFileReader(fileReader) {
    private val FIELD_NAMES = COMMON_FIELD_NAMES + additionalFieldNames
    private val _payments = mutableListOf<Payment>()

    override fun onHeaderRow(fieldNames: List<String>) {
        if (fieldNames != FIELD_NAMES) {
            throw InvalidDataException("Unexpected field names in header row. Expected $FIELD_NAMES but found $fieldNames.")
        }
    }

    override fun onBodyRow(fieldValues: List<String>) {
        _payments.add(createPayment(fieldValues))
    }

    abstract fun createPayment(fieldValues: List<String>): Payment

    val payments: List<Payment>
        get() = _payments.toList()
}
