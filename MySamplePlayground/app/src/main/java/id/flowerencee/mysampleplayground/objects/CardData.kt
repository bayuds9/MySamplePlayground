package id.flowerencee.mysampleplayground.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardData(
    var id: String? = null,
    var position: Int? = null,
    var imageUrl: String? = null,
    var title: String? = null,
    var description: String? = null,
    var selected: Boolean? = null,
    var price: Double? = null,
    var quantity: Int? = null,
    var limit: Int? = null,
    var total: Double? = null
) : Parcelable
