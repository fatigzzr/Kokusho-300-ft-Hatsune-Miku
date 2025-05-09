package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Song")
data class Song(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val artist: String,
    val image: ByteArray,
    val audio: ByteArray,
    var points: Int
)
