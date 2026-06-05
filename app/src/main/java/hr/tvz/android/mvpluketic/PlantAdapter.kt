package hr.tvz.android.mvpluketic

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.mvpluketic.databinding.ItemPlantBinding
import hr.tvz.android.mvpluketic.model.Plant

class PlantAdapter(
    private val plants: List<Plant>,
    private val onItemClick: (Plant) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        holder.binding.txtPlantName.text      = plant.name
        holder.binding.txtPlantShortDesc.text = plant.shortDesc
        holder.binding.imgPlantThumb.setImageURI(Uri.parse(plant.imageUrl))
        holder.binding.root.setOnClickListener { onItemClick(plant) }
    }

    override fun getItemCount(): Int = plants.size
}
