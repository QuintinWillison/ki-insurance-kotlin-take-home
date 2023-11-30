package com.ki.io

import com.ki.Fixture
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BankPaymentsCSVFileReaderTest {
    @Test
    fun empty() {
        val reader = createAndExerciseReader("bank_payments_empty")
        assertEquals(0, reader.payments.size)
    }

    @Test
    fun mixed() {
        val reader = createAndExerciseReader("bank_payments_mixed")
        assertEquals(4, reader.payments.size)

        // not testing all fields, just a sampling
        val payments = reader.payments
        assertEquals(345, payments[1].customerId)
        assertNull(payments[0].card)
    }

    @Test(expected = InvalidDataException::class)
    fun cardEmpty() {
        createAndExerciseReader("card_payments_empty")
    }

    @Test(expected = InvalidDataException::class)
    fun cardMixed() {
        createAndExerciseReader("card_payments_mixed")
    }

    private fun createAndExerciseReader(csvFileName: String): PaymentsCSVFileReader {
        val reader = BankPaymentsCSVFileReader(Fixture.createCSVFileReader(csvFileName))
        reader.readAll()
        return reader
    }
}
