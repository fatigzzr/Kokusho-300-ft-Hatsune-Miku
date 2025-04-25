package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.ActivityMainBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.CombinationFragmentBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.KanjiActivityBinding

class KanjiActivity : AppCompatActivity() {
    // Create Kanji Activity Layout Binder
    private lateinit var kanjiBinding: KanjiActivityBinding
    // Radical Items Recycler View Adapter
    private lateinit var radicalAdapter: RadicalAdapter

    // TEMP CAMBIARLO?????? - Radical Items List
    private lateinit var radicals: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize Kanji Activity Binder
        kanjiBinding = KanjiActivityBinding.inflate(layoutInflater)
        setContentView(kanjiBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.kanjiActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ESTO ES TEMPORAL !!!!!!!!!!
        radicals = mutableListOf("人", "⻍", "⺾", "女")

        // If the fragment wasn't load (Activity was just created, there´s no instances)
        if (savedInstanceState == null) {
            val fragment = CombinationFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.mixKanjiFragmentContainer, fragment)
                .commit()
        }

        // Back Button - Go back to Main Activity
        kanjiBinding.kanjiBackButton.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Initialize Radical Recycler View Adapter as an Horizontal list with a click listener
        radicalAdapter = RadicalAdapter(radicals) { position ->
            val fragment = supportFragmentManager.findFragmentById(R.id.mixKanjiFragmentContainer) as CombinationFragment
            fragment.updateContent("se supone que significado", radicals[position])
        }
        kanjiBinding.radicalRecyclerView.adapter = radicalAdapter
        kanjiBinding.radicalRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }
}