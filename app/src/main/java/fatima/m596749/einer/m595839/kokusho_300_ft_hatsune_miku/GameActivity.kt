package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.GameActivityBinding

class GameActivity : AppCompatActivity(), Communicator {
    // Create Game Activity Layout Binder
    private lateinit var gameBinding: GameActivityBinding
    // Song Items Recycler View Adapter
    private lateinit var songAdapter: SongAdapter

    // TEMP !!!!!!!!!!!!!!!!!!
    private lateinit var songs: List<String>

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

        // TEMP !!!!!!!!!!!!!!!!!!
        songs = mutableListOf("CANCION 1")

        // Back Button - Go back to Home
        gameBinding.gameBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Initialize Song Recycler View Adapter as Vertical list with a click listener
        songAdapter = SongAdapter(songs) { position ->
            passSongId(position)
        }
        gameBinding.songRecyclerView.adapter = songAdapter
        gameBinding.songRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun passSongId(id: Int) {
        val fragment = GameFragment.newInstance(id)

        supportFragmentManager.beginTransaction()
            .replace(R.id.gameFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}