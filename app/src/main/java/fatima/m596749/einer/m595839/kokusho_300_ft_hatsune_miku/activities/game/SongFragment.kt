package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.SongGameFragmentBinding

// Song list item view
class SongFragment : Fragment() {
    private lateinit var binding: SongGameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SongGameFragmentBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        return binding.root
    }
}