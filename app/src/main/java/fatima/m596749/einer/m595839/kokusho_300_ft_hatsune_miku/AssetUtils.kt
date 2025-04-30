package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.content.Context


object AssetUtils {
    fun loadAssetBytes(context: Context, fileName: String): ByteArray {
        return context.assets.open(fileName).use { inputStream ->
            inputStream.readBytes()
        }
    }
}