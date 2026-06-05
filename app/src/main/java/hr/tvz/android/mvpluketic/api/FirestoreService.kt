package hr.tvz.android.mvpluketic.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FirestoreService {
    @GET("projects/{projectId}/databases/(default)/documents/{collection}")
    fun getCollection(
        @Path("projectId")  projectId:  String,
        @Path("collection") collection: String,
        @Query("pageSize")  pageSize:   Int = 100
    ): Call<FirestoreListResponse>
}
