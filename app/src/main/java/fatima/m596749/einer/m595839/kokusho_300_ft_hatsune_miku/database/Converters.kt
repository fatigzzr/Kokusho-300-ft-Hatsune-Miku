package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Duration

class Converters {
    @TypeConverter
    fun fromPosition(position: Position): Int {
        return position.ordinal
    }

    @TypeConverter
    fun toPosition(ordinal: Int): Position {
        return Position.values()[ordinal]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromDuration(duration: Duration?): Long? {
        return duration?.toMillis()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDuration(milliseconds: Long?): Duration? {
        return milliseconds?.let { Duration.ofMillis(it) }
    }
}