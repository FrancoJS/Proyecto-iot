package cl.inacap.app1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val usuario = intent.getStringExtra("usu")
        val tipo = intent.getStringExtra("tip")

        val txtBienvenida = findViewById<TextView>(R.id.bienvenido)

        txtBienvenida.text = "¡Bienvenido, $usuario!"

        val btnman: Button = findViewById(R.id.btnman)
        val btnacc: Button = findViewById(R.id.btnacc)
        val btncerrar: Button = findViewById(R.id.btncerrar)

        btnman.setOnClickListener {
            val i = Intent(this@Menu, Manipular::class.java)
            i.putExtra("usu", usuario)
            startActivity(i)

        }

        btnacc.setOnClickListener {
            val i = Intent(this@Menu, Acciones::class.java)
            i.putExtra("usu", usuario)
            startActivity(i)

        }

        btncerrar.setOnClickListener {
            val i = Intent(this@Menu, Principal::class.java)
            startActivity(i)
            finish()
        }
    }
}
