package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.InvalidationTracker
import androidx.room.Room
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.kanji.WordPageAdapter
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.KanjiDao
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.Position
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.RadicalAdapter
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.CharacterWord
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Component
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Radical
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.KanjiActivityBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.RadicalSectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KanjiActivity : AppCompatActivity() {

    private lateinit var binding: KanjiActivityBinding
    private lateinit var kanjiDao: KanjiDao

    data class RadicalWithPosition(
        val id: Int,
        val radical: String,
        val meaning: String,
        val position: Position
    )

    lateinit var rightRadicals: List<RadicalWithPosition>
    lateinit var leftRadicals: List<RadicalWithPosition>
    lateinit var upRadicals: List<RadicalWithPosition>
    lateinit var downRadicals: List<RadicalWithPosition>
    lateinit var outsideRadicals: List<RadicalWithPosition>
    lateinit var hangingRadicals: List<RadicalWithPosition>
    lateinit var chairRadicals: List<RadicalWithPosition>
    lateinit var otherRadicals: List<RadicalWithPosition>

    private lateinit var componentsGroupedByCharacterId: Map<Int, List<Component>>

    private var kanjiId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KanjiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kanjiDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "KanjiDB.db"
        ).build().kanjiDao()

        kanjiDao.getFoundAndTotalCounts().observe(this) { result ->
            val displayText = "Kanji Found: ${result.foundCount}/${result.totalCount}"
            binding.kanjiTextView.text = displayText
        }

        val selectedRadicals = mutableListOf<RadicalWithPosition>()

        fun updateKanjiDisplay() {
            CoroutineScope(Dispatchers.IO).launch {
                val character = kanjiDao.getCharacterById(kanjiId)
                val wordList = kanjiDao.getWordsByCharacterId(kanjiId)

                withContext(Dispatchers.Main) {
                    binding.kanjiMeaning.text = character.meaning.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    }

                    if (wordList.isNotEmpty()) {
                        val adapter = WordPageAdapter(wordList)
                        binding.wordViewPager.adapter = adapter
                        binding.dotsIndicator.attachTo(binding.wordViewPager)
                    } else {
                        val placeholderList = listOf(
                            CharacterWord(-1, "Hiragana", "Kanji", "English")
                        )
                        val adapter = WordPageAdapter(placeholderList)
                        binding.wordViewPager.adapter = adapter
                        binding.dotsIndicator.attachTo(binding.wordViewPager)
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val components = kanjiDao.getAllComponents()
            componentsGroupedByCharacterId = components.groupBy { it.idCharacter }
        }

        fun showKanjiInBox(kanji: String) {
            val textView = TextView(this).apply {
                text = kanji
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                setTextAppearance(R.style.KanjiBox)
            }

            binding.kanjiPartsBox.removeAllViews()
            binding.kanjiPartsBox.addView(textView)
        }

        fun clearKanjiDisplay() {
            binding.kanjiPartsBox.removeAllViews()

            binding.kanjiMeaning.text = "Significado"

            val placeholderList = listOf(
                CharacterWord(-1, "Hiragana", "Kanji", "English")
            )

            val placeholderAdapter = WordPageAdapter(placeholderList)
            binding.wordViewPager.adapter = placeholderAdapter
            binding.dotsIndicator.setViewPager2(binding.wordViewPager)
        }

        clearKanjiDisplay()

        fun checkKanjiCompletion() {
            val selectedSet = selectedRadicals.map { Pair(it.id, it.position) }.toSet()

            for ((characterId, components) in componentsGroupedByCharacterId) {
                val componentSet = components.map { Pair(it.idRadical, it.position) }.toSet()

                if (selectedSet == componentSet) {
                    Log.d("KanjiMatch", "✅ Match found for character ID: $characterId")

                    CoroutineScope(Dispatchers.IO).launch {
                        val kanji = kanjiDao.getCharacterById(characterId)
                        kanjiDao.markAsFound(characterId)

                        withContext(Dispatchers.Main) {
                            kanjiId = characterId
                            updateKanjiDisplay()
                            showKanjiInBox(kanji.character)
                        }
                    }

                    return
                }
            }

            Log.d("KanjiMatch", "❌ No match found")
            kanjiId = -1
            clearKanjiDisplay()

        }

        fun toggleRadicalSelection(radical: RadicalWithPosition) {
            if (selectedRadicals.contains(radical)) {
                selectedRadicals.remove(radical)
            } else {
                selectedRadicals.add(radical)
            }
            checkKanjiCompletion()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val allRadicals = kanjiDao.getAllRadicalsWithPosition()

            val grouped = allRadicals.groupBy { it.position }

            rightRadicals = grouped[Position.RIGHT] ?: emptyList()
            leftRadicals = grouped[Position.LEFT] ?: emptyList()
            upRadicals = grouped[Position.UP] ?: emptyList()
            downRadicals = grouped[Position.DOWN] ?: emptyList()
            outsideRadicals = grouped[Position.OUTSIDE] ?: emptyList()
            hangingRadicals = grouped[Position.HANGING] ?: emptyList()
            chairRadicals = grouped[Position.CHAIR] ?: emptyList()
            otherRadicals = grouped[Position.OTHER] ?: emptyList()

            withContext(Dispatchers.Main) {
                val sectionData = listOf(
                    "RIGHT" to rightRadicals,
                    "LEFT" to leftRadicals,
                    "UP" to upRadicals,
                    "DOWN" to downRadicals,
                    "OUTSIDE" to outsideRadicals,
                    "HANGING" to hangingRadicals,
                    "CHAIR" to chairRadicals,
                    "OTHER" to otherRadicals
                )

                for ((title, radicals) in sectionData) {
                    val sectionBinding = RadicalSectionBinding.inflate(layoutInflater, binding.sectionContainer, false)

                    sectionBinding.sectionTitle.text = title
                    sectionBinding.horizontalRecycler.apply {
                        layoutManager = LinearLayoutManager(this@KanjiActivity, LinearLayoutManager.HORIZONTAL, false)
                        adapter = RadicalAdapter(radicals){ radicalWithPosition ->
                            toggleRadicalSelection(radicalWithPosition)
                        }
                    }

                    binding.sectionContainer.addView(sectionBinding.root)
                }
            }
        }

        binding.kanjiBackButton.setOnClickListener {
            finish()
        }
    }
}
