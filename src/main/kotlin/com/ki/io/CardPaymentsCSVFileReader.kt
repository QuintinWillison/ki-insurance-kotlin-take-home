package com.ki.io

import com.ki.models.Payment
import java.io.FileReader

private val ADDITIONAL_HEADERS = listOf("card_id", "card_status")

internal class CardPaymentsCSVFileReader(
    fileReader: FileReader,
) : PaymentsCSVFileReader(fileReader, ADDITIONAL_HEADERS) {
    override fun createPayment(fieldValues: List<String>): Payment {
        return Payment(fieldValues.toTypedArray())
    }
}
