package hr.tvz.android.mvpluketic

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlantDao {

    @Query("SELECT * FROM carnivorous_plants ORDER BY id ASC")
    fun getAll(): List<CarnivorousPlant>

    @Insert
    fun insertAll(plants: List<CarnivorousPlant>)

    @Query("SELECT COUNT(*) FROM carnivorous_plants")
    fun count(): Int
}
