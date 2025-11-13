package cl.inacap.app1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Acciones : AppCompatActivity() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val accionesRef: DatabaseReference = database.getReference("Acciones")

    private var accionesListener: ValueEventListener? = null

    // Views
    private lateinit var txtBienvenida: TextView
    private lateinit var btnVolver: Button
    private lateinit var tvHistorial: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_acciones)

        val usuario = intent.getStringExtra("usu") ?: "Usuario"
        txtBienvenida = findViewById(R.id.bienvenido)
        txtBienvenida.text = "¡Bienvenido $usuario!"

        btnVolver = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }

        tvHistorial = findViewById(R.id.tvHistorial)
        tvHistorial.text = "Cargando historial..."

        accionesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nodes = snapshot.children.toList().asReversed()

                if (nodes.isEmpty()) {
                    tvHistorial.text = "No hay acciones registradas."
                    return
                }

                val sb = StringBuilder()
                for (child in nodes) {

                    val accionText = child.child("accion").getValue(String::class.java) ?: ""
                    val fecha = child.child("fecha").getValue(String::class.java) ?: ""
                    val hora = child.child("hora").getValue(String::class.java) ?: ""


                    val parts = listOfNotNull(
                        if (accionText.isNotEmpty()) accionText else null,
                        if (fecha.isNotEmpty()) fecha else null,
                        if (hora.isNotEmpty()) hora else null
                    )
                    val line = if (parts.isNotEmpty()) parts.joinToString("—") else "(registro vacío)"

                    sb.append("• ").append(line).append("\n\n")
                }

                tvHistorial.text = sb.toString().trim()
            }

            override fun onCancelled(error: DatabaseError) {
                tvHistorial.text = "Error cargando historial: ${error.message}"
                Toast.makeText(this@Acciones, "Error cargando historial: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        accionesRef.addValueEventListener(accionesListener as ValueEventListener)
    }

    override fun onStop() {
        super.onStop()
        accionesListener?.let { accionesRef.removeEventListener(it) }
    }
}
