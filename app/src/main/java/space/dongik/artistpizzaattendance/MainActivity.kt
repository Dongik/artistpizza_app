package space.dongik.artistpizzaattendance

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    public lateinit var apiService:ArtistpizzaApiService
    private val TAG = "MainActivity"

    val defaultText = "010-****"
    var typedText = ""
    var attendable = false
    var memberId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        apiService = ArtistpizzaApiService.create()
        phoneTextView.text = defaultText
        phoneTextView.gravity = Gravity.CENTER

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(TAG, "keycode=${keyCode}")
        when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                if (attendable) {
                    attend()
                }
            }
            KeyEvent.KEYCODE_0 -> typeNumber(0)
            KeyEvent.KEYCODE_1 -> typeNumber(1)
            KeyEvent.KEYCODE_2 -> typeNumber(2)
            KeyEvent.KEYCODE_3 -> typeNumber(3)
            KeyEvent.KEYCODE_4 -> typeNumber(4)
            KeyEvent.KEYCODE_5 -> typeNumber(5)
            KeyEvent.KEYCODE_6 -> typeNumber(6)
            KeyEvent.KEYCODE_7 -> typeNumber(7)
            KeyEvent.KEYCODE_8 -> typeNumber(8)
            KeyEvent.KEYCODE_9 -> typeNumber(9)
            KeyEvent.KEYCODE_0 -> typeNumber(0)
            KeyEvent.KEYCODE_DEL -> delNumber()
            else -> return super.onKeyUp(keyCode, event)
        }
        return false
    }

    private fun attend() {
        val memberId = memberId
        Log.d(TAG, "attend requested")
        apiService.attend(AttendRequest(memberId))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                    result->
                messageTextView.text = result.message
                Log.d(TAG, result.message)

                typedText = ""
                phoneTextView.text = defaultText
                Log.d(TAG, "message:${result.message}")
                attendable = false
            }, { error ->
                error.printStackTrace()
                Log.d(TAG, error.toString())
                attendable = false
            })
        phoneTextView.text = defaultText
        typedText = ""
    }

    private fun delNumber() {
        if (0 < typedText.length) {
            typedText = typedText.substring(0, typedText.length - 1)
            phoneTextView.text = defaultText + "-" +typedText
            messageTextView.text = ""
//            attendButton.visibility = View.INVISIBLE
            attendable = false
            button.visibility = View.INVISIBLE
            if (typedText.length == 0) {
                phoneTextView.text = defaultText
            }
        }
    }

    private fun typeNumber(number:Int){
        if (typedText.length < 4) {
            typedText = typedText + number.toString()
            phoneTextView.text = defaultText + "-" + typedText
            if (typedText.length == 4) {
                Log.d(TAG, "ask! ")
                apiService.ask(typedText)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                            result->
                        messageTextView.text = result.message
                        memberId = result.member_id
                        Log.d("MainActivity", "memberId=$memberId")
                        if (result.is_attendable) {
//                            attendButton.visibility = View.VISIBLE\
                            attendable = true
                        } else if (result.message.contains("가입되지")) {
                            button.setOnClickListener {
                                val intent = Intent(this, RegisterActivity::class.java)
                                intent.putExtra("number", typedText)
                                intent.putExtra("type", button.text)
                                startActivity(intent)
                            }
                            button.text = "가입"
                            button.visibility = View.VISIBLE
                            button.isClickable = true
                        } else if (result.message.contains("지났습니다") or result.message.contains("0입니다")) {
                            button.setOnClickListener {
                                val intent = Intent(this, ExtendActivity::class.java)
                                intent.putExtra("memberId", memberId)
                                startActivity(intent) }

                            button.text = "연장"
                            button.visibility = View.VISIBLE
                            button.isClickable = true
                        } else {
                            button.visibility = View.INVISIBLE
                            button.isClickable = false
                        }
                    },{ error->
                        Log.d(TAG, error.toString())
                    })
            }
        }
    }
}
