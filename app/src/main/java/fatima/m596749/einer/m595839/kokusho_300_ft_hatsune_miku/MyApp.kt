package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase.Companion.characters
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase.Companion.components
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase.Companion.radicals
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase.Companion.readings
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase.Companion.words
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.SongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp : Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        val db = AppDatabase.getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {
            if (db.kanjiDao().getAllComponents().isEmpty()) {
                db.kanjiDao().insertCharacterBatch(characters)
                db.kanjiDao().insertCharacterWordBatch(words)
                db.kanjiDao().insertRadicalBatch(radicals)
                db.kanjiDao().insertCharacterReadingBatch(readings)
                db.kanjiDao().insertComponentBatch(components)

                val songRepo = SongRepository(this@MyApp, db.songDao())
                songRepo.insertInitialSongs()
                songRepo.insertInitialSongCharacters()
            }
        }

        Log.d("MyApp", "Custom Application started and DB initialized")
    }
}