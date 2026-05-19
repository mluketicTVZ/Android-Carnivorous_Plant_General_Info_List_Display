package hr.tvz.android.fragmentiluketic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.fragmentiluketic.databinding.ItemPlantBinding

class PlantAdapter(
    private val plants: List<CarnivorousPlant>,
    private val onItemClick: (CarnivorousPlant) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        val ctx   = holder.itemView.context

        holder.binding.txtPlantName.text      = ctx.getString(plant.nameResId)
        holder.binding.txtPlantShortDesc.text = ctx.getString(plant.shortDescResId)
        holder.binding.imgPlantThumb.setImageResource(plant.drawableResId)

        holder.binding.root.setOnClickListener {
            onItemClick(plant)
        }
    }

    override fun getItemCount(): Int = plants.size
}
