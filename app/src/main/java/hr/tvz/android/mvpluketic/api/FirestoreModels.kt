package hr.tvz.android.mvpluketic.api

import hr.tvz.android.mvpluketic.model.Plant

data class FirestoreListResponse(
    val documents: List<FirestoreDocument>? = null
)

data class FirestoreDocument(
    val name: String = "",
    val fields: Map<String, FirestoreValue> = emptyMap()
)

data class FirestoreValue(
    val stringValue: String? = null,
    val integerValue: String? = null
)

fun FirestoreDocument.toPlant(): Plant {
    val f = fields
    return Plant(
        id        = name.substringAfterLast("/"),
        name      = f["name"]?.stringValue      ?: "",
        shortDesc = f["shortDesc"]?.stringValue  ?: "",
        longDesc  = f["longDesc"]?.stringValue   ?: "",
        habitat   = f["habitat"]?.stringValue    ?: "",
        trapping  = f["trapping"]?.stringValue   ?: "",
        imageUrl  = f["imageUrl"]?.stringValue   ?: "",
        wikiUrl   = f["wikiUrl"]?.stringValue    ?: ""
    )
}
