package hr.tvz.android.mvpluketic

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.tvz.android.mvpluketic.databinding.ActivityImageBinding

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

        val imageUrl  = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""
        val plantName = intent.getStringExtra(EXTRA_PLANT_NAME) ?: getString(R.string.title_plant_image)

        setSupportActionBar(binding.toolbarImage)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = plantName
        }

        if (imageUrl.isNotEmpty()) {
            binding.imgFullscreen.setImageURI(Uri.parse(imageUrl))
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
        const val EXTRA_IMAGE_URL  = "extra_image_url"
        const val EXTRA_PLANT_NAME = "extra_plant_name"
    }
}

