package hr.tvz.android.fragmentiluketic

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.tvz.android.fragmentiluketic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), PlantListFragment.OnPlantSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var isTwoPane = false

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        // detailContainer only exists in the landscape (layout-land) variant
        isTwoPane = findViewById<android.view.View?>(R.id.detailContainer) != null

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, PlantListFragment())
                .commit()
        }
    }

    override fun onPlantSelected(plant: CarnivorousPlant) {
        if (isTwoPane) {
            // Landscape: show detail in the right pane, hide the placeholder hint
            findViewById<android.view.View?>(R.id.txtDetailHint)?.visibility = android.view.View.GONE
            supportFragmentManager.beginTransaction()
                .replace(R.id.detailContainer, PlantDetailFragment.newInstance(plant))
                .commit()
        } else {
            // Portrait: open DetailActivity as before
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(CarnivorousPlant.EXTRA_KEY, plant)
            }
            startActivity(intent)
        }
    }
}
