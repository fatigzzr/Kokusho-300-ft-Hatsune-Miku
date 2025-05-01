package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game

import android.animation.Animator
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore.Audio
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.SongGameFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import kotlinx.coroutines.delay


class GameFragment : Fragment() {
    private lateinit var gameBinding: SongGameFragmentBinding
    private lateinit var db: AppDatabase
    private var songId: Int? = null
    private var mediaPlayer: MediaPlayer? = null
    private var beatsRight = ArrayList<Float>()
    private var beatsLeft = ArrayList<Float>()
    private var points = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        songId = arguments?.getInt("id")
        db = AppDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gameBinding = SongGameFragmentBinding.inflate(inflater, container, false)

        songId?.let { songId ->
            CoroutineScope(Dispatchers.IO).launch {
                playAudio("song${songId}.mp3")
                val beats = loadBeats("beats${songId}.txt")
                triggerPerBeat(beatsLeft, beatsRight)
            }
        }

        // Inflate the layout for this fragment
        return gameBinding.root
    }

    // When the fragment is paused (eg. go back to prev view)
    override fun onPause() {
        super.onPause()
        stopAudio()
    }

    // When the fragment is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        stopAudio()
    }

    // Start playing the Song
    fun playAudio(filename: String) {
        val fileDescriptor = requireContext().assets.openFd(filename)

        mediaPlayer = MediaPlayer()
            .apply {
                setDataSource(fileDescriptor.fileDescriptor, fileDescriptor.startOffset, fileDescriptor.length)
                prepare()
                start()
            }

        mediaPlayer?.setOnCompletionListener {
            stopAudio()
        }
    }

    // Stop playing the Song
    fun stopAudio() {
        mediaPlayer?.let {
            if(it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    fun loadBeats(filename: String) {
        try {
            val inputStream = requireContext().assets.open(filename)
            inputStream.bufferedReader().useLines { lines ->
                var index = 0

                lines.forEach { line ->
                    if (index % 2 == 0) {
                        val beat = line.toFloat()
                        // 1-4-> right (0.4), 5-8-> left (0.4), 9-10-> both (0.2)
                        val randomChoice = (1..10).random()

                        when {
                            randomChoice <= 4 -> beatsRight.add(beat)
                            randomChoice <= 8 -> beatsLeft.add(beat)
                            else -> {
                                beatsRight.add(beat)
                                beatsLeft.add(beat)
                            }
                        }
                    }

                    index++
                }
            }
        }
        catch (e: Exception) {
            Log.e("GameFragment", "Error loading beats: ${e.message}")
        }
    }

    fun triggerPerBeat(beatsRight: List<Float>, beatsLeft: List<Float>) {
        CoroutineScope(Dispatchers.IO).launch {
            val startTime = System.currentTimeMillis()

            val right = launch {
                for (beat in beatsRight) {
                    val delay = (beat * 1000).toLong() - (System.currentTimeMillis() - startTime)

                    if (delay > 0) {
                        delay(delay)
                    }

                    fallAnimationRed()
                }
            }

            val left = launch {
                for (beat in beatsLeft) {
                    val delay = (beat * 1000).toLong() - (System.currentTimeMillis() - startTime)

                    if (delay > 0) {
                        delay(delay)
                    }

                    fallAnimationGreen()
                }
            }

            right.join()
            left.join()
        }
    }

    fun createCircle(color: Int, container: FrameLayout): ImageView {
        val newCircle = ImageView(requireContext()).apply {
            layoutParams = FrameLayout.LayoutParams(
                325, 325
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }

            setImageResource(color)
            id = View.generateViewId()
        }

        container.addView(newCircle)

        return newCircle
    }

    fun fallAnimation(newCircle: ImageView) {
        val drumY = gameBinding.redDrum.y
        val drumHeight = gameBinding.redDrum.height
        val targetY = drumY + drumHeight / 4 + 40

        newCircle.setOnClickListener {
            val distance = Math.abs(newCircle.y - targetY)
            val threshold = 100 * resources.displayMetrics.density // 50 dp

            if (distance <= threshold) {
                // Update points
                points += 5
                gameBinding.pointsTextView.text = points.toString()

                newCircle.animate().cancel()

                val parent = newCircle.parent as? ViewGroup
                parent?.let {
                    // Disable layout transitions temporarily
                    parent.layoutTransition = null
                    parent.removeView(newCircle)
                    // Re-enable layout transitions
                    parent.layoutTransition = android.animation.LayoutTransition()
                }
            }
        }

        val animator = ObjectAnimator.ofFloat(
            newCircle,
            "translationY",
            0f,
            targetY
        ).apply {
            duration = 2000

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) { }

                override fun onAnimationEnd(animation: Animator) {
                    val parent = newCircle.parent as? ViewGroup
                    parent?.removeView(newCircle)
                }

                override fun onAnimationCancel(animation: Animator) { }

                override fun onAnimationRepeat(animation: Animator) { }
            } )
        }

        animator.start()
    }

    // Fall when Beat
    fun fallAnimationRed() {
        CoroutineScope(Dispatchers.Main).launch {
            val newCircle = createCircle(R.drawable.circle_red, gameBinding.leftCircleContainer)
            fallAnimation(newCircle)
        }
    }

    fun fallAnimationGreen() {
        CoroutineScope(Dispatchers.Main).launch {
            val newCircle = createCircle(R.drawable.circle_green, gameBinding.rightCircleContainer)
            fallAnimation(newCircle)
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