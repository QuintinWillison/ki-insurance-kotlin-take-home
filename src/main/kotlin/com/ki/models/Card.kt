package com.ki.models

/**
 * Data class defining the record structure for a card payment transaction.
 *
 * **IMPORTANT**: The API offered by this class is relied upon by other services.
 * The module it's included in is installed on the platform as a dependency for those other services.
 */
class Card {
    var cardId = 0
    var status: String? = null
}
