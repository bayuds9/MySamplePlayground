package id.flowerencee.mysampleplayground.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckBoxData(
    var id: String? = null,
    var label: String? = null,
    var actualValue: String? = null
) : Parcelable
