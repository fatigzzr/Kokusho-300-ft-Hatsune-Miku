package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

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
                        onDelete = ForeignKey.CASCADE) ],
    indices = [
        Index(value = ["idCharacter"]),
        Index(value = ["idRadical"])
    ]
)
data class Component(
    val idCharacter: Int,
    val idRadical: Int,
    val position: Position
)
