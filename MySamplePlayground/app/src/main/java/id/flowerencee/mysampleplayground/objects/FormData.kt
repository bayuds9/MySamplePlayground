package id.flowerencee.mysampleplayground.objects

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormData(
    var id: String? = null,
    var label: String? = null,
    var subLabel: String? = null,
    var type: Int? = null,
    var texts: ArrayList<TextData>? = null,
    var cards: ArrayList<CardData>? = null,
    var checkboxes: ArrayList<CheckBoxData>? = null,
    var webViews: ArrayList<String>? = null,
    var expanded: Boolean = false
) : Parcelable
