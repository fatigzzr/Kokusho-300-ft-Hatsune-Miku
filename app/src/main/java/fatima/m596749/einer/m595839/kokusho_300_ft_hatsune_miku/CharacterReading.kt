package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "CharacterReading",
    primaryKeys = ["idCharacter", "reading"],
    foreignKeys = [ ForeignKey(
                        entity = Character::class,
                        parentColumns = ["id"],
                        childColumns = ["idCharacter"],
                        onDelete = ForeignKey.CASCADE) ])
data class CharacterReading(
    val idCharacter: Int,
    val reading: String
)