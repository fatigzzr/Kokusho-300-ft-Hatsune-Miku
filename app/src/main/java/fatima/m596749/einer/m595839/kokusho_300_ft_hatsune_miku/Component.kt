package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Component",
    primaryKeys = ["idCharacter", "idRadical", "position"],
    foreignKeys = [ ForeignKey(
                        entity = Character::class,
                        parentColumns = ["id"],
                        childColumns = ["idCharacter"],
                        onDelete = ForeignKey.CASCADE),
                    ForeignKey(
                        entity = Radical::class,
                        parentColumns = ["id"],
                        childColumns = ["idRadical"],
                        onDelete = ForeignKey.CASCADE) ])
data class Component(
    val idCharacter: Int,
    val idRadical: Int,
    val position: Position
)
