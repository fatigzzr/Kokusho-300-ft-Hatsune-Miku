package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.game

import android.animation.Animator
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.database.AppDatabase
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.SongGameFragmentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.R
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.activities.GameActivity
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.ExitGameFragmentBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.NewRecordFragmentBinding
import fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku.databinding.PointsFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

// Fragment for the rhythm game of an specific song
class GameFragment : Fragment() {
    // Initialize variables
    private lateinit var gameBinding: SongGameFragmentBinding // Layout binding
    private lateinit var db: AppDatabase // Database for the queries
    private var songId: Int? = null // Song id of the song being played
    private var mediaPlayer: MediaPlayer? = null // Media Player used to play the song audio
    private var beatsRight = ArrayList<Pair<Float, Int>>() // List of beats appearing on the right side {time, type = 1 (green) or 2 (yellow)}
    private var beatsLeft = ArrayList<Pair<Float, Int>>() // List of beats appearing on the left side {time, type = 1 (red) or 2 (yellow)}
    private var beatsRandomKanji = ArrayList<Float>() // List of beats appearing with a random Kanjis {time}
    private var beatsYellow = ArrayList<Pair<Float, String>>() // List of beats appearing with the lyric Kanji {time, character}
    private var points = 0 // Points of the current game
    private lateinit var options: List<CharRead> // Options for the select random Kanji (only Kanjis that have been found)
    private var show = false // Know if circles can keep falling (true -> pause, random or yellow event is happening)
    private var correctSide = 0 // Correct drum side

    // Fragment is being created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the Song Id from the Game Activity
        songId = arguments?.getInt("id")
        // Get the app DB
        db = AppDatabase.getDatabase(requireContext())

        // Get a callback object for the back icon being pressed event, enabling it and saying that a the Exit Popup will show
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitPopup()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    // Fragment View is being created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the Game Fragment layout
        gameBinding = SongGameFragmentBinding.inflate(inflater, container, false)

        // If the fragment saved the song Id
        songId?.let { songId ->
            CoroutineScope(Dispatchers.IO).launch {
                // Get the options (for random Kanji) from the DB
                options = db.kanjiDao().getCharReading()
                // Get characters in the song (time, character) from the DB
                val timesChar = db.songDao().getSongCharacter(songId)

                // Start playing the song audio (preloaded)
                playAudio("song${songId}.mp3")

                // Load the beats (with a preloaded beats file and the characters in the song and that have been found)
                loadBeats("beats${songId}.txt", timesChar)

                // Start triggering each beat
                triggerPerBeat()
            }
        }

        // Inflate the layout for this fragment
        return gameBinding.root
    }

    // When the fragment is paused (eg. go back to prev view)
    override fun onPause() {
        super.onPause()

        // Stop the song audio
        stopAudio()
    }

    // When the fragment is destroyed
    override fun onDestroyView() {
        super.onDestroyView()

        // Stop the audio
        stopAudio()
    }

    // Start playing the Song
    fun playAudio(filename: String) {
        // File Descriptor of the audio file
        val fileDescriptor = requireContext().assets.openFd(filename)

        // Create a media player and set the data as the audio file descriptor
        mediaPlayer = MediaPlayer()
            .apply {
                setDataSource(fileDescriptor.fileDescriptor, fileDescriptor.startOffset, fileDescriptor.length)
                fileDescriptor.close()
                prepare()
                start()
            }

        // When the media player finish playing the song audio, stop the audio
        mediaPlayer?.setOnCompletionListener {
            stopAudio()
            checkRecord()
        }
    }

    // Stop playing the Song
    fun stopAudio() {
        // If there is a media player playing, stop it and release it
        mediaPlayer?.let {
            if(it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
        }
    }

    // Load the beats of the song to the corresponding list (right, left, random Kanji, yellow)
    fun loadBeats(filename: String, timesChar:  List<CharSongQuery>) {
        try {
            // Get the file that has the beats of the song
            requireContext().assets.open(filename).use { inputStream ->
                // Get the file's lines
                inputStream.bufferedReader().useLines { lines ->
                    var index = 0 // Know the number of the line the loop is currently
                    var range = 1 // Range of 1s
                    var prev2  = ArrayDeque<Pair<String, Float>>() // Save the last 2 inserted beats {type, time}

                    // Process each line (each beat)
                    lines.forEach { line ->
                        // Skip 1 line between lines (every 2 lines) (to not get very close beats, and reduce game's difficulty)
                        if (index % 2 == 0) {
                            val beat = line.toFloat() // Convert the linea (beat) to float
                            var randomChoice = 11 // Default random number (use when it's the yellow circle case)

                            // If there's a match between the beat time and the lyric Kanji time (range +-1s -> no func requirement)
                            val match = timesChar.find {
                                val time = it.time.toFloat()
                                time != null && time >= beat - range && time <= beat + range
                            }

                            // If there was a match -> yellow circle case
                            if (match != null) {
                                // Remove the last 2 beats circles (to leave time for the user to answer)
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

                                // Add beat to the right, left {time, type yellow = 2} and yellow list {time, yellow circle character} (Because it needs circles on both sides)
                                beatsRight.add(Pair(match.time.toFloat(), 2))
                                beatsLeft.add(Pair(match.time.toFloat(), 2))
                                beatsYellow.add(Pair(match.time.toFloat(), match.character))
                            }

                            // If it's not yellow circle case
                            else {
                                // Choose a random scenario
                                // 1-4-> right (40%), 5-8-> left (40%), 9-> both (10%), 10-> both with kanji (10%)
                                randomChoice = (1..10).random()
                            }

                            // Add the corresponding circles to the chosen scenario
                            when {
                                // Right circle scenario
                                randomChoice <= 4 -> {
                                    beatsRight.add(Pair(beat, 1))
                                    prev2.addLast("right" to beat)
                                }

                                // Left circle scenario
                                randomChoice <= 8 -> {
                                    beatsLeft.add(Pair(beat, 1))
                                    prev2.addLast("left" to beat)
                                }

                                // Both circles scenario
                                randomChoice == 9 -> {
                                    beatsRight.add(Pair(beat, 1))
                                    beatsLeft.add(Pair(beat, 1))
                                    prev2.addLast("both" to beat)
                                }

                                // Random Kanji and both circles scenario
                                randomChoice == 10 -> {
                                    // Remove last 2 beats (to leave time for the user to answer)
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

                                    // Add right, left (to have 2 circles) and random beat (to have the kanji)
                                    beatsRight.add(Pair(beat, 1))
                                    beatsLeft.add(Pair(beat, 1))
                                    beatsRandomKanji.add(beat)

                                    prev2.addLast("kanji" to beat)
                                }

                                // Case of 11 -> if yellow circle case happened on this line (beat)
                                else -> { }
                            }

                            // Keep only the 2 last beats
                            if (prev2.size > 2) {
                                prev2.removeFirst()
                            }
                        }

                        // Increment indexing (representing being on the next line)
                        index++
                    }
                }
            }
        }
        catch (e: Exception) {
            Log.e("GameFragment", "Error loading beats: ${e.message}") // Any error of the load beats
        }
    }

    // Asynchronous - Trigger the mechanism for each type of circle scenario at the corresponding time
    fun triggerPerBeat() {
        CoroutineScope(Dispatchers.IO).launch {
            // Get the current time (when the song started)
            val startTime = System.currentTimeMillis()

            // For each right side beat
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
                        if (isAdded) {
                            fallAnimationRed(type)
                        }
                    }

                }
            }

            // For each left side beat
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
                        if (isAdded) {
                            fallAnimationGreen(type)
                        }
                    }

                }
            }

            // For each random Kanji and both side beat
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
                        if (isAdded) {
                            showRandomKanji()
                        }
                    }
                }
            }

            // For each yellow circle beat
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
                        if (isAdded) {
                            showLyric(character)
                        }
                    }
                }
            }

            // Wait for all the threads to finish
            right.join()
            left.join()
            randomKanji.join()
            yellow.join()
        }
    }

    // Create the circle view:{green, red, yellow} container:{right or left container}
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

    // Start the fall animation and the event listeners
    fun fallAnimation(newCircle: ImageView, side: Int) {
        val drumY = gameBinding.redDrum.y // Drum Y position
        val drumHeight = gameBinding.redDrum.height // Drum height
        val targetY = drumY + drumHeight / 4 + 40 // Goal for the Y position (when the animation stops)

        // When the circle is clicked -> ex. Update points
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

        // Animation config
        val animator = ObjectAnimator.ofFloat(
            newCircle,
            "translationY",
            0f,
            targetY
        ).apply {
            duration = 2000

            // Animation events
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) { }

                // Reached target Y
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

        // Start animation
        animator.start()
    }

    // Fall when Beat for red circles (initialize the process)
    fun fallAnimationRed(type: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            // 1. Create circle
            val newCircle = if (type == 1) {
                createCircle(R.drawable.circle_red, gameBinding.leftCircleContainer)
            }
            else {
                createCircle(R.drawable.circle_yellow, gameBinding.leftCircleContainer)
            }
            // 2. Falling Animation
            fallAnimation(newCircle, 1)
        }
    }

    // Fall when Beat for green circles (initialize the process)
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

    // Initialize process for circles related to lyrics
    fun showLyric(character: String) {
        // Get a random drum (choose the drum where the correct answer will be)
        val size = options.size
        val randomDrum = (1..2).random()

        // In case there's only 1 discovered char (Default wrong char)
        if (size == 1) {
            setCharacters(randomDrum, character, "女")
        }
        // More characters aside of the lyric char
        else {
            // Get a different random character
            var randomIndex : Int
            do {
                randomIndex = (0..size-1).random()
            } while (options[randomIndex].character == character)

            setCharacters(randomDrum, character, options[randomIndex].character)
        }

        show = true
        correctSide = randomDrum
    }

    // Update the text on the drums
    fun setCharacters(randomDrum: Int, correct: String, incorrect: String) {
        when (randomDrum) {
            1 -> {
                gameBinding.redDrumTextView.text = correct
                gameBinding.greenDrumTextView.text = incorrect
            }
            2 -> {
                gameBinding.redDrumTextView.text = incorrect
                gameBinding.greenDrumTextView.text = correct
            }
        }
    }

    // Initialize process for circles with random Kanjis
    fun showRandomKanji() {
        val size = options.size
        val randomIndex = (0..size-1).random() // Choose random kanji (correct option)
        val randomDrum = (1..2).random() // Choose random drum (drum where the correct kanji will appear)

        // Show the reading for the correct Kanji
        gameBinding.readingTextView.visibility = View.VISIBLE
        gameBinding.readingTextView.text = options[randomIndex].reading

        // If there's only 1 discovered Kanji: Get a different random Kanji (incorrect option)
        if (size == 1) {
            if (options[randomIndex].character != "女") {
                setCharacters(randomDrum, options[randomIndex].character, "女") // Default incorrect Kanji
            }
            else {
                setCharacters(randomDrum, options[randomIndex].character, "人") // 2nd Default incorrect Kanji
            }
        }

        // More than 1 found Kanji: Get a different random Kanji (incorrect option)
        else {
            var randomIndex2: Int
            do {
                randomIndex2 = (0..size - 1).random()
            } while (randomIndex2 == randomIndex)

            setCharacters(randomDrum, options[randomIndex].character, options[randomIndex2].character)
        }

        show = true
        correctSide = randomDrum
    }

    // Restart the view (remove the text from the lyric or random Kanji)
    fun unShowRandomKanji() {
        gameBinding.readingTextView.visibility = View.INVISIBLE
        gameBinding.readingTextView.text = ""
        gameBinding.redDrumTextView.text = ""
        gameBinding.greenDrumTextView.text = ""

        show = false
        correctSide = 0
    }

    // Show PopUp: Points gained at the end of the game
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
                .remove(this@GameFragment)
                .commit()
        }

        popupWindow.showAtLocation(gameBinding.root, Gravity.CENTER, 0, 0)
    }

    // Check if the points gained in the current game are greater than the current record: update and show popup (different for new record or normal points)
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

    // Show popup when back button is clicked (will loss points)
    fun showExitPopup() {
        val popupBinding = ExitGameFragmentBinding.inflate(LayoutInflater.from(requireContext()))

        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(requireContext().getDrawable(android.R.color.transparent))


        popupBinding.yesButton.setOnClickListener {
            popupWindow.dismiss()
            parentFragmentManager.beginTransaction()
                .remove(this@GameFragment)
                .commit()
        }

        popupBinding.cancelButton.setOnClickListener {
            popupWindow.dismiss()
        }

        val wm = requireActivity().window
        val params = wm.attributes
        params.alpha = 0.5f
        wm.attributes = params

        popupWindow.setOnDismissListener {
            params.alpha = 1.0f
            wm.attributes = params
        }

        popupWindow.showAtLocation(gameBinding.root, Gravity.CENTER, 0, 0)
    }

    // Create a fragment that passes the songId
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