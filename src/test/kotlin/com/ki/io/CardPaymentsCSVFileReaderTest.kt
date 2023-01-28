package com.ki.io

import com.ki.Fixture
import org.junit.Test
import kotlin.test.assertEquals

class CardPaymentsCSVFileReaderTest {
    @Test
    fun empty() {
        val reader = createAndExerciseReader("card_payments_empty")
        assertEquals(0, reader.payments.size)
    }

    @Test
    fun mixed() {
        val reader = createAndExerciseReader("card_payments_mixed")
        assertEquals(3, reader.payments.size)

        // not testing all fields, just a sampling
        val payments = reader.payments
        assertEquals(123, payments[0].customerId)
        assertEquals("processed", payments[0].card!!.status)
        assertEquals(45, payments[1].card!!.cardId)
        assertEquals("declined", payments[1].card!!.status)
    }

    @Test(expected = InvalidDataException::class)
    fun bankEmpty() {
        createAndExerciseReader("bank_payments_empty")
    }

    @Test(expected = InvalidDataException::class)
    fun bankMixed() {
        createAndExerciseReader("bank_payments_mixed")
    }

    private fun createAndExerciseReader(csvFileName: String): PaymentsCSVFileReader {
        val reader = CardPaymentsCSVFileReader(Fixture.createCSVFileReader(csvFileName))
        reader.readAll()
        return reader
    }
}
