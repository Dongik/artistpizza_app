package space.dongik.artistpizzaattendance

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_extend.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import android.text.Editable




class ExtendActivity : AppCompatActivity() {
    lateinit var apiService:ArtistpizzaApiService
    lateinit var memberId:String
    var weekNumber = 0
    var course = ""
    var expireDate = 0
    private val TAG = "RegisterActivity"
    var dateFormat:SimpleDateFormat = SimpleDateFormat("yyMMdd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extend)
        var intent = getIntent()
        memberId = intent.getStringExtra("memberId")
        apiService = ArtistpizzaApiService.create()
        var name = memberId.substring(0, memberId.length - 4)
        textView.text = name + "님 과정을 고르세요"


        extend_button.isEnabled = false
        editWeek.setText(weekNumber.toString())

        oilRadio.setOnClickListener {
            course = "oil"
            if (0 < weekNumber) {
                extend_button.isEnabled = true
            }
        }

        waterRadio.setOnClickListener {
            course = "water"
            if (0 < weekNumber) {
                extend_button.isEnabled = true
            }
        }
        val inputTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                var wn = editWeek.text.toString().toIntOrNull()

                if (wn is Int) {
                    weekNumber = wn
                } else {
                    weekNumber = 0
                }

                calculate(weekNumber)

                if (0 < weekNumber || course.isEmpty()) {
                    extend_button.isEnabled = true
                } else {
                    extend_button.isEnabled = false
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        }
        editWeek.addTextChangedListener(inputTextWatcher)

        extend_button.setOnClickListener {
            if (0 < weekNumber) {
                apiService.extend(ExtendRequest(memberId, course, weekNumber))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ result ->
                        Log.d(TAG, result.message)
                        Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                        this.finish()
                    }, { error ->
                        error.printStackTrace()
                        Log.d(TAG, error.toString())
                    })
            }


        }

//        editNumber = number
    }

    private fun calculate(weekNumber: Int) {
        var date = Calendar.getInstance()
        date.add(Calendar.DATE, weekNumber * 7)
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH) + 1
        val day = date.get(Calendar.DAY_OF_MONTH)
        textView3.text = "${year}년 ${month}월 ${day}일 까지 입니다."
//        expireDate = dateFormat.format(date).toInt()
    }
}