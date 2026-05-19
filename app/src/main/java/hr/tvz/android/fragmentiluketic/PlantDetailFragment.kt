package hr.tvz.android.fragmentiluketic

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.tvz.android.fragmentiluketic.databinding.FragmentPlantDetailBinding

class PlantDetailFragment : Fragment() {

    private var _binding: FragmentPlantDetailBinding? = null
    private val binding get() = _binding!!

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
            arguments?.getParcelable(ARG_PLANT, CarnivorousPlant::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_PLANT)
        } ?: return

        bindViews(plant)
    }

    private fun bindViews(plant: CarnivorousPlant) {
        binding.txtDetailName.text     = getString(plant.nameResId)
        binding.txtDetailLongDesc.text = getString(plant.longDescResId)
        binding.txtDetailHabitat.text  = getString(plant.habitatResId)
        binding.txtDetailTrapping.text = getString(plant.trappingResId)
        binding.imgDetailPlant.setImageResource(plant.drawableResId)

        binding.imgDetailPlant.setOnClickListener {
            val intent = Intent(requireContext(), ImageActivity::class.java).apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PLANT = "arg_plant"

        fun newInstance(plant: CarnivorousPlant): PlantDetailFragment {
            return PlantDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLANT, plant)
                }
            }
        }
    }
}
