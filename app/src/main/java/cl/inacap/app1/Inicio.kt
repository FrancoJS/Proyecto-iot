package cl.inacap.app1

import android.os.Bundle
import android.widget.Button
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val btnLogin = findViewById<Button>(R.id.login)
        val btnReg = findViewById<Button>(R.id.registrar)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, Principal::class.java))
        }
        btnReg.setOnClickListener {

            startActivity(Intent(this, Registrar::class.java))
        }
    }
}