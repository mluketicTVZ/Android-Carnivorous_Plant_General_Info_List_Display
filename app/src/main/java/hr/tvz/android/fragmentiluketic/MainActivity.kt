package hr.tvz.android.fragmentiluketic

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.fragmentiluketic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = PlantAdapter(buildPlantList())

        binding.recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_slide_in)
    }

    private fun buildPlantList(): List<CarnivorousPlant> = listOf(
        CarnivorousPlant(
            id             = 0,
            nameResId      = R.string.plant_venus_name,
            shortDescResId = R.string.plant_venus_short,
            longDescResId  = R.string.plant_venus_long,
            habitatResId   = R.string.plant_venus_habitat,
            trappingResId  = R.string.plant_venus_trapping,
            drawableResId  = R.drawable.ic_plant_venus,
            wikiUrl        = getString(R.string.plant_venus_wiki)
        ),
        CarnivorousPlant(
            id             = 1,
            nameResId      = R.string.plant_sarr_name,
            shortDescResId = R.string.plant_sarr_short,
            longDescResId  = R.string.plant_sarr_long,
            habitatResId   = R.string.plant_sarr_habitat,
            trappingResId  = R.string.plant_sarr_trapping,
            drawableResId  = R.drawable.ic_plant_sarracenia,
            wikiUrl        = getString(R.string.plant_sarr_wiki)
        ),
        CarnivorousPlant(
            id             = 2,
            nameResId      = R.string.plant_nep_name,
            shortDescResId = R.string.plant_nep_short,
            longDescResId  = R.string.plant_nep_long,
            habitatResId   = R.string.plant_nep_habitat,
            trappingResId  = R.string.plant_nep_trapping,
            drawableResId  = R.drawable.ic_plant_nepenthes,
            wikiUrl        = getString(R.string.plant_nep_wiki)
        ),
        CarnivorousPlant(
            id             = 3,
            nameResId      = R.string.plant_sundew_name,
            shortDescResId = R.string.plant_sundew_short,
            longDescResId  = R.string.plant_sundew_long,
            habitatResId   = R.string.plant_sundew_habitat,
            trappingResId  = R.string.plant_sundew_trapping,
            drawableResId  = R.drawable.ic_plant_sundew,
            wikiUrl        = getString(R.string.plant_sundew_wiki)
        ),
        CarnivorousPlant(
            id             = 4,
            nameResId      = R.string.plant_bladder_name,
            shortDescResId = R.string.plant_bladder_short,
            longDescResId  = R.string.plant_bladder_long,
            habitatResId   = R.string.plant_bladder_habitat,
            trappingResId  = R.string.plant_bladder_trapping,
            drawableResId  = R.drawable.ic_plant_bladderwort,
            wikiUrl        = getString(R.string.plant_bladder_wiki)
        ),
        CarnivorousPlant(
            id             = 5,
            nameResId      = R.string.plant_butter_name,
            shortDescResId = R.string.plant_butter_short,
            longDescResId  = R.string.plant_butter_long,
            habitatResId   = R.string.plant_butter_habitat,
            trappingResId  = R.string.plant_butter_trapping,
            drawableResId  = R.drawable.ic_plant_butterwort,
            wikiUrl        = getString(R.string.plant_butter_wiki)
        ),
        CarnivorousPlant(
            id             = 6,
            nameResId      = R.string.plant_cobra_name,
            shortDescResId = R.string.plant_cobra_short,
            longDescResId  = R.string.plant_cobra_long,
            habitatResId   = R.string.plant_cobra_habitat,
            trappingResId  = R.string.plant_cobra_trapping,
            drawableResId  = R.drawable.ic_plant_cobra,
            wikiUrl        = getString(R.string.plant_cobra_wiki)
        ),
        CarnivorousPlant(
            id             = 7,
            nameResId      = R.string.plant_cephalotus_name,
            shortDescResId = R.string.plant_cephalotus_short,
            longDescResId  = R.string.plant_cephalotus_long,
            habitatResId   = R.string.plant_cephalotus_habitat,
            trappingResId  = R.string.plant_cephalotus_trapping,
            drawableResId  = R.drawable.ic_plant_cephalotus,
            wikiUrl        = getString(R.string.plant_cephalotus_wiki)
        )
    )
}