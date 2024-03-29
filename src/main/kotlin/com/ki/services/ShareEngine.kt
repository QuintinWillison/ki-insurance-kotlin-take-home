package com.ki.services

import com.ki.models.Payment
import com.ki.models.ShareOrder
import java.math.BigDecimal

/**
 * Service class offering methods providing the core business logic for share orders.
 *
 * **IMPORTANT**: The API offered by this class is relied upon by other services.
 * The module it's included in is installed on the platform as a dependency for those other services.
 */
class ShareEngine {
    /**
     * Create share order records from payment records.
     *
     * @param sharePrice The price at which the shares are available for purchase.
     * @param payments The payment records, likely from multiple customers, in any order.
     * @return Share order records, one per customer.
     */
    fun generateShareOrders(sharePrice: BigDecimal, payments: Array<Payment>): Array<ShareOrder> {
        val shareOrders = ArrayList<ShareOrder>()
        val paymentsByCustomer = groupPaymentsByCustomer(payments)
        for ((customerId, customerPayments) in paymentsByCustomer) {
            var totalShares = 0
            for (payment in customerPayments) {
                val shares = BigDecimal(payment.amount).divide(sharePrice).toInt()
                totalShares += shares
            }
            val shareOrder = ShareOrder()
            shareOrder.customerId = customerId.toInt()
            shareOrder.shares = totalShares
            shareOrders.add(shareOrder)
        }
        return shareOrders.toTypedArray()
    }

    private fun groupPaymentsByCustomer(payments: Array<Payment>): Map<String, MutableList<Payment>> {
        val paymentsByCustomer: MutableMap<String, MutableList<Payment>> = HashMap()
        for (payment in payments) {
            val customerId = payment.customerId.toString()
            if (!paymentsByCustomer.containsKey(customerId)) {
                paymentsByCustomer[customerId] = ArrayList()
            }
            paymentsByCustomer[customerId]!!.add(payment)
        }
        return paymentsByCustomer
    }
}
