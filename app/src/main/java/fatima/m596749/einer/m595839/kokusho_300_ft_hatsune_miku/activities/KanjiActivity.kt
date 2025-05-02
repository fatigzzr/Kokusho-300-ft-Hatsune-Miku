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
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.RadicalAdapter
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.KanjiActivityBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.RadicalSectionBinding

class KanjiActivity : AppCompatActivity() {

    private lateinit var binding: KanjiActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KanjiActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionTitles = listOf(
            "RIGHT",
            "LEFT",
            "UP",
            "DOWN",
            "OUTSIDE",
            "HANGING",
            "CHAIR",
            "OTHER"
        )

        // Example radical list (you can replace with real data per section)
        val radicalItems = listOf("⻍", "水", "火", "口", "心", "木", "手", "目", "日", "人")

        // Dynamically add each section
        for (title in sectionTitles) {
            val sectionBinding = RadicalSectionBinding.inflate(layoutInflater, binding.sectionContainer, false)

            sectionBinding.sectionTitle.text = title
            sectionBinding.horizontalRecycler.apply {
                layoutManager = LinearLayoutManager(this@KanjiActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = RadicalAdapter(radicalItems)
            }

            binding.sectionContainer.addView(sectionBinding.root)
        }

        // Back button
        binding.kanjiBackButton.setOnClickListener {
            finish()
        }
    }
}
