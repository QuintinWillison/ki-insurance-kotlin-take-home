package com.ki.services

import com.ki.models.Payment
import com.opencsv.CSVReaderBuilder
import java.io.FileReader
import java.io.IOException

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
     * @param source The source of the payments, currently only `'card'` is supported.
     * @return Payment records, in the order read from the file.
     */
    fun getPayments(
        csvPath: String,
        source: String,
    ): Array<Payment> {
        if (source != "card") {
            throw IllegalArgumentException("Only card payments are supported. You supplied \"${source}\".")
        }

        val payments = ArrayList<Payment>()
        try {
            val file = FileReader(csvPath)
            val reader = CSVReaderBuilder(file).withSkipLines(1).build()
            while (true) {
                val line = reader.readNext() ?: break
                val payment = Payment(line)
                payments.add(payment)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return payments.toArray(arrayOf())
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
