package hr.tvz.android.mvpluketic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ShareBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_PLANT_SHARED) {
            val message = context.getString(R.string.broadcast_shared)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val ACTION_PLANT_SHARED = "hr.tvz.android.mvpluketic.PLANT_SHARED"
    }
}
