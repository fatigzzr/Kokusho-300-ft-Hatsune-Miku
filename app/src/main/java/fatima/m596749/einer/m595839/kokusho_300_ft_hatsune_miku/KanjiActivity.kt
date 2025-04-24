package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.ActivityMainBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.KanjiActivityBinding

class KanjiActivity : AppCompatActivity() {
    // Create Kanji Activity Layout Binder
    private lateinit var kanjiBinding: KanjiActivityBinding

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
    }
}