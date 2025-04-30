package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

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