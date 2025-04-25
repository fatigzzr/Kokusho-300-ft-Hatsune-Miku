package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.RadicalItemViewBinding

class RadicalAdapter(var radicals : List<String>, val onItemClick: (Int) -> Unit): RecyclerView.Adapter<RadicalAdapter.RadicalViewHolder>() {
    inner class RadicalViewHolder(val binding: RadicalItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(radical: String, position: Int) {
            binding.radicalTextView.text = radical

            binding.root.setOnClickListener {
                onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadicalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RadicalItemViewBinding.inflate(layoutInflater, parent, false)

        return RadicalViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return radicals.size
    }

    override fun onBindViewHolder(holder: RadicalViewHolder, position: Int) {
        holder.bind(radicals[position], position)
    }
}