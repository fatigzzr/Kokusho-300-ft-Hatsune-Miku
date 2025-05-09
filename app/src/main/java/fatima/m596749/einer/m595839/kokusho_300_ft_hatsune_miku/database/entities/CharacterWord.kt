package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "CharacterWord",
    primaryKeys = ["idCharacter", "word_kanji"],
    foreignKeys = [ ForeignKey(
                        entity = Character::class,
                        parentColumns = ["id"],
                        childColumns = ["idCharacter"],
                        onDelete = ForeignKey.CASCADE) ])
data class CharacterWord(
    val idCharacter: Int,
    @ColumnInfo(name = "word_hiragana") val wordHiragana: String,
    @ColumnInfo(name = "word_kanji") val wordKanji: String,
    @ColumnInfo(name = "word_english") val wordEnglish: String
)
