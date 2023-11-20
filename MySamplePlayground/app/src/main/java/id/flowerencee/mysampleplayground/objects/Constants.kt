package id.flowerencee.mysampleplayground.objects

object Constants {
    interface FORM_TYPE {
        companion object {
            const val CARDS = 1
            const val TEXT = 2
            const val WEB_VIEW = 3
            const val CHECK_BOX = 4
            const val INPUT_TEXT = 5
            const val SELECTABLE = 6
            const val QUANTITY = 7
        }
    }

    interface INPUT_TYPE {
        companion object {
            const val EMAIL = "EMAIL"
            const val NUMBER = "NUMBER"
            const val PHONE = "PHONE"
        }
    }

    interface CONTENT_POSITION {
        companion object {
            const val TOP = 1
            const val START = 2
            const val END = 3
        }
    }
}