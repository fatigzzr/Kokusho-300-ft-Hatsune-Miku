package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.kanji

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.CharacterWord
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.WordPageBinding

class WordPageAdapter(
    private val words: List<CharacterWord>
) : RecyclerView.Adapter<WordPageAdapter.WordViewHolder>() {

    inner class WordViewHolder(val binding: WordPageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = WordPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.binding.hiraganaReading.text = word.wordHiragana
        holder.binding.kanjiReading.text = word.wordKanji
        holder.binding.englishReading.text = word.wordEnglish
    }
}
