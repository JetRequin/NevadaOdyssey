package fr.isen.nevadaodyssey

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var userPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userPreferences = getSharedPreferences(UserPreferences.name, Context.MODE_PRIVATE)

        buttonLogin.setOnClickListener{

            saveUser(editTextLogin.text.toString())
            checkUserValues()

            val intentWallActivity = Intent(this, HomeActivity::class.java)
            intentWallActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentWallActivity)
            finish()
        }
    }

    private fun saveUser(name: String) {
        val editor = userPreferences?.edit()
        editor?.putString(UserPreferences.name, name)
        editor?.putInt(UserPreferences.money,10000)
        editor?.apply()
    }

     fun checkUserValues(){
        val name = userPreferences?.getString(UserPreferences.name,null)?: ""

        Toast.makeText(
            this,
            "Coucou $name",
            Toast.LENGTH_LONG).show()
    }
}
