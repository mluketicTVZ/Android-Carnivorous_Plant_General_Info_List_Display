package hr.tvz.android.mvpluketic

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import hr.tvz.android.mvpluketic.repository.PlantRepository

class PlantWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // goAsync() keeps the BroadcastReceiver process alive until finish() is called,
        // preventing Android from killing it before the async Retrofit callback fires.
        val pendingResult = goAsync()

        PlantRepository.getPlants(
            onSuccess = { plants ->
                val plant = plants.lastOrNull() ?: run {
                    pendingResult.finish()
                    return@getPlants
                }
                appWidgetIds.forEach { widgetId ->
                    val views = RemoteViews(context.packageName, R.layout.widget_plant)

                    views.setTextViewText(R.id.widget_title, plant.name)
                    views.setTextViewText(R.id.widget_desc, plant.shortDesc)

                    // Launch app on tap
                    val intent = Intent(context, MainActivity::class.java)
                    val pending = PendingIntent.getActivity(
                        context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widget_title, pending)
                    views.setOnClickPendingIntent(R.id.widget_image, pending)

                    // Show text immediately before image loads
                    appWidgetManager.updateAppWidget(widgetId, views)

                    // Load image on background thread then update widget again with bitmap
                    Thread {
                        val bitmap = loadBitmapFromUrl(plant.imageUrl)
                            ?: drawableToBitmap(context, R.drawable.ic_launcher_foreground)
                        if (bitmap != null) {
                            views.setImageViewBitmap(R.id.widget_image, bitmap)
                        }
                        appWidgetManager.updateAppWidget(widgetId, views)
                        pendingResult.finish()
                    }.start()
                }
            },
            onError = {
                pendingResult.finish()
            }
        )
    }

    private fun loadBitmapFromUrl(url: String): Bitmap? {
        if (url.isEmpty()) return null
        return try {
            val connection = java.net.URL(url).openConnection().apply {
                connectTimeout = 5000
                readTimeout    = 5000
                connect()
            }
            android.graphics.BitmapFactory.decodeStream(connection.getInputStream())
        } catch (e: Exception) {
            null
        }
    }

    private fun drawableToBitmap(context: Context, drawableRes: Int): Bitmap? {
        val drawable: Drawable = context.getDrawable(drawableRes) ?: return null
        if (drawable is BitmapDrawable) return drawable.bitmap
        val bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, 64, 64)
        drawable.draw(canvas)
        return bitmap
    }
}
