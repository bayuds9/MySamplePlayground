package id.flowerencee.mysampleplayground.objects

data class Header(
    val label: String?,
    val subLabel: String?
)

data class Option(
    val id: String,
    val label: String
)

data class JsonData(
    val id: String,
    val type: Int,
    val header: Header,
    val data: List<GenericData>,
    val values: List<String>,
    val option: List<Option>,
    val expanded: Boolean = false
)

data class GenericData(
    val id: String,
    val position: Int,
    val imageUrl: String,
    val title: String,
    val description: String,
    val price: Double,
    val text: String,
    val hint: String,
    val inputType: String,
    val limit: Int,
    val clickable: Boolean
)