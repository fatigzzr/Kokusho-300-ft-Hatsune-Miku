package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game.Communicator
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game.GameFragment
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game.SongAdapter
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.GameActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameActivity : AppCompatActivity(), Communicator {
    // Create Game Activity Layout Binder
    private lateinit var gameBinding: GameActivityBinding
    // Song Items Recycler View Adapter
    private lateinit var songAdapter: SongAdapter
    // Songs List
    private var songsListId: ArrayList<Int> = ArrayList()
    // Type List
    private var typesList: ArrayList<Int>  = ArrayList()
    // Database
    private lateinit var db: AppDatabase

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

        db = AppDatabase.getDatabase(applicationContext)

        backButton()
        updateListSongs()
        initializeRecyclerView()
    }

    // Back Button - Go back to Home
    fun backButton() {
        gameBinding.gameBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    // Update the list of Songs according to the Songs in the Database
    fun updateListSongs() {
        db.songDao().getSongsId().observe(this@GameActivity) { ids ->
            songsListId.clear()
            typesList.clear()

            CoroutineScope(Dispatchers.IO).launch {
                for (id in ids) {
                    // Add id
                    songsListId.add(id)

                    // Add type
                    val countUnlocked = db.songDao().foundSong(id)

                    if (countUnlocked >= 1) {
                        typesList.add(0)
                    } else {
                        typesList.add(1)
                    }
                }

                withContext(Dispatchers.Main) {
                    songAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    // Initialize Song Recycler View Adapter as Vertical list with a click listener
    fun initializeRecyclerView() {
        songAdapter = SongAdapter(this, songsListId, db, typesList) { position ->
            passSongId(songsListId[position])
        }
        gameBinding.songRecyclerView.adapter = songAdapter
        gameBinding.songRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    // Pass the Song ID
    override fun passSongId(id: Int) {
        val fragment = GameFragment.newInstance(id)

        supportFragmentManager.beginTransaction()
            .replace(R.id.gameFragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}