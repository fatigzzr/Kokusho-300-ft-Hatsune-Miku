package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.kanji.WordPageAdapter
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.*
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.*
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.*
import kotlinx.coroutines.*

class KanjiActivity : AppCompatActivity() {

    // View binding and database access object
    private lateinit var binding: KanjiActivityBinding
    private lateinit var kanjiDao: KanjiDao

    // Data class to pair a Radical with its position
    data class RadicalWithPosition(
        val id: Int,
        val radical: String,
        val meaning: String,
        val position: Position
    )

    // Lists to store radicals by position
    lateinit var rightRadicals: List<RadicalWithPosition>
    lateinit var leftRadicals: List<RadicalWithPosition>
    lateinit var upRadicals: List<RadicalWithPosition>
    lateinit var downRadicals: List<RadicalWithPosition>
    lateinit var outsideRadicals: List<RadicalWithPosition>
    lateinit var hangingRadicals: List<RadicalWithPosition>
    lateinit var chairRadicals: List<RadicalWithPosition>
    lateinit var otherRadicals: List<RadicalWithPosition>

    // Map of every Character's Components
    private lateinit var componentsGroupedByCharacterId: Map<Int, List<Component>>

    // ID of the currently shown Kanji, or -1 if none is shown
    private var kanjiId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KanjiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database DAO
        kanjiDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "KanjiDB.db"
        ).build().kanjiDao()

        // Calculate and show amount of Kanji found and Kanji available
        kanjiDao.getFoundAndTotalCounts().observe(this) { result ->
            val displayText = "Encontrados: ${result.foundCount}/${result.totalCount}"
            binding.kanjiTextView.text = displayText
        }

        // Stores currently selected radicals
        val selectedRadicals = mutableListOf<RadicalWithPosition>()

        // Updates the display with the Kanji meaning and associated words
        fun updateKanjiDisplay() {
            CoroutineScope(Dispatchers.IO).launch {
                val character = kanjiDao.getCharacterById(kanjiId)
                val wordList = kanjiDao.getWordsByCharacterId(kanjiId)

                withContext(Dispatchers.Main) {
                    // Capitalize the first letter of the meaning
                    binding.kanjiMeaning.text = character.meaning.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    }

                    // Set up the ViewPager, with default words declared
                    val adapter = WordPageAdapter(
                        if (wordList.isNotEmpty()) wordList
                        else listOf(CharacterWord(-1, "Hiragana", "Kanji", "English"))
                    )
                    binding.wordViewPager.adapter = adapter
                    binding.dotsIndicator.attachTo(binding.wordViewPager)
                }
            }
        }

        // Load all Kanji and their components
        CoroutineScope(Dispatchers.IO).launch {
            val components = kanjiDao.getAllComponents()
            componentsGroupedByCharacterId = components.groupBy { it.idCharacter }
        }

        // Display a Kanji character in the top box
        fun showKanjiInBox(kanji: String) {
            val textView = TextView(this).apply {
                text = kanji
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                setTextAppearance(R.style.KanjiBox)
            }
            binding.kanjiPartsBox.removeAllViews()
            binding.kanjiPartsBox.addView(textView)
        }

        // Clear Kanji information from the box
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

        // Initially show empty display
        clearKanjiDisplay()

        // Check if the selected radicals form a complete Kanji
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

        // Add or remove Radical from selection list
        fun toggleRadicalSelection(radical: RadicalWithPosition) {
            if (selectedRadicals.contains(radical)) {
                selectedRadicals.remove(radical)
            } else {
                selectedRadicals.add(radical)
            }
            checkKanjiCompletion()
        }

        // Load radicals from database and create horizontal sections by position
        CoroutineScope(Dispatchers.IO).launch {
            val allRadicals = kanjiDao.getAllRadicalsWithPosition()
            val grouped = allRadicals.groupBy { it.position }

            // Assign radicals to lists by position
            rightRadicals = grouped[Position.RIGHT] ?: emptyList()
            leftRadicals = grouped[Position.LEFT] ?: emptyList()
            upRadicals = grouped[Position.UP] ?: emptyList()
            downRadicals = grouped[Position.DOWN] ?: emptyList()
            outsideRadicals = grouped[Position.OUTSIDE] ?: emptyList()
            hangingRadicals = grouped[Position.HANGING] ?: emptyList()
            chairRadicals = grouped[Position.CHAIR] ?: emptyList()
            otherRadicals = grouped[Position.OTHER] ?: emptyList()

            withContext(Dispatchers.Main) {
                // Display each radical group in a horizontal section with its title
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
                        adapter = RadicalAdapter(radicals) { radicalWithPosition ->
                            toggleRadicalSelection(radicalWithPosition)
                        }
                    }

                    binding.sectionContainer.addView(sectionBinding.root)
                }
            }
        }

        // Back button to close activity
        binding.kanjiBackButton.setOnClickListener {
            finish()
        }
    }
}
