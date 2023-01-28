package com.ki.io

import com.ki.Fixture
import com.ki.models.Payment
import org.junit.Test
import java.io.FileReader
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PaymentsCSVFileReaderTest {
    private val ADDITIONAL_CARD_HEADERS = listOf("card_id", "card_status")
    private val ADDITIONAL_BANK_HEADERS = listOf("bank_account_id")

    @Test(expected = InvalidDataException::class)
    fun invalidHeaderWrongSourceWhenExpectingBank() {
        val reader = createAndExerciseReader("card_payments_mixed", ADDITIONAL_BANK_HEADERS)
    }

    @Test(expected = InvalidDataException::class)
    fun invalidHeaderWrongSourceWhenExpectingCard() {
        val reader = createAndExerciseReader("bank_payments_mixed", ADDITIONAL_CARD_HEADERS)
    }

    @Test
    fun cardPaymentsEmpty() {
        val reader = createAndExerciseReader("card_payments_empty", ADDITIONAL_CARD_HEADERS)
        assertEquals(0, reader.payments.size)
    }

    @Test
    fun cardPaymentsMixed() {
        val reader = createAndExerciseReader("card_payments_mixed", ADDITIONAL_CARD_HEADERS)
        assertEquals(3, reader.payments.size)

        // not testing all fields, just a sampling
        val payments = reader.payments
        assertEquals(123, payments[0].customerId)
        assertEquals("processed", payments[0].card!!.status)
        assertEquals(45, payments[1].card!!.cardId)
        assertEquals("declined", payments[1].card!!.status)
    }

    @Test
    fun bankPaymentsEmpty() {
        val reader = createAndExerciseReader("bank_payments_empty", ADDITIONAL_BANK_HEADERS)
        assertEquals(0, reader.payments.size)
    }

    @Test
    fun bankPaymentsMixed() {
        val reader = createAndExerciseReader("bank_payments_mixed", ADDITIONAL_BANK_HEADERS)
        assertEquals(4, reader.payments.size)

        // not testing all fields, just a sampling
        val payments = reader.payments
        assertEquals(345, payments[1].customerId)
        assertNull(payments[0].card)
    }

    private fun createAndExerciseReader(
        csvFileName: String,
        additionalFieldNames: List<String>,
    ): PaymentsCSVFileReader {
        val reader = DumbPaymentsCSVFileReader(
            Fixture.createCSVFileReader(csvFileName),
            additionalFieldNames,
        )
        reader.readAll()
        return reader
    }

    private class DumbPaymentsCSVFileReader(
        fileReader: FileReader,
        additionalFieldNames: List<String>,
    ) : PaymentsCSVFileReader(fileReader, additionalFieldNames) {
        override fun createPayment(fieldValues: List<String>): Payment {
            return Payment(fieldValues.toTypedArray())
        }
    }
}
