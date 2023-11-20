package id.flowerencee.mysampleplayground.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextData(
    var id: String? = null,
    var text: String? = null,
    var hint: String? = null,
    var position: Int? = null,
    var inputType: String? = null,
    var limit: Int = 99,
    var clickable: Boolean = false
) : Parcelable
