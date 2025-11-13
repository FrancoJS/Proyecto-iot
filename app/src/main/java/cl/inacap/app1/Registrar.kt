package cl.inacap.app1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import android.widget.ArrayAdapter
import android.widget.Spinner

class Registrar : AppCompatActivity() {

    private lateinit var edtUsuario: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPass: EditText

    private lateinit var btnRegistro: Button

    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        edtUsuario = findViewById(R.id.usureg)
        edtPass = findViewById(R.id.conreg)
        btnRegistro = findViewById(R.id.btnreg)
        edtEmail = findViewById<EditText>(R.id.emailreg)
        spinner = findViewById(R.id.spinner_tipo)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val opciones = listOf("Selecciona un tipo de usuario","Operador", "Administrador")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter



        btnRegistro.setOnClickListener {
            registrarUsuario()
        }
        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun registrarUsuario() {
        val usuario = edtUsuario.text.toString().trim()
        val email = edtEmail.text.toString().trim()
        val pass = edtPass.text.toString().trim()
        val tipo = spinner.selectedItem.toString()



        if (usuario.isEmpty() || pass.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if (tipo == "Seleccionar tipo de usuario") {
            Toast.makeText(this, "Seleccione un tipo de usuario", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseDatabase.getInstance().getReference("Usuarios")

        db.get().addOnSuccessListener { snapshot ->
            val nuevoId = (snapshot.childrenCount + 1).toString()

            val datos = mapOf(
                "nombre" to usuario,
                "password" to pass,
                "email" to email,
                "tipo" to tipo
            )

            db.child(nuevoId).setValue(datos).addOnCompleteListener {
                Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
