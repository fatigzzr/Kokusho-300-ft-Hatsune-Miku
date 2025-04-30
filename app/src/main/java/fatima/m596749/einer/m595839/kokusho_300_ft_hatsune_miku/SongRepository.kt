package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class SongRepository(private val context: Context, private val songDao: SongDao) {

    fun provideInitialSongs(): List<Song> {
        return listOf(
            Song(
                id = 1,
                title = "World is Mine",
                artist = "Supercell",
                image = AssetUtils.loadAssetBytes(context, "cover1.jpg"),
                audio = AssetUtils.loadAssetBytes(context, "song1.mp3"),
                points = 0
            ),
            Song(
                id = 2,
                title = "Miku",
                artist = "Amanaguchi",
                image = AssetUtils.loadAssetBytes(context, "cover2.jpg"),
                audio = AssetUtils.loadAssetBytes(context, "song2.mp3"),
                points = 0
            )
        )
    }

    suspend fun insertInitialSongs() {
        val songs = provideInitialSongs()
        songDao.insertSongBatch(songs)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val songCharacters = listOf(
        SongCharacter(1, 21, 30),
        SongCharacter(1, 22, 36),
        SongCharacter(1, 23, 42),
        SongCharacter(1, 21, 43),
        SongCharacter(1, 23, 45),
        SongCharacter(1, 74, 46),
        SongCharacter(1, 15, 50),
        SongCharacter(1, 21, 70),
        SongCharacter(1, 86, 106),
        SongCharacter(1, 36, 117),
        SongCharacter(1, 52, 122),
        SongCharacter(1, 4, 194),
        SongCharacter(1, 85, 200),
        SongCharacter(1, 21, 210),
        SongCharacter(1, 51, 217),

        SongCharacter(2, 61, 25),
        SongCharacter(2, 39, 27),
        SongCharacter(2, 61, 37),
        SongCharacter(2, 61, 41),
        SongCharacter(2, 61, 45),
        SongCharacter(2, 61, 48),
        SongCharacter(2, 61, 56),
        SongCharacter(2, 61, 67),
        SongCharacter(2, 61, 71),
        SongCharacter(2, 61, 75),
        SongCharacter(2, 61, 79),
        SongCharacter(2, 52, 85),
        SongCharacter(2, 16, 89),
        SongCharacter(2, 16, 91),
        SongCharacter(2, 61, 116),
        SongCharacter(2, 61, 131),
        SongCharacter(2, 61, 143),
        SongCharacter(2, 61, 146),
        SongCharacter(2, 61, 150),
        SongCharacter(2, 61, 154),
        SongCharacter(2, 85, 177),
        SongCharacter(2, 61, 184),
        SongCharacter(2, 22, 188),
        SongCharacter(2, 100, 190),
        SongCharacter(2, 61, 192),
        SongCharacter(2, 60, 194)
    )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertInitialSongCharacters() {
        songDao.insertSongCharactersWithTransaction(songCharacters)
    }
}
