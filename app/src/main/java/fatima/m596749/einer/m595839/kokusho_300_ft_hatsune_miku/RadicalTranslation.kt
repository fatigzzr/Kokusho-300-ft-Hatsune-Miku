package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.util.foreignKeyCheck

@Entity(tableName = "RadicalTranslation",
        primaryKeys = ["idRadical", "word"],
        foreignKeys = [ ForeignKey(
            entity = Radical::class,
            parentColumns = ["id"],
            childColumns = ["idRadical"],
            onDelete = ForeignKey.CASCADE )] )
data class RadicalTranslation(
    val idRadical: Int,
    val word: String
)
