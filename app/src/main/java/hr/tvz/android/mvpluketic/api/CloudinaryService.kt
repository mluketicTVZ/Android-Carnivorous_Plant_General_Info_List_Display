package hr.tvz.android.mvpluketic.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class CloudinaryUploadResponse(
    val secure_url: String = ""
)

interface CloudinaryService {
    @Multipart
    @POST("v1_1/dqoqsapca/image/upload")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody
    ): Call<CloudinaryUploadResponse>
}
