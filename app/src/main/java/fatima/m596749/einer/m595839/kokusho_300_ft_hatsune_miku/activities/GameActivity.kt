package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.GameActivityBinding

class GameActivity : AppCompatActivity() {
    // Create Game Activity Layout Binder
    private lateinit var gameBinding: GameActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize Game Activity Binder
        gameBinding = GameActivityBinding.inflate(layoutInflater)
        setContentView(gameBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gameActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}