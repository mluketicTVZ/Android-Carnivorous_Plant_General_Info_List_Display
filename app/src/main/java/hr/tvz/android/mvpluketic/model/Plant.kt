package hr.tvz.android.mvpluketic.model

import android.os.Parcel
import android.os.Parcelable

data class Plant(
    val id: String = "",
    val name: String = "",
    val shortDesc: String = "",
    val longDesc: String = "",
    val habitat: String = "",
    val trapping: String = "",
    val imageUrl: String = "",
    val wikiUrl: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id        = parcel.readString() ?: "",
        name      = parcel.readString() ?: "",
        shortDesc = parcel.readString() ?: "",
        longDesc  = parcel.readString() ?: "",
        habitat   = parcel.readString() ?: "",
        trapping  = parcel.readString() ?: "",
        imageUrl  = parcel.readString() ?: "",
        wikiUrl   = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(shortDesc)
        parcel.writeString(longDesc)
        parcel.writeString(habitat)
        parcel.writeString(trapping)
        parcel.writeString(imageUrl)
        parcel.writeString(wikiUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Plant> {
        const val EXTRA_KEY = "extra_plant"

        override fun createFromParcel(parcel: Parcel): Plant = Plant(parcel)
        override fun newArray(size: Int): Array<Plant?> = arrayOfNulls(size)
    }
}
