package com.ki.services

import com.ki.io.BankPaymentsCSVFileReader
import com.ki.io.CardPaymentsCSVFileReader
import com.ki.io.PaymentsCSVFileReader
import com.ki.models.Payment
import java.io.FileReader

/**
 * Service class offering methods for loading and processing payment records.
 *
 * **IMPORTANT**: The API offered by this class is relied upon by other services.
 * The module it's included in is installed on the platform as a dependency for those other services.
 */
class PaymentProcessor {
    /**
     * Read payment records from the supplied CSV file.
     *
     * @param csvPath Path to the payments CSV file.
     * @param source The source of the payments, currently only `'card'` or `'bank'` are supported.
     * @return Payment records, in the order read from the file.
     */
    fun getPayments(
        csvPath: String,
        source: String,
    ): Array<Payment> {
        val reader = createReader(csvPath, source)
        reader.readAll()
        return reader.payments.toTypedArray()
    }

    private fun createReader(
        csvPath: String,
        source: String,
    ): PaymentsCSVFileReader {
        val fileReader = FileReader(csvPath)

        when (source) {
            "card" -> return CardPaymentsCSVFileReader(fileReader)
            "bank" -> return BankPaymentsCSVFileReader(fileReader)
        }

        throw IllegalArgumentException("Only card or bank payments are supported. You supplied \"${source}\".")
    }

    /**
     * Filter payment records, removing those which are invalid for some reason (e.g. declined).
     *
     * @param payments The payment records to filter.
     * @return Valid payment records. This array could contain from zero to [payments].`size` records.
     */
    fun verifyPayments(payments: Array<Payment>): Array<Payment> {
        val filtered = ArrayList<Payment>()
        for (payment in payments) {
            if (payment.isSuccessful) {
                filtered.add(payment)
            }
        }
        return filtered.toArray(arrayOf())
    }
}
