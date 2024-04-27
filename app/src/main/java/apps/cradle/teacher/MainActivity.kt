package apps.cradle.teacher

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import apps.cradle.teacher.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    private enum class ACTION { ADDITION, SUBTRACTION }

    private val actionList: List<ACTION> = listOf(ACTION.ADDITION, ACTION.SUBTRACTION)

    private var firstOperand: Int = 0
    private var secondOperand: Int = 0
    private var action: ACTION = ACTION.ADDITION
    private val userInput: StringBuilder = StringBuilder()
    private var tasksCount: Int = DAILY_TASKS_COUNT

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setListeners()
        tasksCount = if (isTodayExerciseDone()) 0 else DAILY_TASKS_COUNT
        updateTask()
    }

    override fun onStart() {
        super.onStart()
        if (!isTodayExerciseDone() && tasksCount == 0) {
            tasksCount = DAILY_TASKS_COUNT
            updateTaskUi()
        }
    }

    private fun setListeners() {
        binding.apply {
            button0.setOnClickListener(onButtonClick)
            button1.setOnClickListener(onButtonClick)
            button2.setOnClickListener(onButtonClick)
            button3.setOnClickListener(onButtonClick)
            button4.setOnClickListener(onButtonClick)
            button5.setOnClickListener(onButtonClick)
            button6.setOnClickListener(onButtonClick)
            button7.setOnClickListener(onButtonClick)
            button8.setOnClickListener(onButtonClick)
            button9.setOnClickListener(onButtonClick)
            buttonBackspace.setOnClickListener(onButtonClick)
            buttonOK.setOnClickListener(onButtonClick)
        }
    }

    private fun updateTask() {
        generateTask()
        updateTaskUi()
    }

    private fun generateTask() {
        firstOperand = Random.nextInt(10)
        secondOperand = Random.nextInt(10)
        action = actionList[Random.nextInt(2)]

        if (firstOperand < secondOperand) {
            val temp = firstOperand
            firstOperand = secondOperand
            secondOperand = temp
        }

        userInput.clear()
    }

    private fun updateTaskUi() {
        binding.aim.text = when (tasksCount) {
            0 -> getString(R.string.activityMainTaskFinished)
            else -> getString(R.string.activityMainAim, tasksCount.toString())
        }
        binding.task.text = getString(
            R.string.activityMainTask,
            firstOperand.toString(),
            when (action) {
                ACTION.ADDITION -> "+"
                ACTION.SUBTRACTION -> "-"
            },
            secondOperand.toString(),
            userInput.toString()
        )
    }

    private val onButtonClick: (View) -> Unit = { button ->
        if (button.id == R.id.buttonBackspace)
            if (userInput.isNotEmpty()) userInput.deleteAt(userInput.lastIndex)
        if (button.id == R.id.buttonOK) checkAnswer()
        val containsOnlyZero = userInput.length == 1 && userInput[0] == '0'
        if (!containsOnlyZero) {
            if (userInput.length < 2) {
                when (button.id) {
                    R.id.button0 -> userInput.append("0")
                    R.id.button1 -> userInput.append("1")
                    R.id.button2 -> userInput.append("2")
                    R.id.button3 -> userInput.append("3")
                    R.id.button4 -> userInput.append("4")
                    R.id.button5 -> userInput.append("5")
                    R.id.button6 -> userInput.append("6")
                    R.id.button7 -> userInput.append("7")
                    R.id.button8 -> userInput.append("8")
                    R.id.button9 -> userInput.append("9")
                }
            }
        }
        updateTaskUi()
    }

    private fun checkAnswer() {
        if (userInput.isEmpty()) return
        val correctAnswer = when (action) {
            ACTION.ADDITION -> firstOperand + secondOperand
            ACTION.SUBTRACTION -> firstOperand - secondOperand
        }
        val userAnswer = userInput.toString().toInt()
        if (userAnswer == correctAnswer) {
            if (tasksCount > 0) {
                tasksCount--
                if (tasksCount == 0) saveTodayProgress()
            }
        } else {
            if (!isTodayExerciseDone()) tasksCount = DAILY_TASKS_COUNT
            showErrorDialog(correctAnswer)
        }
        updateTask()
    }

    private fun showErrorDialog(correctAnswer: Int) {
        val errorDialog = ErrorDialogFragment()
        val correctAnswerString = getCorrectAnswerString(correctAnswer)
        val bundle = Bundle()
        bundle.putString(ErrorDialogFragment.EXTRA_MESSAGE, correctAnswerString)
        errorDialog.arguments = bundle
        errorDialog.show(supportFragmentManager, "error_dialog")
    }

    private fun getCorrectAnswerString(correctAnswer: Int): String {
        val builder = StringBuilder()
        builder.append(firstOperand)
        when (action) {
            ACTION.ADDITION -> builder.append(" + ")
            ACTION.SUBTRACTION -> builder.append(" - ")
        }
        builder.append(secondOperand)
        builder.append(" = ")
        builder.append(correctAnswer)
        return builder.toString()
    }

    private fun saveTodayProgress() {
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putLong(PREF_LAST_SUCCESS_DATE, System.currentTimeMillis())
            .apply()
    }

    private fun isTodayExerciseDone(): Boolean {
        val today = System.currentTimeMillis()
        val lastSuccess = PreferenceManager.getDefaultSharedPreferences(this)
            .getLong(PREF_LAST_SUCCESS_DATE, 0L)
        val todayCalendar = Calendar.getInstance().apply { timeInMillis = today }
        val lastCalendar = Calendar.getInstance().apply { timeInMillis = lastSuccess }
        val years = todayCalendar.get(Calendar.YEAR) == lastCalendar.get(Calendar.YEAR)
        val months = todayCalendar.get(Calendar.MONTH) == lastCalendar.get(Calendar.MONTH)
        val days =
            todayCalendar.get(Calendar.DAY_OF_MONTH) == lastCalendar.get(Calendar.DAY_OF_MONTH)
        return years && months && days
    }

    companion object {
        const val DAILY_TASKS_COUNT = 10
        const val PREF_LAST_SUCCESS_DATE = "pref_last_success_date"
    }
}