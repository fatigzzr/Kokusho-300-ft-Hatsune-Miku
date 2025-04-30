package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Character
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.CharacterReading
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.CharacterWord
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Component
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Radical

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
} 