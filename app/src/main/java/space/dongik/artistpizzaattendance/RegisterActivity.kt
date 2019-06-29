package space.dongik.artistpizzaattendance

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private val TAG = "RegisterActivity"

    lateinit var apiService:ArtistpizzaApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var intent = getIntent()
        var number = intent.getStringExtra("number")
        editNumber.setText(number)
        apiService = ArtistpizzaApiService.create()
        register_button.setOnClickListener {
            val name = nameEdit.text.toString()
            val pin = editNumber.text.toString()
            apiService.register(RegisterRequest(name, pin))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                        result->
                    Log.d(TAG, result.message)
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                    this.finish()
                }, { error ->
                    error.printStackTrace()
                    Log.d(TAG, error.toString())
                })
        }
//        editNumber = number
    }
}