package com.ki.io

import com.ki.Fixture
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.FileReader
import kotlin.test.assertNull

class CSVFileReaderTest {
    private val EXPECTED_CARD_HEADERS = listOf("customer_id", "date", "amount", "card_id", "card_status")
    private val EXPECTED_BANK_HEADERS = listOf("customer_id", "date", "amount", "bank_account_id")

    @Test
    fun cardPaymentsEmpty() {
        val reader = createAndExerciseReader("card_payments_empty")
        assertThat(reader.headerRow, `is`(EXPECTED_CARD_HEADERS))
        assertNull(reader.bodyRows)
    }

    @Test
    fun cardPaymentsMixed() {
        val reader = createAndExerciseReader("card_payments_mixed")
        assertThat(reader.headerRow, `is`(EXPECTED_CARD_HEADERS))
        assertThat(
            reader.bodyRows,
            `is`(
                listOf(
                    listOf("123", "2019-01-12", "900", "30", "processed"),
                    listOf("123", "2019-02-10", "900", "45", "declined"),
                    listOf("456", "2019-01-20", "4200", "10", "processed"),
                ),
            ),
        )
    }

    @Test
    fun bankPaymentsEmpty() {
        val reader = createAndExerciseReader("bank_payments_empty")
        assertThat(reader.headerRow, `is`(EXPECTED_BANK_HEADERS))
        assertNull(reader.bodyRows)
    }

    @Test
    fun bankPaymentsMixed() {
        val reader = createAndExerciseReader("bank_payments_mixed")
        assertThat(reader.headerRow, `is`(EXPECTED_BANK_HEADERS))
        assertThat(
            reader.bodyRows,
            `is`(
                listOf(
                    listOf("789", "2018-10-25", "900", "20"),
                    listOf("345", "2018-11-03", "900", "60"),
                    listOf("1", "2022-01-28", "3", "5"),
                    listOf("2", "2022-01-29", "4", "6"),
                ),
            ),
        )
    }

    private fun createAndExerciseReader(csvFileName: String): AccumulatingCSVFileReader {
        val reader = AccumulatingCSVFileReader(FileReader(Fixture.getPath("$csvFileName.csv")))
        reader.readAll()
        return reader
    }

    private class AccumulatingCSVFileReader(
        fileReader: FileReader,
    ) : CSVFileReader(fileReader) {
        private var _headerRow: List<String>? = null
        private var _bodyRows: MutableList<List<String>>? = null

        override fun onHeaderRow(fieldNames: List<String>) {
            if (null != _headerRow) {
                throw IllegalStateException("Received more than one call to onHeaderRow.")
            }
            _headerRow = fieldNames
        }

        override fun onBodyRow(fieldValues: List<String>) {
            bodyRowsAccumulator.add(fieldValues)
        }

        private val bodyRowsAccumulator: MutableList<List<String>>
            get() {
                // TODO Replace this with something Kotlin-idiomatic.
                // This is very much "Java-flavoured".
                if (null == _bodyRows) {
                    _bodyRows = mutableListOf()
                }
                return _bodyRows!!
            }

        val headerRow: List<String>?
            get() = _headerRow

        val bodyRows: List<List<String>>?
            get() = _bodyRows
    }
}
