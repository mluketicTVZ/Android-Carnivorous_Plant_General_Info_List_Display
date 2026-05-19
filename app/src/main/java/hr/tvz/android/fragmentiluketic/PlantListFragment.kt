package hr.tvz.android.fragmentiluketic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.fragmentiluketic.databinding.FragmentPlantListBinding

class PlantListFragment : Fragment() {

    interface OnPlantSelectedListener {
        fun onPlantSelected(plant: CarnivorousPlant)
    }

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!

    private var listener: OnPlantSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnPlantSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFragment.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFragment.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_slide_in)

        val context = requireContext()
        val db = PlantDatabase.getInstance(context)

        Thread {
            val dao = db.plantDao()
            if (dao.count() == 0) {
                dao.insertAll(buildPlantList(context))
            }
            val plants = dao.getAll()
            requireActivity().runOnUiThread {
                binding.recyclerViewFragment.adapter = PlantAdapter(plants) { plant ->
                    listener?.onPlantSelected(plant)
                }
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun buildPlantList(context: Context): List<CarnivorousPlant> = listOf(
        CarnivorousPlant(
            id             = 0,
            nameResId      = R.string.plant_venus_name,
            shortDescResId = R.string.plant_venus_short,
            longDescResId  = R.string.plant_venus_long,
            habitatResId   = R.string.plant_venus_habitat,
            trappingResId  = R.string.plant_venus_trapping,
            drawableResId  = R.drawable.ic_plant_venus,
            wikiUrl        = context.getString(R.string.plant_venus_wiki)
        ),
        CarnivorousPlant(
            id             = 1,
            nameResId      = R.string.plant_sarr_name,
            shortDescResId = R.string.plant_sarr_short,
            longDescResId  = R.string.plant_sarr_long,
            habitatResId   = R.string.plant_sarr_habitat,
            trappingResId  = R.string.plant_sarr_trapping,
            drawableResId  = R.drawable.ic_plant_sarracenia,
            wikiUrl        = context.getString(R.string.plant_sarr_wiki)
        ),
        CarnivorousPlant(
            id             = 2,
            nameResId      = R.string.plant_nep_name,
            shortDescResId = R.string.plant_nep_short,
            longDescResId  = R.string.plant_nep_long,
            habitatResId   = R.string.plant_nep_habitat,
            trappingResId  = R.string.plant_nep_trapping,
            drawableResId  = R.drawable.ic_plant_nepenthes,
            wikiUrl        = context.getString(R.string.plant_nep_wiki)
        ),
        CarnivorousPlant(
            id             = 3,
            nameResId      = R.string.plant_sundew_name,
            shortDescResId = R.string.plant_sundew_short,
            longDescResId  = R.string.plant_sundew_long,
            habitatResId   = R.string.plant_sundew_habitat,
            trappingResId  = R.string.plant_sundew_trapping,
            drawableResId  = R.drawable.ic_plant_sundew,
            wikiUrl        = context.getString(R.string.plant_sundew_wiki)
        ),
        CarnivorousPlant(
            id             = 4,
            nameResId      = R.string.plant_bladder_name,
            shortDescResId = R.string.plant_bladder_short,
            longDescResId  = R.string.plant_bladder_long,
            habitatResId   = R.string.plant_bladder_habitat,
            trappingResId  = R.string.plant_bladder_trapping,
            drawableResId  = R.drawable.ic_plant_bladderwort,
            wikiUrl        = context.getString(R.string.plant_bladder_wiki)
        ),
        CarnivorousPlant(
            id             = 5,
            nameResId      = R.string.plant_butter_name,
            shortDescResId = R.string.plant_butter_short,
            longDescResId  = R.string.plant_butter_long,
            habitatResId   = R.string.plant_butter_habitat,
            trappingResId  = R.string.plant_butter_trapping,
            drawableResId  = R.drawable.ic_plant_butterwort,
            wikiUrl        = context.getString(R.string.plant_butter_wiki)
        ),
        CarnivorousPlant(
            id             = 6,
            nameResId      = R.string.plant_cobra_name,
            shortDescResId = R.string.plant_cobra_short,
            longDescResId  = R.string.plant_cobra_long,
            habitatResId   = R.string.plant_cobra_habitat,
            trappingResId  = R.string.plant_cobra_trapping,
            drawableResId  = R.drawable.ic_plant_cobra,
            wikiUrl        = context.getString(R.string.plant_cobra_wiki)
        ),
        CarnivorousPlant(
            id             = 7,
            nameResId      = R.string.plant_cephalotus_name,
            shortDescResId = R.string.plant_cephalotus_short,
            longDescResId  = R.string.plant_cephalotus_long,
            habitatResId   = R.string.plant_cephalotus_habitat,
            trappingResId  = R.string.plant_cephalotus_trapping,
            drawableResId  = R.drawable.ic_plant_cephalotus,
            wikiUrl        = context.getString(R.string.plant_cephalotus_wiki)
        )
    )
}
