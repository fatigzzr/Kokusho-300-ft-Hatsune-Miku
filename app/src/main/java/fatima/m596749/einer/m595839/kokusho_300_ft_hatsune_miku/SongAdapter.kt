package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.SongItemViewBinding

class SongAdapter(var songs: List<String>, val onItemClick: (Int) -> Unit): RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    inner class SongViewHolder(val binding: SongItemViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: String, position: Int) {
            binding.songTitle.text = song

            binding.root.setOnClickListener {
                onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SongItemViewBinding.inflate(layoutInflater, parent, false)

        return SongViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position], position)
    }
}