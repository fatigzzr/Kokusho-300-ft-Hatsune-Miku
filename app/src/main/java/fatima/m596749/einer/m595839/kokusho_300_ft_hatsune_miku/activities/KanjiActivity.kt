package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities

import android.os.Bundle
import android.view.LayoutInflater
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

        CoroutineScope(Dispatchers.IO).launch {
            val allRadicals = kanjiDao.getAllRadicalsWithPosition()

            val grouped = allRadicals.groupBy { it.position }

            rightRadicals = grouped[Position.RIGHT]?.map { it.radical } ?: emptyList()
            leftRadicals = grouped[Position.LEFT]?.map { it.radical } ?: emptyList()
            upRadicals = grouped[Position.UP]?.map { it.radical } ?: emptyList()
            downRadicals = grouped[Position.DOWN]?.map { it.radical } ?: emptyList()
            outsideRadicals = grouped[Position.OUTSIDE]?.map { it.radical } ?: emptyList()
            hangingRadicals = grouped[Position.HANGING]?.map { it.radical } ?: emptyList()
            chairRadicals = grouped[Position.CHAIR]?.map { it.radical } ?: emptyList()
            otherRadicals = grouped[Position.OTHER]?.map { it.radical } ?: emptyList()

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

        binding.kanjiBackButton.setOnClickListener {
            finish()
        }
    }
}
