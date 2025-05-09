package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game

import android.animation.Animator
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore.Audio
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.CorrectionInfo
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.lifecycle.lifecycleScope
import androidx.transition.Visibility
import androidx.viewbinding.ViewBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.SongGameFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.GameActivity
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.entities.Song
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.NewRecordFragmentBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.PointsFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.sql.Time


class GameFragment : Fragment() {
    private lateinit var gameBinding: SongGameFragmentBinding
    private lateinit var db: AppDatabase
    private var songId: Int? = null
    private var mediaPlayer: MediaPlayer? = null
    private var beatsRight = ArrayList<Pair<Float, Int>>()
    private var beatsLeft = ArrayList<Pair<Float, Int>>()
    private var beatsRandomKanji = ArrayList<Float>()
    private var beatsYellow = ArrayList<Pair<Float, String>>()
    private var points = 0
    private lateinit var options: List<CharRead>
    private var show = false
    private var correctSide = 0

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
                options = db.kanjiDao().getCharReading()
                val timesChar = db.songDao().getSongCharacter(songId)

                playAudio("song${songId}.mp3")
                loadBeats("beats${songId}.txt", timesChar)
                triggerPerBeat()
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
                fileDescriptor.close()
                prepare()
                start()
            }

        mediaPlayer?.setOnCompletionListener {
            stopAudio()
            checkRecord()
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

    fun loadBeats(filename: String, timesChar:  List<CharSongQuery>) {
        try {
            requireContext().assets.open(filename).use { inputStream ->
                inputStream.bufferedReader().useLines { lines ->
                    var index = 0
                    var range = 0.5
                    var prev2  = ArrayDeque<Pair<String, Float>>()

                    lines.forEach { line ->
                        if (index % 2 == 0) {
                            val beat = line.toFloat()
                            var randomChoice = 11

                            val match = timesChar.find {
                                val time = it.time.toFloat()
                                time != null && time >= beat - range && time <= beat + range
                            }

                            if (match != null) {
                                repeat(2) {
                                    if (prev2.isNotEmpty()) {
                                        val (type, beat) = prev2.removeLast()

                                        when (type) {
                                            "right" -> beatsRight.remove(Pair(beat, 1))
                                            "left" -> beatsLeft.remove(Pair(beat, 1))
                                            "both" -> {
                                                beatsRight.remove(Pair(beat, 1))
                                                beatsLeft.remove(Pair(beat, 1))
                                            }

                                            "kanji" -> {
                                                beatsRight.remove(Pair(beat, 1))
                                                beatsLeft.remove(Pair(beat, 1))
                                                beatsRandomKanji.remove(beat)
                                            }
                                        }
                                    }
                                }

                                beatsRight.add(Pair(match.time.toFloat(), 2))
                                beatsLeft.add(Pair(match.time.toFloat(), 2))
                                beatsYellow.add(Pair(match.time.toFloat(), match.character))
                            }
                            else {
                                // 1-4-> right (0.4), 5-8-> left (0.4), 9-> both (0.1), 10-> both with kanji (0.1)
                                randomChoice = (1..10).random()
                            }

                            when {
                                randomChoice <= 4 -> {
                                    beatsRight.add(Pair(beat, 1))
                                    prev2.addLast("right" to beat)
                                }

                                randomChoice <= 8 -> {
                                    beatsLeft.add(Pair(beat, 1))
                                    prev2.addLast("left" to beat)
                                }

                                randomChoice == 9 -> {
                                    beatsRight.add(Pair(beat, 1))
                                    beatsLeft.add(Pair(beat, 1))
                                    prev2.addLast("both" to beat)
                                }

                                randomChoice == 10 -> {
                                    repeat(2) {
                                        if (prev2.isNotEmpty()) {
                                            val (type, beat) = prev2.removeLast()

                                            when (type) {
                                                "right" -> beatsRight.remove(Pair(beat, 1))
                                                "left" -> beatsLeft.remove(Pair(beat, 1))
                                                "both" -> {
                                                    beatsRight.remove(Pair(beat, 1))
                                                    beatsLeft.remove(Pair(beat, 1))
                                                }

                                                "kanji" -> {
                                                    beatsRight.remove(Pair(beat, 1))
                                                    beatsLeft.remove(Pair(beat, 1))
                                                    beatsRandomKanji.remove(beat)
                                                }
                                            }
                                        }
                                    }

                                    beatsRight.add(Pair(beat, 1))
                                    beatsLeft.add(Pair(beat, 1))
                                    beatsRandomKanji.add(beat)

                                    prev2.addLast("kanji" to beat)
                                }

                                else -> { }
                            }

                            if (prev2.size > 2) {
                                prev2.removeFirst()
                            }
                        }

                        index++
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.e("GameFragment", "Error loading beats: ${e.message}")
        }
    }

    fun triggerPerBeat() {
        CoroutineScope(Dispatchers.IO).launch {
            val startTime = System.currentTimeMillis()

            val right = launch {
                for ((beat, type) in beatsRight) {
                    val delay = (beat * 1000).toLong() - (System.currentTimeMillis() - startTime)

                    if (delay > 0) {
                        delay(delay)
                    }

                    if (show) {
                        continue
                    }

                    withContext(Dispatchers.Main) {
                        fallAnimationRed(type)
                    }

                }
            }

            val left = launch {
                for ((beat, type) in beatsLeft) {
                    val delay = (beat * 1000).toLong() - (System.currentTimeMillis() - startTime)

                    if (delay > 0) {
                        delay(delay)
                    }

                    if (show) {
                        continue
                    }

                    withContext(Dispatchers.Main) {
                        fallAnimationGreen(type)
                    }

                }
            }

            val randomKanji = launch {
                for (beat in beatsRandomKanji) {
                    val delay = (beat * 1000).toLong() - (System.currentTimeMillis() - startTime)

                    if (delay > 0) {
                        delay(delay)
                    }

                    if (show) {
                        continue
                    }

                    withContext(Dispatchers.Main) {
                        showRandomKanji()
                    }
                }
            }

            val yellow = launch {
                for ((beat, character) in beatsYellow) {
                    val delay = (beat * 1000).toLong() - (System.currentTimeMillis() - startTime)

                    if (delay > 0) {
                        delay(delay)
                    }

                    if (show) {
                        continue
                    }

                    withContext(Dispatchers.Main) {
                        showLyric(character)
                    }
                }
            }

            right.join()
            left.join()
            randomKanji.join()
            yellow.join()
        }
    }

    fun createCircle(color: Int, container: FrameLayout): ImageView {
        if (!isAdded) {
            throw IllegalStateException("Fragment is not attached to a context.")
        }

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

    fun fallAnimation(newCircle: ImageView, side: Int) {
        val drumY = gameBinding.redDrum.y
        val drumHeight = gameBinding.redDrum.height
        val targetY = drumY + drumHeight / 4 + 40

        newCircle.setOnClickListener {
            val distance = Math.abs(newCircle.y - targetY)
            val threshold = 100 * resources.displayMetrics.density // 50 dp

            if (distance <= threshold) {
                newCircle.animate().cancel()

                val parent = newCircle.parent as? ViewGroup
                parent?.let {
                    // Disable layout transitions temporarily
                    parent.layoutTransition = null
                    parent.removeView(newCircle)
                    // Re-enable layout transitions
                    parent.layoutTransition = android.animation.LayoutTransition()
                }

                // Update points
                if (show) {
                    if (correctSide == side) {
                        points += 10
                    }
                }
                else {
                    points += 5
                }
                gameBinding.pointsTextView.text = points.toString()
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

                    if (show) {
                        unShowRandomKanji()
                    }
                }

                override fun onAnimationCancel(animation: Animator) { }

                override fun onAnimationRepeat(animation: Animator) { }
            } )
        }

        animator.start()
    }

    // Fall when Beat
    fun fallAnimationRed(type: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val newCircle = if (type == 1) {
                createCircle(R.drawable.circle_red, gameBinding.leftCircleContainer)
            }
            else {
                createCircle(R.drawable.circle_yellow, gameBinding.leftCircleContainer)
            }
            fallAnimation(newCircle, 1)
        }
    }

    fun fallAnimationGreen(type: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            val newCircle = if (type == 1) {
                createCircle(R.drawable.circle_green, gameBinding.rightCircleContainer)
            }
            else {
                createCircle(R.drawable.circle_yellow, gameBinding.rightCircleContainer)
            }
            fallAnimation(newCircle, 2)
        }
    }

    fun showLyric(character: String) {
        val size = options.size
        val randomIndex = (0..size-1).random()
        val randomDrum = (1..2).random()

        when (randomDrum) {
            1 -> {
                gameBinding.redDrumTextView.text = character
                gameBinding.greenDrumTextView.text = options[randomIndex].character
            }
            2 -> {
                gameBinding.redDrumTextView.text = options[randomIndex].character
                gameBinding.greenDrumTextView.text = character
            }
        }

        show = true
        correctSide = randomDrum
    }

    fun showRandomKanji() {
        val size = options.size
        val randomIndex = (0..size-1).random()
        val randomDrum = (1..2).random()

        var randomIndex2: Int
        do {
            randomIndex2 = (0..size - 1).random()
        } while (randomIndex2 == randomIndex)

        gameBinding.readingTextView.visibility = View.VISIBLE
        gameBinding.readingTextView.text = options[randomIndex].reading

        when (randomDrum) {
            1 -> {
                gameBinding.redDrumTextView.text = options[randomIndex].character
                gameBinding.greenDrumTextView.text = options[randomIndex2].character
            }
            2 -> {
                gameBinding.redDrumTextView.text = options[randomIndex2].character
                gameBinding.greenDrumTextView.text = options[randomIndex].character
            }
        }

        show = true
        correctSide = randomDrum
    }

    fun unShowRandomKanji() {
        gameBinding.readingTextView.visibility = View.INVISIBLE
        gameBinding.readingTextView.text = ""
        gameBinding.redDrumTextView.text = ""
        gameBinding.greenDrumTextView.text = ""

        show = false
        correctSide = 0
    }

    fun showPopup(typeLayout: Int) {
        val popupBinding = when(typeLayout) {
            1 -> PointsFragmentBinding.inflate(LayoutInflater.from(requireContext()))
            else -> NewRecordFragmentBinding.inflate(LayoutInflater.from(requireContext()))
        }

        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(requireContext().getDrawable(android.R.color.transparent))

        when(typeLayout) {
            1 -> {
                val binding = popupBinding as PointsFragmentBinding
                binding.pointsTextView.text = points.toString()

                binding.okButton.setOnClickListener {
                    popupWindow.dismiss()

                    parentFragmentManager.beginTransaction()
                        .remove(this)
                        .commit()
                }
            }
            else -> {
                val binding = popupBinding as NewRecordFragmentBinding
                binding.pointsTextView.text = points.toString()

                binding.okButton.setOnClickListener {
                    popupWindow.dismiss()

                    parentFragmentManager.beginTransaction()
                        .remove(this)
                        .commit()
                }
            }
        }

        val wm = requireActivity().window
        val params = wm.attributes
        params.alpha = 0.5f
        wm.attributes = params

        popupWindow.setOnDismissListener {
            params.alpha = 1.0f
            wm.attributes = params

            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }

        popupWindow.showAtLocation(gameBinding.root, Gravity.CENTER, 0, 0)
    }

    fun checkRecord() {
        songId?.let { songId ->
            CoroutineScope(Dispatchers.IO).launch {
                val currPoints = db.songDao().getPoints(songId)

                if (currPoints < points) {
                    db.songDao().updatePoints(songId, points)

                    withContext(Dispatchers.Main) {
                        (requireActivity() as? GameActivity)?.updateListSongs()
                        showPopup(2)
                    }
                }
                else {
                    withContext(Dispatchers.Main) {
                        showPopup(1)
                    }
                }
            }
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