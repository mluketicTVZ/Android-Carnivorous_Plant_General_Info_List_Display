package hr.tvz.android.mvpluketic

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.facebook.drawee.backends.pipeline.Fresco

class MVPApplication : Application() {

    private var activeActivities = 0

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityStarted(activity: Activity) {
                activeActivities++
                if (activeActivities == 1) {
                    // First activity visible — start music
                    ContextCompat.startForegroundService(
                        this@MVPApplication,
                        Intent(this@MVPApplication, MusicService::class.java)
                    )
                }
            }
            override fun onActivityStopped(activity: Activity) {
                activeActivities--
                if (activeActivities == 0) {
                    // No activities visible — stop music
                    stopService(Intent(this@MVPApplication, MusicService::class.java))
                }
            }
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}
