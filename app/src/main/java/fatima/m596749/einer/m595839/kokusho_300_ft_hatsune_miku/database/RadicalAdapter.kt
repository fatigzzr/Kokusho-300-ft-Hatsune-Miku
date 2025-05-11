package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.KanjiActivity
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.RadicalItemViewBinding

// Adapter class to bind a list of radicals (with positions) to a RecyclerView
class RadicalAdapter(
    private val items: List<KanjiActivity.RadicalWithPosition>, // List of radical items to display
    private val onComponentClick: (KanjiActivity.RadicalWithPosition) -> Unit // Callback when a radical is clicked
) : RecyclerView.Adapter<RadicalAdapter.RadicalViewHolder>() {

    // List of selected Radical positions
    private val selectedPositions = mutableSetOf<Int>()

    // ViewHolder that holds reference to the view for each radical item
    inner class RadicalViewHolder(val binding: RadicalItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Inflates the view for a radical item from the XML layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadicalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RadicalItemViewBinding.inflate(inflater, parent, false)
        return RadicalViewHolder(binding)
    }

    // Binds data (radical text) to the views and handles selection logic
    override fun onBindViewHolder(holder: RadicalViewHolder, position: Int) {
        val item = items[position]
        holder.binding.radicalTextView.text = item.radical

        // Select or unselect button
        val isSelected = selectedPositions.contains(position)
        holder.binding.radicalButton.isSelected = isSelected

        // Toggle selection and notify listener
        holder.binding.radicalButton.setOnClickListener {
            if (isSelected) {
                selectedPositions.remove(position) // Unselect
            } else {
                selectedPositions.add(position) // Select
            }

            // Refresh this item to update selection state
            notifyItemChanged(position)

            // Notify the activity about this radical click
            onComponentClick(item)
        }
    }

    // Returns the total number of radical items in the list
    override fun getItemCount(): Int = items.size
}
