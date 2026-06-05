package hr.tvz.android.mvpluketic.repository

import hr.tvz.android.mvpluketic.api.FirestoreListResponse
import hr.tvz.android.mvpluketic.api.FirestoreService
import hr.tvz.android.mvpluketic.api.toPlant
import hr.tvz.android.mvpluketic.model.Plant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlantRepository {

    private const val PROJECT_ID = "fragmentiandroid2026"
    private const val COLLECTION = "plants"

    private val service: FirestoreService = Retrofit.Builder()
        .baseUrl("https://firestore.googleapis.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FirestoreService::class.java)

    fun getPlants(
        onSuccess: (List<Plant>) -> Unit,
        onError: (String) -> Unit
    ) {
        service.getCollection(PROJECT_ID, COLLECTION).enqueue(object : Callback<FirestoreListResponse> {
            override fun onResponse(
                call: Call<FirestoreListResponse>,
                response: Response<FirestoreListResponse>
            ) {
                if (response.isSuccessful) {
                    val plants = response.body()?.documents
                        ?.map { it.toPlant() }
                        ?.sortedBy { it.id }
                        ?: emptyList()
                    onSuccess(plants)
                } else {
                    onError("Server error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<FirestoreListResponse>, t: Throwable) {
                onError(t.message ?: "Network error")
            }
        })
    }
}
