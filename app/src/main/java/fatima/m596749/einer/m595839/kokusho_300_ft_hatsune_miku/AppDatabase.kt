package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Character::class, CharacterTranslation::class, Component::class, Radical::class, RadicalTranslation::class, Song::class, SongCharacter::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun kanjiDao() : KanjiDao

    abstract fun gameDao() : GameDao
}