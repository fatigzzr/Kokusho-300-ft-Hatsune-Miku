package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database

import android.widget.GridLayout.Spec
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game.SongInfo
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Song
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.SongCharacter

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongBatch(songs: List<Song>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongCharacterBatch(songCharacters: List<SongCharacter>)

    @Transaction
    suspend fun insertSongCharactersWithTransaction(songCharacters: List<SongCharacter>) {
        insertSongCharacterBatch(songCharacters)
    }

    // Get all Songs (not large data)
    @Query("SELECT id FROM Song")
    fun getSongsId() : LiveData<List<Int>>

    // Get short data from specific Song
    @Query("SELECT title, points FROM Song WHERE id= :id")
    fun getSongInfo(id: Int): SongInfo

    @Query("SELECT COUNT(*) FROM SongCharacter JOIN Character ON SongCharacter.idSong = Character.id WHERE found = True AND idSong = :id")
    fun foundSong(id: Int): Int
} 