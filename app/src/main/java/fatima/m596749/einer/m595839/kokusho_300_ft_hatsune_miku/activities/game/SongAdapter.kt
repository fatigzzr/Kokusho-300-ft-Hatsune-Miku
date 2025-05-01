package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game

import android.content.Context
import android.graphics.BitmapFactory
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.SongItemViewBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Song
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.LockedSongItemViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongAdapter(val context: Context,
                  var songsId: List<Int>,
                  val db: AppDatabase,
                  val types: List<Int>,
                  val onItemClick: (Int) -> Unit)
                        : RecyclerView.Adapter<SongAdapter.viewHolder>() {
    inner class viewHolder(val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(songId: Int, position: Int) {
            try {
                // Asset Manager to get the images
                val assetManager = context.assets
                val inputStream = assetManager.open("cover${songId}.jpg")

                CoroutineScope(Dispatchers.IO).launch {
                    // Query
                    val songInfo = db.songDao().getSongInfo(songId)

                    // Switch to the main thread to update the UI
                    CoroutineScope(Dispatchers.Main).launch {
                        val bitmap = BitmapFactory.decodeStream(inputStream)

                        when (binding) {
                            is SongItemViewBinding -> {
                                binding.songImage.setImageBitmap(bitmap)
                                binding.songTitle.text = songInfo . title
                                binding.songPoints.text = "Record: ${songInfo.points} puntos"

                                binding.root.setOnClickListener {
                                    onItemClick(position)
                                }
                            }
                            is LockedSongItemViewBinding -> {
                                binding.songImage.setImageBitmap(bitmap)
                                binding.songTitle.text = songInfo . title
                                binding.songPoints.text = "Record: ${songInfo.points} puntos"
                                binding.root.isClickable = false
                                binding.root.isEnabled = false
                            }
                        }
                    }
                }
            }
            catch (e: Exception) {
                Log.e("SongAdapter", "Error binding song data", e)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = when (viewType) {
            VIEW_TYPE_UNLOCKED -> SongItemViewBinding.inflate(layoutInflater, parent, false)
            VIEW_TYPE_LOCKED -> LockedSongItemViewBinding.inflate(layoutInflater, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }

        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songsId.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(songsId[position], position)
    }

    override fun getItemViewType(position: Int): Int {
        return types[position]
    }

    companion object {
        const val VIEW_TYPE_UNLOCKED = 0
        const val VIEW_TYPE_LOCKED = 1
    }
}