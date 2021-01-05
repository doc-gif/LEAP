@file:Suppress("Annotator")

package com.example.leap

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leap.databinding.ActivityTestScreanBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where


class TestScreen : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var binding: ActivityTestScreanBinding

    private var intentFlagToResult = 0
    private var tangoEJ = false
    private var tangoJE = false
    private var exampleEJ = false
    private var exampleJE = false
    private var numF = 0
    private var numE = 0
    private var searchCorrect = 0
    private var searchCheck = 0
    private var howManyQue = 0
    private var howQue = 0
    private var questions = ""
    private var queNum = 0
    private var kindQuestion = 0
    private var kindQuestionAns = 0
    private var falseAns1 = 0.0
    private var falseAns2 = 0.0
    private var falseAns3 = 0.0
    private var checkQueNum = 0
    private var checking = false
    private var totalQue = 0
    private var toResult = 1
    private var hasBeenRegister = false
    private var howManyCorrect = 0
    private var howManyIncorrect = 0
    private var randomBase = 0
    private var oneMoreFlag = false
    private var indexMeaning = 0
    private var indexMeaningAns = 0
    private var originOfQueNum = 0.0
    private val wordsNumberList: MutableList<Double> = mutableListOf()
    private val answerNumberList: MutableList<Double> = mutableListOf()
    private val originalNumberList: MutableList<Int> = mutableListOf()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScreanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        realm = Realm.getDefaultInstance()

        binding.next.visibility = View.INVISIBLE

        binding.answer1.setBackgroundResource(R.drawable.test_button)
        binding.answer2.setBackgroundResource(R.drawable.test_button)
        binding.answer3.setBackgroundResource(R.drawable.test_button)
        binding.answer4.setBackgroundResource(R.drawable.test_button)

        tangoEJ = intent.getBooleanExtra("TANGO_EJ", false)
        tangoJE = intent.getBooleanExtra("TANGO_JE", false)
        exampleEJ = intent.getBooleanExtra("EXAMPLE_EJ", false)
        exampleJE = intent.getBooleanExtra("EXAMPLE_JE", false)
        numF = intent.getIntExtra("NUM_F", 0)
        numE = intent.getIntExtra("NUM_E", 0)
        searchCorrect = intent.getIntExtra("SEARCH_CORRECT", 0)
        searchCheck = intent.getIntExtra("SEARCH_CHECK", 0)
        howManyQue = intent.getIntExtra("HOW_MANY_QUE", 0)
        howQue = intent.getIntExtra("HOW_QUE", 0)
        oneMoreFlag = intent.getBooleanExtra("ONE_MORE_FLAG", false)

        for (i in numF..numE) originalNumberList.add(i)

        val realmResult1 = realm.where<WordsNumberList>().findAll()
        for (i in 0 until realmResult1.size) wordsNumberList.add(realmResult1[i]?.number!!)
        val realmResult2 = realm.where<AnswerNumberList>().findAll()
        for (i in 0 until realmResult2.size) answerNumberList.add(realmResult2[i]?.number!!)


        //出題範囲(形式)から問題を選ぶ
        //このまま問題をmutableMapに登録をするか番号だけで行くか迷い中
        //番号のままで行くと出題のメソッドを変えないといけない
        //遅延とかを考えると先にarrayに登録できればしたほうがいいかも

        if (howQue == 1) selectQuestion() else selectQuestionRandom()

        //チェックリストに単語が乗っているか確認する
        val realmResult = realm.where<CheckList>()
            .equalTo("number", queNum)
            .equalTo("kindQuestion", kindQuestion)
            .equalTo("indexMeaning", indexMeaning)
            .findFirst()
        if (realmResult != null) {
            hasBeenRegister = true
        }
        checking = when (hasBeenRegister) {
            true -> {
                binding.checkButton.setImageResource(R.drawable.ic_baseline_check_circle_24)
                true
            }
            false -> false
        }
        //出題する問題数を確認する
        totalQue = wordsNumberList.size
        countHowManyQue(howManyQue)
        //解答選択肢表示
        when ((0..3).random()) {
            0 -> selectAnswer(binding.answer1,binding.answer2,binding.answer3,binding.answer4,1)
            1 -> selectAnswer(binding.answer2,binding.answer1,binding.answer3,binding.answer4,2)
            2 -> selectAnswer(binding.answer3,binding.answer2,binding.answer1,binding.answer1,3)
            3 -> selectAnswer(binding.answer4,binding.answer2,binding.answer3,binding.answer1,4)
        }

        binding.answer1.setOnClickListener { onClickAnswer(binding.answer1) }
        binding.answer2.setOnClickListener { onClickAnswer(binding.answer2) }
        binding.answer3.setOnClickListener { onClickAnswer(binding.answer3) }
        binding.answer4.setOnClickListener { onClickAnswer(binding.answer4) }
        binding.next.setOnClickListener { onNextTapped() }
        binding.checkButton.setOnClickListener { onCheckButtonTapped() }

        binding.totalque.text = totalQue.toString()
        binding.quenumNow.text = toResult.toString()
        binding.quenum.text = queNum.toString()
    }

    private fun selectQuestion() {
        separateQuestionNumber(0)
        putQuestion()
    }

    private fun selectQuestionRandom() {
        val random = (0 until wordsNumberList.size).random()
        separateQuestionNumber(random)
        putQuestion()
    }

    private fun separateQuestionNumber(x: Int) {
        originOfQueNum = wordsNumberList[x]
        indexMeaning = originOfQueNum.toString().takeLast(1).toInt()
        when (originOfQueNum.toInt()) {
            in 1..1935 -> {
                queNum = originOfQueNum.toInt()
                kindQuestion = 1
            }
            in 1936..3870 -> {
                queNum = originOfQueNum.toInt() - 1935
                kindQuestion = 10
            }
            in 3871..5805 -> {
                queNum = originOfQueNum.toInt() - 3870
                kindQuestion = 100
            }
            in 5806..7740 -> {
                queNum = originOfQueNum.toInt() - 5805
                kindQuestion = 1000
            }
        }
    }

    private fun putQuestion() {
        val realmResult = realm.where<WordsData>()
            .equalTo("number", queNum)
            .findFirst()
        when (kindQuestion) {
            1 -> binding.question.text = realmResult?.word
            10 -> {
                when (indexMeaning) {
                    1 -> binding.question.text = realmResult?.meaningFirst
                    2 -> binding.question.text = realmResult?.meaningSecond
                    3 -> binding.question.text = realmResult?.meaningThird
                    4 -> binding.question.text = realmResult?.meaningFourth
                }
            }
            100 -> {
                when (indexMeaning) {
                    1 -> binding.question.text = realmResult?.exampleEngFirst
                    2 -> binding.question.text = realmResult?.exampleEngSecond
                    3 -> binding.question.text = realmResult?.exampleEngThird
                    4 -> binding.question.text = realmResult?.exampleEngFourth
                }
            }
            1000 -> {
                when (indexMeaning) {
                    1 -> binding.question.text = realmResult?.exampleJpnFirst
                    2 -> binding.question.text = realmResult?.exampleJpnSecond
                    3 -> binding.question.text = realmResult?.exampleJpnThird
                    4 -> binding.question.text = realmResult?.exampleJpnFourth
                }
            }
        }
    }

    private fun selectAnswer(buttonCorrect: Button, button1: Button, button2: Button, button3: Button, x: Int) {
        randomBase = x
        val realmResult = realm.where<WordsData>()
            .equalTo("number", queNum)
            .findFirst()
        when (kindQuestion) {
            1 -> {
                when (indexMeaning) {
                    1 -> {
                        questions = realmResult?.meaningFirst.toString()
                        buttonCorrect.text = realmResult?.meaningFirst
                    }
                    2 -> {
                        questions = realmResult?.meaningSecond.toString()
                        buttonCorrect.text = realmResult?.meaningSecond
                    }
                    3 -> {
                        questions = realmResult?.meaningThird.toString()
                        buttonCorrect.text = realmResult?.meaningThird
                    }
                    4 -> {
                        questions = realmResult?.meaningFourth.toString()
                        buttonCorrect.text = realmResult?.meaningFourth
                    }
                }
            }
            10 -> {
                questions = realmResult?.word.toString()
                buttonCorrect.text = realmResult?.word
            }
            100 -> {
                when (indexMeaning) {
                    1 -> {
                        questions = realmResult?.exampleJpnFirst.toString()
                        buttonCorrect.text = realmResult?.exampleJpnFirst
                    }
                    2 -> {
                        questions = realmResult?.exampleJpnSecond.toString()
                        buttonCorrect.text = realmResult?.exampleJpnSecond
                    }
                    3 -> {
                        questions = realmResult?.exampleJpnThird.toString()
                        buttonCorrect.text = realmResult?.exampleJpnThird
                    }
                    4 -> {
                        questions = realmResult?.exampleJpnFourth.toString()
                        buttonCorrect.text = realmResult?.exampleJpnFourth
                    }
                }
            }
            1000 -> {
                when (indexMeaning) {
                    1 -> {
                        questions = realmResult?.exampleEngFirst.toString()
                        buttonCorrect.text = realmResult?.exampleEngFirst
                    }
                    2 -> {
                        questions = realmResult?.exampleEngSecond.toString()
                        buttonCorrect.text = realmResult?.exampleEngSecond
                    }
                    3 -> {
                        questions = realmResult?.exampleEngThird.toString()
                        buttonCorrect.text = realmResult?.exampleEngThird
                    }
                    4 -> {
                        questions = realmResult?.exampleEngFourth.toString()
                        buttonCorrect.text = realmResult?.exampleEngFourth
                    }
                }
            }
        }
        button1.text = selectFalseAnswer1()
        button2.text = selectFalseAnswer2()
        button3.text = selectFalseAnswer3()
    }

    private fun findAnswer(x: Double): String {
        var answer = ""
        when (kindQuestionAns) {
            1 -> {
                val realmResult = realm.where<WordsData>()
                    .equalTo("number", x.toInt())
                    .findFirst()
                when (indexMeaningAns) {
                    1 -> answer = realmResult?.meaningFirst.toString()
                    2 -> answer = realmResult?.meaningSecond.toString()
                    3 -> answer = realmResult?.meaningThird.toString()
                    4 -> answer = realmResult?.meaningFourth.toString()
                }
            }
            10 -> {
                answer = realm.where<WordsData>()
                    .equalTo("number", x.toInt())
                    .findFirst()
                    ?.word.toString()
            }
            100 -> {
                val realmResult = realm.where<WordsData>()
                    .equalTo("number", x.toInt())
                    .findFirst()
                when (indexMeaningAns) {
                    1 -> answer = realmResult?.exampleJpnFirst.toString()
                    2 -> answer = realmResult?.exampleJpnSecond.toString()
                    3 -> answer = realmResult?.exampleJpnThird.toString()
                    4 -> answer = realmResult?.exampleJpnFourth.toString()
                }
            }
            1000 -> {
                val realmResult = realm.where<WordsData>()
                    .equalTo("number", x.toInt())
                    .findFirst()
                when (indexMeaningAns) {
                    1 -> answer = realmResult?.exampleEngFirst.toString()
                    2 -> answer = realmResult?.exampleEngSecond.toString()
                    3 -> answer = realmResult?.exampleEngThird.toString()
                    4 -> answer = realmResult?.exampleEngFourth.toString()
                }
            }
        }
        return answer
    }

    private fun selectFalseAnswer1(): String {
        var a: Int
        do {
            falseAns1 = answerNumberList[(0 until answerNumberList.size).random()]
            indexMeaningAns = falseAns1.toString().takeLast(1).toInt()
            when (falseAns1.toInt()) {
                in 1..1935 -> {
                    kindQuestionAns = 1
                }
                in 1936..3870 -> {
                    a = falseAns1.toInt() - 1935
                    falseAns1 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 10
                }
                in 3871..5805 -> {
                    a = falseAns1.toInt() - 3870
                    falseAns1 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 100
                }
                in 5806..7740 -> {
                    a = falseAns1.toInt() - 5805
                    falseAns1 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 1000
                }
            }
        } while (falseAns1.toInt() == originOfQueNum.toInt() || kindQuestion != kindQuestionAns)

        return findAnswer(falseAns1)
    }

    private fun selectFalseAnswer2(): String {
        var a: Int
        do {
            falseAns2 = answerNumberList[(0 until answerNumberList.size).random()]
            indexMeaningAns = falseAns2.toString().takeLast(1).toInt()
            when (falseAns2.toInt()) {
                in 1..1935 -> {
                    kindQuestionAns = 1
                }
                in 1936..3870 -> {
                    a = falseAns2.toInt() - 1935
                    falseAns2 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 10
                }
                in 3871..5805 -> {
                    a = falseAns2.toInt() - 3870
                    falseAns2 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 100
                }
                in 5806..7740 -> {
                    a = falseAns2.toInt() - 5805
                    falseAns2 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 1000
                }
            }
        } while (falseAns2.toInt() == originOfQueNum.toInt() || falseAns2.toInt() == falseAns1.toInt() || kindQuestion != kindQuestionAns)

        return findAnswer(falseAns2)
    }

    private fun selectFalseAnswer3(): String {
        var a: Int
        do {
            falseAns3 = answerNumberList[(0 until answerNumberList.size).random()]
            indexMeaningAns = falseAns3.toString().takeLast(1).toInt()
            when (falseAns3.toInt()) {
                in 1..1935 -> {
                    kindQuestionAns = 1
                }
                in 1936..3870 -> {
                    a = falseAns3.toInt() - 1935
                    falseAns3 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 10
                }
                in 3871..5805 -> {
                    a = falseAns3.toInt() - 3870
                    falseAns3 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 100
                }
                in 5806..7740 -> {
                    a = falseAns3.toInt() - 5805
                    falseAns3 = a.toDouble() + (indexMeaningAns.toDouble()/10)
                    kindQuestionAns = 1000
                }
            }
        } while (falseAns3.toInt() == originOfQueNum.toInt() || falseAns3.toInt() == falseAns2.toInt() || falseAns3.toInt() == falseAns1.toInt() || kindQuestion != kindQuestionAns)

        return findAnswer(falseAns3)
    }

    private fun countHowManyQue(x: Int) {
        if (x == 6) return
        if (totalQue >= x * 10) {
            totalQue = x * 10
        } else {
            Toast.makeText(applicationContext, "指定された出題数より問題が少ないため${totalQue}問出題します", Toast.LENGTH_LONG).show()
        }
    }

    private fun correctAnswer() {
        when (randomBase) {
            1 -> binding.answer1.setBackgroundColor(R.drawable.ic_circle_24)
            2 -> binding.answer2.setBackgroundColor(R.drawable.ic_circle_24)
            3 -> binding.answer3.setBackgroundColor(R.drawable.ic_circle_24)
            4 -> binding.answer4.setBackgroundColor(R.drawable.ic_circle_24)
        }
    }

    private fun registerCorrect(x: Boolean) {
        realm.executeTransaction {
            if (realm.where<Correct>()
                    .equalTo("number", queNum)
                    .equalTo("indexMeaning", indexMeaning)
                    .equalTo("kindQuestion", kindQuestion)
                    .findFirst() == null) {
                val realmObject = realm.createObject<Correct>()
                when (x) {
                    true -> {
                        realmObject.number = queNum
                        realmObject.indexMeaning = indexMeaning
                        realmObject.kindQuestion = kindQuestion
                        realmObject.howManyCorrect = realmObject.howManyCorrect + 1
                    }
                    false -> {
                        realmObject.number = queNum
                        realmObject.indexMeaning = indexMeaning
                        realmObject.kindQuestion = kindQuestion
                        realmObject.howManyCorrect = 0
                    }
                }
            }
            val realmObject = realm.where<Correct>()
                .equalTo("number",queNum)
                .equalTo("indexMeaning", indexMeaning)
                .equalTo("kindQuestion", kindQuestion)
                .findFirst()
            when (x) {
                true -> {
                    realmObject?.howManyCorrect = realmObject?.howManyCorrect!! + 1
                }
                false -> {
                    realmObject?.howManyCorrect = 0
                }
            }
        }
    }

    private fun onClickAnswer(x: Button) {
        /***
         * intentFlagToResult
         * 0 問題未解答
         * 1 Result画面にintent
         * 2 次の問題に行く
         */
        if (x.text == questions && intentFlagToResult == 0) {
            binding.setCorIncor.setImageResource(R.drawable.ic_circle_24)
            intentFlagToResult = 2
            howManyCorrect ++
            registerCorrect(true)
            binding.next.visibility = View.VISIBLE
            x.setBackgroundResource(R.drawable.test_button_press)
        }
        if (x.text != questions && intentFlagToResult == 0) {
            binding.setCorIncor.setImageResource(R.drawable.ic_close_24)
            intentFlagToResult = 2
            howManyIncorrect ++
            registerCorrect(false)
            binding.next.visibility = View.VISIBLE
            correctAnswer()
        }
        if (toResult == totalQue){
            intentFlagToResult = 1
            binding.next.text = "終了"
            binding.next.visibility = View.VISIBLE
        }
    }

    private fun registerCheckList() {
        realm.executeTransaction {
            val realmObject = realm.createObject<CheckList>()
            realmObject.number = queNum
            realmObject.indexMeaning = indexMeaning
            realmObject.kindQuestion = kindQuestion
        }
    }

    private fun onCheckButtonTapped() {
        checking = if (!checking) {
            binding.checkButton.setImageResource(R.drawable.ic_baseline_check_circle_24)
            Toast.makeText(applicationContext, "チェックされました。", Toast.LENGTH_SHORT).show()
            checkQueNum = 1
            true
        } else {
            binding.checkButton.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
            checkQueNum = 0
            val target = realm.where(CheckList::class.java)
                .equalTo("number", queNum)
                .findAll()
            realm.executeTransaction {
                target.deleteFirstFromRealm()
            }
            false
        }
    }

    private fun onNextTapped() {
        when (checkQueNum) {
            1 -> {
                registerCheckList()
                checkQueNum = 0
            }
        }
        when (intentFlagToResult) {
            1 -> if (toResult == totalQue) {
                val intent = Intent(this, Result::class.java)
                intent.putExtra("CORRECT",howManyCorrect)
                intent.putExtra("INCORRECT",howManyIncorrect)
                intent.putExtra("TOTAL", totalQue)
                intent.putExtra("TANGO_EJ",tangoEJ)
                intent.putExtra("TANGO_JE",tangoJE)
                intent.putExtra("EX_EJ",exampleEJ)
                intent.putExtra("EX_JE",exampleJE)
                intent.putExtra("NUM_F",numF)
                intent.putExtra("NUM_E",numE)
                intent.putExtra("SEARCH_CHECK",searchCheck)
                intent.putExtra("SEARCH_CORRECT", searchCorrect)
                intent.putExtra("HOW_MANY_QUE",howManyQue)
                intent.putExtra("HOW_QUE",howQue)
                startActivity(intent)
            }
            2 -> {
                binding.next.visibility = View.INVISIBLE
                binding.setCorIncor.setImageResource(R.drawable.ic_none_24)
                binding.checkButton.setImageResource(R.drawable.ic_baseline_check_circle_outline_24)
                binding.answer1.setBackgroundResource(R.drawable.test_button)
                binding.answer2.setBackgroundResource(R.drawable.test_button)
                binding.answer3.setBackgroundResource(R.drawable.test_button)
                binding.answer4.setBackgroundResource(R.drawable.test_button)
                intentFlagToResult = 0
                falseAns1 = 0.0
                falseAns2 = 0.0
                falseAns3 = 0.0
                toResult ++
                hasBeenRegister = false
                wordsNumberList.remove(originOfQueNum)
                binding.quenumNow.text = toResult.toString()

                //問題表示
                if (howQue == 1) selectQuestion() else selectQuestionRandom()

                binding.quenum.text = queNum.toString()

                //解答選択肢表示
                when ((0..3).random()) {
                    0 -> selectAnswer(binding.answer1,binding.answer2,binding.answer3,binding.answer4,1)
                    1 -> selectAnswer(binding.answer2,binding.answer1,binding.answer3,binding.answer4,2)
                    2 -> selectAnswer(binding.answer3,binding.answer2,binding.answer1,binding.answer1,3)
                    3 -> selectAnswer(binding.answer4,binding.answer2,binding.answer3,binding.answer1,4)
                }
                //チェックリストに単語が乗っているか確認する
                val realmResult = realm.where<CheckList>()
                    .equalTo("number", queNum)
                    .equalTo("kindQuestion", kindQuestion)
                    .equalTo("indexMeaning", indexMeaning)
                    .findFirst()
                if (realmResult != null) {
                    hasBeenRegister = true
                }
                checking = when (hasBeenRegister) {
                    true -> {
                        binding.checkButton.setImageResource(R.drawable.ic_baseline_check_circle_24)
                        true
                    }
                    false -> false
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}



