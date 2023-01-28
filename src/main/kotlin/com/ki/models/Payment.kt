package com.ki.models

import com.ki.Config
import com.ki.io.InvalidDataException
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Data class defining the record structure for a customer payment.
 *
 * **IMPORTANT**: The API offered by this class is relied upon by other services.
 * The module it's included in is installed on the platform as a dependency for those other services.
 */
class Payment {
    var customerId = 0
    var date: LocalDate? = null
    var amount = 0
    var fee = 0

    var card: Card? = null

    private val ignoreCard: Boolean

    /**
     * Construct a new Card [Payment].
     */
    constructor() {
        ignoreCard = false
    }

    /**
     * Construct a new Card [Payment].
     */
    constructor(data: Array<String>) {
        parseToCommonFields(data, 5)
        ignoreCard = false

        val card = Card()
        card.cardId = data[3].toInt()
        card.status = data[4]
        this.card = card
    }

    /**
     * Construct a new Card or Bank [Payment].
     */
    private constructor(ignoreCard: Boolean) {
        this.ignoreCard = ignoreCard
    }

    val isSuccessful: Boolean
        get() = ignoreCard || (card?.status == "processed")

    internal companion object {
        /**
         * Create a new Bank [Payment].
         */
        fun bank(data: Array<String>): Payment {
            val payment = Payment(true)
            payment.parseToCommonFields(data)
            return payment
        }
    }

    private fun parseToCommonFields(data: Array<String>, minimumFieldCount: Int = 3) {
        val fieldCount = data.size
        if (fieldCount < minimumFieldCount) {
            throw InvalidDataException("Expected at least $minimumFieldCount fields, but found $fieldCount.")
        }

        customerId = data[0].toInt()
        val paymentFeeRate = Config.paymentFeeRate
        val totalAmount = data[2].toInt()
        fee = paymentFeeRate.multiply(BigDecimal(totalAmount)).toInt()
        amount = totalAmount - fee
        date = LocalDate.parse(data[1])
    }
}
