package hr.tvz.android.mvpluketic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hr.tvz.android.mvpluketic.contract.PlantListContract
import hr.tvz.android.mvpluketic.databinding.FragmentPlantListBinding
import hr.tvz.android.mvpluketic.model.Plant
import hr.tvz.android.mvpluketic.presenter.PlantListPresenter

class PlantListFragment : Fragment(), PlantListContract.View {

    interface OnPlantSelectedListener {
        fun onPlantSelected(plant: Plant)
    }

    private var _binding: FragmentPlantListBinding? = null
    private val binding get() = _binding!!

    private var listener: OnPlantSelectedListener? = null
    private lateinit var presenter: PlantListContract.Presenter

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

        presenter = PlantListPresenter(this, requireContext())
        presenter.loadPlants()
    }

    // ── PlantListContract.View ─────────────────────────────────────────────

    override fun showPlants(plants: List<Plant>) {
        binding.recyclerViewFragment.adapter = PlantAdapter(plants) { plant ->
            listener?.onPlantSelected(plant)
        }
    }

    override fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    // ── Lifecycle ──────────────────────────────────────────────────────────

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
