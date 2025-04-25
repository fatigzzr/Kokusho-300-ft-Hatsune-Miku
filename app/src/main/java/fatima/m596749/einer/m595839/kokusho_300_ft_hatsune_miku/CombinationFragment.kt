package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.Visibility
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.CombinationFragmentBinding

class CombinationFragment : Fragment() {
    private lateinit var binding: CombinationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CombinationFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    fun updateContent(translation: String, kanji: String) {
        binding.combinationBackButton.visibility = View.VISIBLE
        binding.translationTextView.text = translation
        binding.completeKanjiTextView.text = kanji
    }
}