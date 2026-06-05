package hr.tvz.android.mvpluketic

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.tvz.android.mvpluketic.contract.PlantDetailContract
import hr.tvz.android.mvpluketic.databinding.ActivityDetailBinding
import hr.tvz.android.mvpluketic.model.Plant
import hr.tvz.android.mvpluketic.presenter.PlantDetailPresenter

class DetailActivity : AppCompatActivity(), PlantDetailContract.View {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var presenter: PlantDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarDetail) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, 0)
            insets
        }

        val plant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Plant.EXTRA_KEY, Plant::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Plant.EXTRA_KEY)
        } ?: run { finish(); return }

        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter = PlantDetailPresenter(this, plant, this)
        presenter.onViewReady()
    }

    // ── PlantDetailContract.View ───────────────────────────────────────────

    override fun showPlant(plant: Plant) {
        supportActionBar?.title = plant.name
        binding.txtDetailName.text     = plant.name
        binding.txtDetailLongDesc.text = plant.longDesc
        binding.txtDetailHabitat.text  = plant.habitat
        binding.txtDetailTrapping.text = plant.trapping
        binding.imgDetailPlant.setImageURI(Uri.parse(plant.imageUrl))
        binding.imgDetailPlant.setOnClickListener { presenter.onImageClicked() }
        binding.btnWikipedia.setOnClickListener    { presenter.onWikiClicked() }
    }

    override fun openWikiPage(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    override fun openImageFullscreen(imageUrl: String, plantName: String) {
        startActivity(Intent(this, ImageActivity::class.java).apply {
            putExtra(ImageActivity.EXTRA_IMAGE_URL, imageUrl)
            putExtra(ImageActivity.EXTRA_PLANT_NAME, plantName)
        })
    }

    override fun showShareConfirmation() {
        Toast.makeText(this, getString(R.string.broadcast_shared), Toast.LENGTH_SHORT).show()
    }

    // ── Options menu (share) ───────────────────────────────────────────────

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> { onBackPressedDispatcher.onBackPressed(); true }
        R.id.action_share -> { showShareDialog(); true }
        else              -> super.onOptionsItemSelected(item)
    }

    private fun showShareDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.share_dialog_title))
            .setMessage(getString(R.string.share_dialog_message))
            .setPositiveButton(getString(R.string.share_dialog_yes)) { _, _ -> presenter.onShareConfirmed() }
            .setNegativeButton(getString(R.string.share_dialog_no), null)
            .show()
    }

    // ── Lifecycle ──────────────────────────────────────────────────────────

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}
