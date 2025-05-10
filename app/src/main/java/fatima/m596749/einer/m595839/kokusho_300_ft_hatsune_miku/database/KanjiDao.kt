package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.KanjiActivity
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game.CharRead
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

    @Query("SELECT * FROM Character WHERE id = :id")
    suspend fun getCharacterById(id: Int): Character

    @Query("SELECT * FROM CharacterWord WHERE idCharacter = :id")
    suspend fun getWordsByCharacterId(id: Int): List<CharacterWord>

    // Get the {character, reading} from the characters that have been found (get only 1 reading)
    @Query("""
        SELECT Character.character AS character, CharacterReading.reading AS reading 
        FROM Character 
        JOIN CharacterReading ON Character.id = CharacterReading.idCharacter 
        WHERE Character.found = 1 AND CharacterReading.reading = (
            SELECT MIN(reading) 
            FROM CharacterReading 
            WHERE CharacterReading.idCharacter = Character.id
        )
    """)
    fun getCharReading() : List<CharRead>

    @Query("SELECT DISTINCT Radical.id, Radical.radical, Radical.meaning, Component.position FROM Radical INNER JOIN Component ON Radical.id = Component.idRadical")
    fun getAllRadicalsWithPosition(): List<KanjiActivity.RadicalWithPosition>

    @Query("SELECT * FROM Component")
    suspend fun getAllComponents(): List<Component>

    @Query("UPDATE Character SET found = 1 WHERE id = :characterId")
    suspend fun markAsFound(characterId: Int)

    data class FoundCountResult(
        val foundCount: Int,
        val totalCount: Int
    )

    @Query("SELECT COUNT(CASE WHEN found = 1 THEN 1 END) AS foundCount, COUNT(*) AS totalCount FROM Character")
    fun getFoundAndTotalCounts(): LiveData<FoundCountResult>
}