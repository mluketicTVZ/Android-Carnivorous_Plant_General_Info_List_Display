package hr.tvz.android.fragmentiluketic

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.tvz.android.fragmentiluketic.databinding.ActivityImageBinding

class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarImage) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, 0)
            insets
        }

        val drawableRes = intent.getIntExtra(EXTRA_DRAWABLE_RES, -1)
        val plantName   = intent.getStringExtra(EXTRA_PLANT_NAME) ?: getString(R.string.title_plant_image)

        setSupportActionBar(binding.toolbarImage)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = plantName
        }

        if (drawableRes != -1) {
            binding.imgFullscreen.setImageResource(drawableRes)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_DRAWABLE_RES = "extra_drawable_res"
        const val EXTRA_PLANT_NAME   = "extra_plant_name"
    }
}
