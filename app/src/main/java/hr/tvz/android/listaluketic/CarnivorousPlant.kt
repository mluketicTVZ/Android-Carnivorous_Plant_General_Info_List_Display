package hr.tvz.android.listaluketic

import android.os.Parcel
import android.os.Parcelable

data class CarnivorousPlant(
    val id: Int,
    val nameResId: Int,
    val shortDescResId: Int,
    val longDescResId: Int,
    val habitatResId: Int,
    val trappingResId: Int,
    val drawableResId: Int,
    val wikiUrl: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id            = parcel.readInt(),
        nameResId     = parcel.readInt(),
        shortDescResId = parcel.readInt(),
        longDescResId  = parcel.readInt(),
        habitatResId   = parcel.readInt(),
        trappingResId  = parcel.readInt(),
        drawableResId  = parcel.readInt(),
        wikiUrl        = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(nameResId)
        parcel.writeInt(shortDescResId)
        parcel.writeInt(longDescResId)
        parcel.writeInt(habitatResId)
        parcel.writeInt(trappingResId)
        parcel.writeInt(drawableResId)
        parcel.writeString(wikiUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CarnivorousPlant> {
        const val EXTRA_KEY = "extra_carnivorous_plant"

        override fun createFromParcel(parcel: Parcel): CarnivorousPlant = CarnivorousPlant(parcel)
        override fun newArray(size: Int): Array<CarnivorousPlant?> = arrayOfNulls(size)
    }
}
