package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
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
} 