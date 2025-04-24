package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Create Main Activity Layout Binder
    private lateinit var homeBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Main Activity Binder
        homeBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Click on Laboratorio de Kanji
        homeBinding.kanjiButton.setOnClickListener {
            // Start (send to) Kanji Activity
            val intent: Intent = Intent(this, KanjiActivity::class.java)
            startActivity(intent)
        }

        // Click on Aprende con Ritmo
        homeBinding.gameButton.setOnClickListener {
            // Start Game Activity
            val intent: Intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}