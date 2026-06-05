package hr.tvz.android.mvpluketic

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import hr.tvz.android.mvpluketic.contract.PlantDetailContract
import hr.tvz.android.mvpluketic.databinding.FragmentPlantDetailBinding
import hr.tvz.android.mvpluketic.model.Plant
import hr.tvz.android.mvpluketic.presenter.PlantDetailPresenter

class PlantDetailFragment : Fragment(), PlantDetailContract.View {

    private var _binding: FragmentPlantDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: PlantDetailContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_PLANT, Plant::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_PLANT)
        } ?: return

        presenter = PlantDetailPresenter(this, plant, requireContext())
        presenter.onViewReady()
    }

    // ── PlantDetailContract.View ───────────────────────────────────────────

    override fun showPlant(plant: Plant) {
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
        startActivity(Intent(requireContext(), ImageActivity::class.java).apply {
            putExtra(ImageActivity.EXTRA_IMAGE_URL, imageUrl)
            putExtra(ImageActivity.EXTRA_PLANT_NAME, plantName)
        })
    }

    override fun showShareConfirmation() {
        Toast.makeText(requireContext(), getString(R.string.broadcast_shared), Toast.LENGTH_SHORT).show()
    }

    // ── Lifecycle ──────────────────────────────────────────────────────────

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARG_PLANT = "arg_plant"

        fun newInstance(plant: Plant): PlantDetailFragment {
            return PlantDetailFragment().apply {
                arguments = Bundle().apply { putParcelable(ARG_PLANT, plant) }
            }
        }
    }
}

