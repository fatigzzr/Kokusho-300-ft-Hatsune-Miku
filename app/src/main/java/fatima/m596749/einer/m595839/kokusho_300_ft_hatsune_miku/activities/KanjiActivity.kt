package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.KanjiDao
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.Position
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.RadicalAdapter
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.CharacterWord
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

    lateinit var rightRadicals: List<String>
    lateinit var leftRadicals: List<String>
    lateinit var upRadicals: List<String>
    lateinit var downRadicals: List<String>
    lateinit var outsideRadicals: List<String>
    lateinit var hangingRadicals: List<String>
    lateinit var chairRadicals: List<String>
    lateinit var otherRadicals: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KanjiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kanjiDao = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "KanjiDB.db"
        ).build().kanjiDao()

        val prevButton = findViewById<Button>(R.id.prevWordButton)
        val nextButton = findViewById<Button>(R.id.nextWordButton)

        CoroutineScope(Dispatchers.IO).launch {
            val allRadicals = kanjiDao.getAllRadicalsWithPosition()

            val grouped = allRadicals.groupBy { it.position }

            rightRadicals = grouped[Position.RIGHT]?.map { it.radical }?.toSet()?.toList() ?: emptyList()
            leftRadicals = grouped[Position.LEFT]?.map { it.radical }?.toSet()?.toList() ?: emptyList()
            upRadicals = grouped[Position.UP]?.map { it.radical }?.toSet()?.toList() ?: emptyList()
            downRadicals = grouped[Position.DOWN]?.map { it.radical }?.toSet()?.toList() ?: emptyList()
            outsideRadicals = grouped[Position.OUTSIDE]?.map { it.radical }?.toSet()?.toList() ?: emptyList()
            hangingRadicals = grouped[Position.HANGING]?.map { it.radical }?.toSet()?.toList() ?: emptyList()
            chairRadicals = grouped[Position.CHAIR]?.map { it.radical }?.toSet()?.toList() ?: emptyList()
            otherRadicals = grouped[Position.OTHER]?.map { it.radical }?.toSet()?.toList() ?: emptyList()

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
                        adapter = RadicalAdapter(radicals)
                    }

                    binding.sectionContainer.addView(sectionBinding.root)
                }
            }
        }

        var currentWordIndex = 0
        //val kanjiId = intent.getIntExtra("KANJI_ID", -1) // or wherever you store the ID

        CoroutineScope(Dispatchers.IO).launch {
            val character = kanjiDao.getCharacterById(1)
            val wordList = kanjiDao.getWordsByCharacterId(1)

            withContext(Dispatchers.Main) {
                //binding.meaningTextView.text = character.meaning

                fun displayWord(word: CharacterWord) {
                    binding.kanjiReading.text = word.wordKanji
                    binding.hiraganaReading.text = word.wordHiragana
                    binding.englishReading.text = word.wordEnglish
                }

                if (wordList.isNotEmpty()) {
                    displayWord(wordList[0])
                }

                prevButton.setOnClickListener {
                    if (wordList.isNotEmpty()) {
                        currentWordIndex = (currentWordIndex - 1 + wordList.size) % wordList.size
                        displayWord(wordList[currentWordIndex])
                    }
                }

                nextButton.setOnClickListener {
                    if (wordList.isNotEmpty()) {
                        currentWordIndex = (currentWordIndex + 1) % wordList.size
                        displayWord(wordList[currentWordIndex])
                    }
                }
            }
        }

        binding.kanjiBackButton.setOnClickListener {
            finish()
        }
    }
}
