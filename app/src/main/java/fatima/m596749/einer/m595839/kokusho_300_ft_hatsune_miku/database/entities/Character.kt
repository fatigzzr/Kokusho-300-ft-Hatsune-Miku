package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Character")
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val character: String,
    val meaning: String,
    val found: Boolean
)
