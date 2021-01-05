@file:Suppress("Annotator")

package com.example.leap

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.leap.databinding.ActivityWordsTestBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.apache.commons.csv.CSVFormat
import java.io.BufferedReader
import java.io.InputStreamReader

@Suppress("LABEL_NAME_CLASH")
class WordsTest : AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var binding: ActivityWordsTestBinding

    private var tangoEJ = false
    private var tangoJE = false
    private var exampleEJ = false
    private var exampleJE = false
    private var searchCorrect = 0
    private var searchCheck = 0
    private var howManyQue = 0
    private var howQue = 0
    private var numF = 0
    private var numE = 0
    private var kindOfQueTanEJ = 0
    private var kindOfQueTanJE = 0
    private var kindOfQueExamEJ = 0
    private var kindOfQueExamJE = 0
    private var cause1 = 0
    private var cause2 = 0
    private var cause3 = 0
    private var cause4 = 0
    private var cause5 = 0
    private var cause6 = 0
    private var cause7 = 0
    private var cause8 = 0
    private var cause9 = 0
    private var cause10 = 0
    private var totalError = 0
    private var indexMeaning = 0
    private var kindQuestion = 0
    private var wordsNumberList: MutableList<Double> = mutableListOf()
    private var answerNumberList: MutableList<Double> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordsTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        realm = Realm.getDefaultInstance()

        createData()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val checkState = sharedPreferences.getBoolean("correct", false)
        indexMeaning = if (!checkState) 1 else 4

        //ボタンの動作の設定
        binding.tangoENGJPN.setOnClickListener {
            tangoEJ = binding.tangoENGJPN.isChecked
            kindOfQueTanEJ = if (tangoEJ) 1 else 0
        }
        binding.tangoJPNENG.setOnClickListener {
            tangoJE = binding.tangoJPNENG.isChecked
            kindOfQueTanJE = if (tangoJE) 10 else 0
        }
        binding.exENGJPN.setOnClickListener {
            exampleEJ = binding.exENGJPN.isChecked
            kindOfQueExamEJ = if (exampleEJ) 100 else 0
        }
        binding.exJPNENG.setOnClickListener {
            exampleJE = binding.exJPNENG.isChecked
            kindOfQueExamJE = if (exampleJE) 1000 else 0
        }

        binding.correct.setOnClickListener { searchCorrect = 1 }
        binding.incorrect.setOnClickListener { searchCorrect = 2 }
        binding.unanswered.setOnClickListener { searchCorrect = 3 }
        binding.noneInCorrect.setOnClickListener { searchCorrect = 4 }
        binding.checked.setOnClickListener { searchCheck = 1 }
        binding.noCheck.setOnClickListener { searchCheck = 2 }
        binding.noneInCheck.setOnClickListener { searchCheck = 3 }
        binding.ten.setOnClickListener { howManyQue = 1 }
        binding.twenty.setOnClickListener { howManyQue = 2 }
        binding.thirty.setOnClickListener { howManyQue = 3 }
        binding.thirty.setOnClickListener { howManyQue = 4 }
        binding.fifty.setOnClickListener { howManyQue = 5 }
        binding.all.setOnClickListener { howManyQue = 6 }
        binding.table.setOnClickListener { howQue = 1 }
        binding.random.setOnClickListener { howQue = 2 }
        binding.startButton.setOnClickListener { onStartButtonTapped() }

    }

    private fun createData() {
        val reader = BufferedReader(InputStreamReader(resources.assets.open("sample.csv")))
        reader.use {
            val records = CSVFormat.EXCEL.parse(reader)
            realm.beginTransaction()
            val target = realm.where<WordsData>().findAll()
            target.deleteAllFromRealm()
            records.records.forEach { record ->
                val obj = realm.createObject<WordsData>()
                obj.number = record[0].toInt()
                obj.word = record[1]
                obj.meaningFirst = record[2]
                obj.meaningSecond = record[3]
                obj.meaningThird = record[4]
                obj.meaningFourth = record[5]
                obj.exampleEngFirst = record[6]
                obj.exampleEngSecond = record[7]
                obj.exampleEngThird = record[8]
                obj.exampleEngFourth = record[9]
                obj.exampleJpnFirst = record[10]
                obj.exampleJpnSecond = record[11]
                obj.exampleJpnThird = record[12]
                obj.exampleJpnFourth = record[13]
            }
            realm.commitTransaction()
        }
    }

    private fun onDestroyList(list: MutableList<Double>) {
        for (i in 0 until list.size) list.removeAt(0)
    }

    private fun selectCorrect() {
        when (kindQuestion) {
            1 -> onLoopCorrect(1,0)
            10 -> onLoopCorrect(10,1935)
            11 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(10,1935)
            }
            100 -> onLoopCorrect(100,1935 * 2)
            101 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(100,1935 * 2)
            }
            110 -> {
                onLoopCorrect(10,1935)
                onLoopCorrect(100,1935 * 2)
            }
            111 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(10,1935)
                onLoopCorrect(100,1935 * 2)
            }
            1000 -> onLoopCorrect(1000,1935 * 3)
            1001 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(1000,1935 * 3)
            }
            1010 -> {
                onLoopCorrect(10,1935)
                onLoopCorrect(1000,1935 * 3)
            }
            1011 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(10,1935)
                onLoopCorrect(1000,1935 * 3)
            }
            1100 -> {
                onLoopCorrect(100,1935 * 2)
                onLoopCorrect(1000,1935 * 3)
            }
            1101 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(100,1935 * 2)
                onLoopCorrect(1000,1935 * 3)
            }
            1110 -> {
                onLoopCorrect(10,1935)
                onLoopCorrect(100,1935 * 2)
                onLoopCorrect(1000,1935 * 3)
            }
            1111 -> {
                onLoopCorrect(1,0)
                onLoopCorrect(10,1935)
                onLoopCorrect(100,1935 * 2)
                onLoopCorrect(1000,1935 * 3)
            }
        }
    }

    private fun onLoopCorrect(x: Int, y: Int) {
        for (j in 1..indexMeaning) {
            when (searchCorrect) {
                1 -> { //正解
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<Correct>()
                            .equalTo("number", i- y)
                            .equalTo("kindQuestion", x)
                            .equalTo("indexMeaning", j)
                            .greaterThan("howManyCorrect", 1)
                            .findFirst()
                        if (realmResult != null) wordsNumberList.add(i.toDouble() + (j.toDouble()/10))
                    }
                }
                2 -> { //不正解
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<Correct>()
                            .equalTo("number", i - y)
                            .equalTo("kindQuestion", x)
                            .equalTo("indexMeaning", j)
                            .lessThan("howManyCorrect", 2)
                            .findFirst()
                        if (realmResult != null) wordsNumberList.add(i.toDouble() + (j.toDouble()/10))
                    }
                }
                3 -> { //未正解
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<Correct>()
                            .equalTo("number", i - y)
                            .findFirst()
                        if (realmResult == null) wordsNumberList.add(i.toDouble() + (j.toDouble()/10))
                    }
                }
                //指定なし
                4 -> {
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        wordsNumberList.add(i.toDouble() + (j.toDouble()/10))
                    }
                }
            }
        }
    }

    private fun selectCheck() {
        when (kindQuestion) {
            1 -> onLoopCheck(1,0)
            10 -> onLoopCheck(10,1935)
            11 -> {
                onLoopCheck(1,0)
                onLoopCheck(10,1935)
            }
            100 -> onLoopCheck(100,1935 * 2)
            101 -> {
                onLoopCheck(1,0)
                onLoopCheck(100,1935 * 2)
            }
            110 -> {
                onLoopCheck(10,1935)
                onLoopCheck(100,1935 * 2)
            }
            111 -> {
                onLoopCheck(1,0)
                onLoopCheck(10,1935)
                onLoopCheck(100,1935 * 2)
            }
            1000 -> onLoopCheck(1000,1935 * 3)
            1001 -> {
                onLoopCheck(1,0)
                onLoopCheck(1000,1935 * 3)
            }
            1010 -> {
                onLoopCheck(10,1935)
                onLoopCheck(1000,1935 * 3)
            }
            1011 -> {
                onLoopCheck(1,0)
                onLoopCheck(10,1935)
                onLoopCheck(1000,1935 * 3)
            }
            1100 -> {
                onLoopCheck(100,1935 * 2)
                onLoopCheck(1000,1935 * 3)
            }
            1101 -> {
                onLoopCheck(1,0)
                onLoopCheck(100,1935 * 2)
                onLoopCheck(1000,1935 * 3)
            }
            1110 -> {
                onLoopCheck(10,1935)
                onLoopCheck(100,1935 * 2)
                onLoopCheck(1000,1935 * 3)
            }
            1111 -> {
                onLoopCheck(1,0)
                onLoopCheck(10,1935)
                onLoopCheck(100,1935 * 2)
                onLoopCheck(1000,1935 * 3)
            }
        }
    }

    private fun onLoopCheck(x: Int, y: Int) {
        for (j in 1..indexMeaning) {
            when (searchCheck) {
                1 -> { //チェックあり
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<CheckList>()
                            .equalTo("number", i - y)
                            .equalTo("kindQuestion", x)
                            .equalTo("indexMeaning", j)
                            .findFirst()
                        if (realmResult == null) wordsNumberList.remove(i.toDouble() + (j.toDouble()/10))
                    }
                }
                2 -> { //チェックなし
                    here@
                    for (i in (numF + y)..(numE + y)) {
                        when (j) {
                            2 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                            3 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                            4 -> if (realm.where<WordsData>().equalTo("number", i - y).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                        }
                        val realmResult = realm.where<CheckList>()
                            .equalTo("number", i - y)
                            .equalTo("kindQuestion", x)
                            .equalTo("indexMeaning", j)
                            .findFirst()
                        if (realmResult != null) wordsNumberList.remove(i.toDouble() + (j.toDouble()/10))
                    }
                }
            }
        }
    }

    private fun createAnswerList() {
        when (kindQuestion) {
            1 -> selectAnswerNumber(0)
            10 -> selectAnswerNumber(1935)
            11 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935)
            }
            100 -> selectAnswerNumber(1935 * 2)
            101 -> {
                selectAnswerNumber( 0)
                selectAnswerNumber(1935 * 2)
            }
            110 -> {
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 2)
            }
            111 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 2)
            }
            1000 -> selectAnswerNumber(1935 * 3)
            1001 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935 * 3)
            }
            1010 -> {
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 3)
            }
            1011 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 3)
            }
            1100 -> {
                selectAnswerNumber(1935 * 2)
                selectAnswerNumber(1935 * 3)
            }
            1101 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935 * 2)
                selectAnswerNumber(1935 * 3)
            }
            1110 -> {
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 2)
                selectAnswerNumber(1935 * 3)
            }
            1111 -> {
                selectAnswerNumber(0)
                selectAnswerNumber(1935)
                selectAnswerNumber(1935 * 2)
                selectAnswerNumber(1935 * 3)
            }
        }
    }

    private fun selectAnswerNumber(x: Int) {
        here@
        for (j in 1..indexMeaning) {
            here@
            for (i in (numF + x)..(numE + x)) {
                when (j) {
                    2 -> if (realm.where<WordsData>().equalTo("number", i - x).findFirst()?.meaningSecond.toString().isEmpty()) continue@here
                    3 -> if (realm.where<WordsData>().equalTo("number", i - x).findFirst()?.meaningThird.toString().isEmpty()) continue@here
                    4 -> if (realm.where<WordsData>().equalTo("number", i - x).findFirst()?.meaningFourth.toString().isEmpty()) continue@here
                }
                answerNumberList.add(i.toDouble() + (j.toDouble()/10))
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onStartButtonTapped() {
        val intent = Intent(this, TestScreen::class.java)
        onDestroyList(wordsNumberList)
        onDestroyList(answerNumberList)

        cause1 = if (!tangoEJ && !tangoJE && !exampleEJ && !exampleJE) {
            Toast.makeText(applicationContext, "【出題範囲】を指定してください。", Toast.LENGTH_SHORT).show()
            1
        } else 0

        cause2 = if (binding.chooseRangeF.text.isEmpty() || binding.chooseRangeE.text.isEmpty()) {
            Toast.makeText(applicationContext,"【出題範囲(番号指定)】が未入力です。", Toast.LENGTH_LONG).show()
            1
        } else {
            numF = binding.chooseRangeF.text.toString().toInt()
            numE = binding.chooseRangeE.text.toString().toInt()
            0
        }

        cause3 = if (searchCorrect == 0) {
            Toast.makeText(applicationContext, "【正解・不正解】を指定してください。", Toast.LENGTH_SHORT).show()
            1
        } else 0

        cause4 = if (searchCheck == 0) {
            Toast.makeText(applicationContext, "【チェックの有無】を指定してください。", Toast.LENGTH_SHORT).show()
            1
        } else 0

        cause5 = if (howManyQue == 0) {
            Toast.makeText(applicationContext, "【出題数】を指定してください。", Toast.LENGTH_SHORT).show()
            1
        } else 0

        cause6 = if (howQue == 0) {
            Toast.makeText(applicationContext, "【出題順】を指定してください。", Toast.LENGTH_SHORT).show()
            1
        } else 0

        cause7 = if (numE - numF + 1 <= 3 && cause2 == 0) {
            Toast.makeText(applicationContext, "【出題範囲】は4問以上にしてください。", Toast.LENGTH_LONG).show()
            1
        } else 0

        cause8 = if (numF <= 0 || numE >= 1936) {
            Toast.makeText(applicationContext,"【出題範囲】は1~1935の間にしてください。", Toast.LENGTH_LONG).show()
            1
        } else 0

        totalError = cause1 + cause2 + cause3 + cause4 + cause5 + cause6 + cause7 + cause8

        if (totalError > 0) return

        kindQuestion = kindOfQueTanEJ + kindOfQueTanJE + kindOfQueExamEJ + kindOfQueExamJE
        selectCorrect()
        cause9 = if (wordsNumberList.size == 0) {
            Toast.makeText(applicationContext,"【正解・不正解】で指定された範囲に問題がありません。", Toast.LENGTH_LONG).show()
            1
        } else 0

        selectCheck()
        cause10 = if (wordsNumberList.size == 0) {
            Toast.makeText(applicationContext,"【チェック有無】で指定された範囲に問題がありません。", Toast.LENGTH_LONG).show()
            1
        } else 0

        totalError += cause9 + cause10

        createAnswerList()

        intent.putExtra("TANGO_EJ",tangoEJ)
        intent.putExtra("TANGO_JE",tangoJE)
        intent.putExtra("EXAMPLE_EJ",exampleEJ)
        intent.putExtra("EXAMPLE_JE",exampleJE)
        intent.putExtra("NUM_F",numF)
        intent.putExtra("NUM_E",numE)
        intent.putExtra("SEARCH_CORRECT", searchCorrect)
        intent.putExtra("SEARCH_CHECK",searchCheck)
        intent.putExtra("HOW_MANY_QUE",howManyQue)
        intent.putExtra("HOW_QUE",howQue)

        realm.executeTransaction {
            val target = realm.where<WordsNumberList>().findAll()
            target.deleteAllFromRealm()
            for (i in 0 until wordsNumberList.size) {
                val realmObject = realm.createObject<WordsNumberList>()
                realmObject.number = wordsNumberList[i]
            }
        }

        realm.executeTransaction {
            val target = realm.where<AnswerNumberList>().findAll()
            target.deleteAllFromRealm()
            for (i in 0 until answerNumberList.size) {
                val realmObject = realm.createObject<AnswerNumberList>()
                realmObject.number = answerNumberList[i]
            }
        }
        if (totalError == 0) startActivity(intent)
    }
}





