package com.ki.models

import com.ki.Config
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

    constructor() {}
    constructor(data: Array<String>) {
        customerId = data[0].toInt()
        val paymentFeeRate = Config.paymentFeeRate
        val totalAmount = data[2].toInt()
        fee = paymentFeeRate.multiply(BigDecimal(totalAmount)).toInt()
        amount = totalAmount - fee
        date = LocalDate.parse(data[1])
        val card = Card()
        card.cardId = data[3].toInt()
        card.status = data[4]
        this.card = card
    }

    val isSuccessful: Boolean
        get() = card?.status == "processed"
}
