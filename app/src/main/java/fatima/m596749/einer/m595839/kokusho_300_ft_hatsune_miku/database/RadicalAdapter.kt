package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.KanjiActivity
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Component
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.RadicalItemViewBinding

class RadicalAdapter(
    private val items: List<KanjiActivity.RadicalWithPosition>,
    private val onComponentClick: (KanjiActivity.RadicalWithPosition) -> Unit
) : RecyclerView.Adapter<RadicalAdapter.RadicalViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()

    inner class RadicalViewHolder(val binding: RadicalItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadicalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RadicalItemViewBinding.inflate(inflater, parent, false)
        return RadicalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RadicalViewHolder, position: Int) {
        val item = items[position]
        holder.binding.radicalTextView.text = item.radical

        val isSelected = selectedPositions.contains(position)
        holder.binding.radicalButton.isSelected = isSelected

        holder.binding.radicalButton.setOnClickListener {
            if (isSelected) {
                selectedPositions.remove(position)
            } else {
                selectedPositions.add(position)
            }

            notifyItemChanged(position)
            onComponentClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

}
