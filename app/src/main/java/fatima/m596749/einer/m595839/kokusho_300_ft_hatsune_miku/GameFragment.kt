package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.SongGameFragmentBinding

class GameFragment : Fragment() {
    private lateinit var gameBinding: SongGameFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val songId = arguments?.getInt("id")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gameBinding = SongGameFragmentBinding.inflate(inflater, container, false)

        gameBinding.circleImageView.post {
            startFallingAnimation()
        }

        // Inflate the layout for this fragment
        return gameBinding.root
    }

    fun startFallingAnimation() {
        val redDrumY = gameBinding.redDrum.y
        val redDrumHeight = gameBinding.redDrum.height

        val targetY = redDrumY + redDrumHeight/4

        ObjectAnimator.ofFloat(
            gameBinding.circleImageView,
            "translationY",
            0f,
            targetY
        ).apply {
            duration = 2000

            addUpdateListener { animation ->
                val currentY = animation.animatedValue as Float

                if ( currentY >= targetY) {
                    animation.cancel()
                    (gameBinding.root as ViewGroup).removeView(gameBinding.circleImageView)
                }
            }

            start()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int): GameFragment {
            val fragment = GameFragment()
            val arguments = Bundle()
            arguments.putInt("id", id)
            fragment.arguments = arguments

            return fragment
        }
    }
}