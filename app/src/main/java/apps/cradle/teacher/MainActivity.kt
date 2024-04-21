package apps.cradle.teacher

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import apps.cradle.teacher.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    private enum class ACTION { ADDITION, SUBTRACTION }

    private val actionList: List<ACTION> = listOf(ACTION.ADDITION, ACTION.SUBTRACTION)

    private var firstOperand: Int = 0
    private var secondOperand: Int = 0
    private var action: ACTION = ACTION.ADDITION
    private val userInput: StringBuilder = StringBuilder()
    private var tasksCount: Int = 10

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setListeners()
        updateTask()
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
            if (tasksCount > 0) tasksCount--
        } else tasksCount = 10
        updateTask()
    }
}