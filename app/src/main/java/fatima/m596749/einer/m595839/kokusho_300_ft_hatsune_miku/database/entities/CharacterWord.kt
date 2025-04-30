package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "CharacterWord",
    primaryKeys = ["idCharacter", "wordKanji"],
    foreignKeys = [ ForeignKey(
                        entity = Character::class,
                        parentColumns = ["id"],
                        childColumns = ["idCharacter"],
                        onDelete = ForeignKey.CASCADE) ])
data class CharacterWord(
    val idCharacter: Int,
    val wordKanji: String,
    val wordHiragana: String,
    val wordEnglish: String
)
