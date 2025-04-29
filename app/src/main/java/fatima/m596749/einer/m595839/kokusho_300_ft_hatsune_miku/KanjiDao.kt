package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface KanjiDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacterBatch(characters: List<Character>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacterWordBatch(words: List<CharacterWord>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRadicalBatch(radicals: List<Radical>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacterReadingBatch(readings: List<CharacterReading>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComponentBatch(components: List<Component>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongBatch(songs: List<Song>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSongCharacterBatch(SongCharacters: List<SongCharacter>)
} 