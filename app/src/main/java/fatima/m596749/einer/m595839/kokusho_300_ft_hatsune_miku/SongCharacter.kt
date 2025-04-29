package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.Duration

@Entity(tableName = "SongCharacter",
    primaryKeys = ["idSong", "idCharacter", "time"],
    foreignKeys = [
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["idSong"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Character::class,
            parentColumns = ["id"],
            childColumns = ["idCharacter"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SongCharacter(
    val idSong: Int,
    val idCharacter: Int,
    val time: Duration,
    val unlocked: Boolean
)
