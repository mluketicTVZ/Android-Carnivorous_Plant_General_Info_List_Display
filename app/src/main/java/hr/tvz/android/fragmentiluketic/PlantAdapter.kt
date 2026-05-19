package hr.tvz.android.fragmentiluketic

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.tvz.android.fragmentiluketic.databinding.ItemPlantBinding

class PlantAdapter(
    private val plants: List<CarnivorousPlant>
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
            val intent = Intent(ctx, DetailActivity::class.java).apply {
                putExtra(CarnivorousPlant.EXTRA_KEY, plant)
            }
            ctx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = plants.size
}
