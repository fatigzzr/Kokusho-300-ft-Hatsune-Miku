package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database

import android.graphics.Point
import android.widget.GridLayout.Spec
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game.CharSongQuery
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

    // Get how many found characters an specific song has
    @Query("SELECT COUNT(DISTINCT idCharacter) FROM SongCharacter JOIN Character ON SongCharacter.idCharacter = Character.id WHERE found = True AND idSong = :id")
    fun foundSong(id: Int): Int

    // Get record points of an specific song
    @Query("SELECT points FROM Song where id = :id")
    fun getPoints(id: Int): Int

    // Update record points of an specific song
    @Query("UPDATE Song SET points = :points WHERE id = :id")
    fun updatePoints(id: Int, points: Int)

    // Get the {time, character} for the characters that have been found and are in the specified song
    @Query("SELECT time, character FROM SongCharacter JOIN Character ON SongCharacter.idCharacter = Character.id WHERE SongCharacter.idSong = :id AND found = true")
    fun getSongCharacter(id: Int): List<CharSongQuery>
} 