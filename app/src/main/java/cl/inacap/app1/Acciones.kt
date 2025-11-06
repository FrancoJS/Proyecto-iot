package cl.inacap.app1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Acciones: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acciones)

        val usuario = intent.getStringExtra("usu")

        val txtBienvenida = findViewById<TextView>(R.id.bienvenido)

        txtBienvenida.text = "Menu Acciones, ¡Bienvenido $usuario!"

        val btnVolver: Button = findViewById(R.id.btnVolver)

        btnVolver.setOnClickListener {
            finish()
        }

    }
}