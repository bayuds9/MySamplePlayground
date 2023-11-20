package id.flowerencee.mysampleplayground

import android.app.Application
import com.google.android.material.color.DynamicColors

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}