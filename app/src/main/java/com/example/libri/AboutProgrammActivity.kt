package com.example.libri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class AboutProgrammActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_about_programm)


            }
    fun Information(view: View){ val intent = Intent(this, InformationAboutTheProgram::class.java)
        startActivity(intent)

        }
    fun BackClick(view: View) {
        finish()
    }


}
