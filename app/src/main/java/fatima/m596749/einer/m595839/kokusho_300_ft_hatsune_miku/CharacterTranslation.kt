package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "CharacterTranslation",
    primaryKeys = ["idCharacter", "word"],
    foreignKeys = [ ForeignKey(
                        entity = Character::class,
                        parentColumns = ["id"],
                        childColumns = ["idCharacter"],
                        onDelete = ForeignKey.CASCADE) ])
data class CharacterTranslation(
    val idCharacter: Int,
    val word: String
)
