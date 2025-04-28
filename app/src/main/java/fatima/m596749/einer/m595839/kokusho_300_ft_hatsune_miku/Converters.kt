package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.TypeConverter
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class Converters {
    @TypeConverter
    fun fromPosition(position: Position): Int {
        return position.ordinal
    }

    @TypeConverter
    fun toPosition(ordinal: Int): Position {
        return Position.values()[ordinal]
    }

    @TypeConverter
    fun fromDuration(duration: Duration?): Long? {
        return duration?.inWholeMilliseconds
    }

    @TypeConverter
    fun toDuration(milliseconds: Long?): Duration? {
        return milliseconds?.toDuration(DurationUnit.MILLISECONDS)
    }
}