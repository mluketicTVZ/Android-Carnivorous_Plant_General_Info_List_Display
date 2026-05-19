package hr.tvz.android.fragmentiluketic

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.tvz.android.fragmentiluketic.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var plant: CarnivorousPlant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarDetail) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, 0)
            insets
        }

        plant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CarnivorousPlant.EXTRA_KEY, CarnivorousPlant::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(CarnivorousPlant.EXTRA_KEY)
        } ?: run {
            finish()
            return
        }

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(plant.nameResId)
        }

        bindViews()
    }

    private fun bindViews() {
        binding.txtDetailName.text     = getString(plant.nameResId)
        binding.txtDetailLongDesc.text = getString(plant.longDescResId)
        binding.txtDetailHabitat.text  = getString(plant.habitatResId)
        binding.txtDetailTrapping.text = getString(plant.trappingResId)

        binding.imgDetailPlant.setImageResource(plant.drawableResId)

        binding.imgDetailPlant.setOnClickListener {
            val intent = Intent(this, ImageActivity::class.java).apply {
                putExtra(ImageActivity.EXTRA_DRAWABLE_RES, plant.drawableResId)
                putExtra(ImageActivity.EXTRA_PLANT_NAME, getString(plant.nameResId))
            }
            startActivity(intent)
        }

        binding.btnWikipedia.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(plant.wikiUrl))
            startActivity(browserIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_share -> {
                showShareDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showShareDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.share_dialog_title))
            .setMessage(getString(R.string.share_dialog_message))
            .setPositiveButton(getString(R.string.share_dialog_yes)) { _, _ ->
                sendShareBroadcast()
            }
            .setNegativeButton(getString(R.string.share_dialog_no), null)
            .show()
    }


    private fun sendShareBroadcast() {
        val intent = Intent(ShareBroadcastReceiver.ACTION_PLANT_SHARED).apply {
            setPackage(packageName)
            putExtra("plant_name", getString(plant.nameResId))
        }
        sendBroadcast(intent)
    }
}
