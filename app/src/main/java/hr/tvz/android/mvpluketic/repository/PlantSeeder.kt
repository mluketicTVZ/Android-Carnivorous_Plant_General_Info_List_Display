package hr.tvz.android.mvpluketic.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import hr.tvz.android.mvpluketic.R
import hr.tvz.android.mvpluketic.api.CloudinaryService
import hr.tvz.android.mvpluketic.api.CloudinaryUploadResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

/**
 * One-time seeder: renders each plant vector drawable to a PNG,
 * uploads it to Cloudinary (unsigned upload), then writes plant metadata + imageUrl to Firestore.
 * Skips if the "plants" collection already has documents.
 *
 * Requires Firestore test-mode rules (allow read, write: if true).
 */
object PlantSeeder {

    private data class SeedData(
        val id:         String,
        val name:       String,
        val shortDesc:  String,
        val longDesc:   String,
        val habitat:    String,
        val trapping:   String,
        val wikiUrl:    String,
        val drawableRes: Int
    )

    /** Checks Firestore; seeds only if the collection is empty or images are missing. Calls [onDone] when finished. */
    fun seedIfNeeded(context: Context, onDone: () -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("plants")
            .get()
            .addOnSuccessListener { snapshot ->
                val needsSeed = snapshot.isEmpty ||
                    snapshot.documents.any { it.getString("imageUrl").isNullOrEmpty() }
                if (needsSeed) {
                    // Delete existing incomplete docs then re-upload
                    val batch = FirebaseFirestore.getInstance().batch()
                    snapshot.documents.forEach { batch.delete(it.reference) }
                    batch.commit().addOnCompleteListener { uploadAll(context, onDone) }
                } else {
                    onDone()
                }
            }
            .addOnFailureListener { onDone() }
    }

    private fun uploadAll(context: Context, onDone: () -> Unit) {
        val seeds = buildSeedList(context)
        var done = 0
        seeds.forEach { seed ->
            uploadOneAndSave(context, seed) {
                done++
                if (done == seeds.size) onDone()
            }
        }
    }

    private val cloudinary: CloudinaryService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.cloudinary.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryService::class.java)
    }

    private fun uploadOneAndSave(context: Context, seed: SeedData, onDone: () -> Unit) {
        val bitmap = drawableToBitmap(context, seed.drawableRes) ?: run { onDone(); return }
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val bytes = baos.toByteArray()

        val filePart = MultipartBody.Part.createFormData(
            "file",
            "${seed.id}.png",
            bytes.toRequestBody("image/png".toMediaTypeOrNull())
        )
        val presetBody: RequestBody = "mvp_plants"
            .toRequestBody("text/plain".toMediaTypeOrNull())

        cloudinary.uploadImage(filePart, presetBody)
            .enqueue(object : Callback<CloudinaryUploadResponse> {
                override fun onResponse(
                    call: Call<CloudinaryUploadResponse>,
                    response: Response<CloudinaryUploadResponse>
                ) {
                    val imageUrl = response.body()?.secure_url ?: ""
                    saveToFirestore(seed, imageUrl, onDone)
                }
                override fun onFailure(call: Call<CloudinaryUploadResponse>, t: Throwable) {
                    saveToFirestore(seed, "", onDone)
                }
            })
    }

    private fun saveToFirestore(seed: SeedData, imageUrl: String, onDone: () -> Unit) {
        val doc = mapOf(
            "name"      to seed.name,
            "shortDesc" to seed.shortDesc,
            "longDesc"  to seed.longDesc,
            "habitat"   to seed.habitat,
            "trapping"  to seed.trapping,
            "wikiUrl"   to seed.wikiUrl,
            "imageUrl"  to imageUrl
        )
        FirebaseFirestore.getInstance()
            .collection("plants")
            .document(seed.id)
            .set(doc)
            .addOnCompleteListener { onDone() }
    }

    private fun drawableToBitmap(context: Context, drawableRes: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return null
        val size = 256
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, size, size)
        drawable.draw(canvas)
        return bitmap
    }

    private fun buildSeedList(context: Context): List<SeedData> = listOf(
        SeedData("venus",       context.getString(R.string.plant_venus_name),        context.getString(R.string.plant_venus_short),        context.getString(R.string.plant_venus_long),        context.getString(R.string.plant_venus_habitat),        context.getString(R.string.plant_venus_trapping),        context.getString(R.string.plant_venus_wiki),        R.drawable.ic_plant_venus),
        SeedData("sarracenia",  context.getString(R.string.plant_sarr_name),         context.getString(R.string.plant_sarr_short),         context.getString(R.string.plant_sarr_long),         context.getString(R.string.plant_sarr_habitat),         context.getString(R.string.plant_sarr_trapping),         context.getString(R.string.plant_sarr_wiki),         R.drawable.ic_plant_sarracenia),
        SeedData("nepenthes",   context.getString(R.string.plant_nep_name),          context.getString(R.string.plant_nep_short),          context.getString(R.string.plant_nep_long),          context.getString(R.string.plant_nep_habitat),          context.getString(R.string.plant_nep_trapping),          context.getString(R.string.plant_nep_wiki),          R.drawable.ic_plant_nepenthes),
        SeedData("sundew",      context.getString(R.string.plant_sundew_name),       context.getString(R.string.plant_sundew_short),       context.getString(R.string.plant_sundew_long),       context.getString(R.string.plant_sundew_habitat),       context.getString(R.string.plant_sundew_trapping),       context.getString(R.string.plant_sundew_wiki),       R.drawable.ic_plant_sundew),
        SeedData("bladderwort", context.getString(R.string.plant_bladder_name),      context.getString(R.string.plant_bladder_short),      context.getString(R.string.plant_bladder_long),      context.getString(R.string.plant_bladder_habitat),      context.getString(R.string.plant_bladder_trapping),      context.getString(R.string.plant_bladder_wiki),      R.drawable.ic_plant_bladderwort),
        SeedData("butterwort",  context.getString(R.string.plant_butter_name),       context.getString(R.string.plant_butter_short),       context.getString(R.string.plant_butter_long),       context.getString(R.string.plant_butter_habitat),       context.getString(R.string.plant_butter_trapping),       context.getString(R.string.plant_butter_wiki),       R.drawable.ic_plant_butterwort),
        SeedData("cobra",       context.getString(R.string.plant_cobra_name),        context.getString(R.string.plant_cobra_short),        context.getString(R.string.plant_cobra_long),        context.getString(R.string.plant_cobra_habitat),        context.getString(R.string.plant_cobra_trapping),        context.getString(R.string.plant_cobra_wiki),        R.drawable.ic_plant_cobra),
        SeedData("cephalotus",  context.getString(R.string.plant_cephalotus_name),   context.getString(R.string.plant_cephalotus_short),   context.getString(R.string.plant_cephalotus_long),   context.getString(R.string.plant_cephalotus_habitat),   context.getString(R.string.plant_cephalotus_trapping),   context.getString(R.string.plant_cephalotus_wiki),   R.drawable.ic_plant_cephalotus)
    )
}
