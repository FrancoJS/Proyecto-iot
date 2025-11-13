package cl.inacap.app1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Manipular: AppCompatActivity (){
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val alarmaRef: DatabaseReference = database.getReference("alarmaActivada")

    private var alarmaListener: ValueEventListener? = null

    fun setAlarma(
        estado: Boolean,
        onSuccess: (() -> Unit)? = null,
        onError: ((Exception)-> Unit)? = null){

        alarmaRef.setValue(estado).addOnSuccessListener {
            onSuccess?.invoke()
        }.addOnFailureListener { exception ->
            onError?.invoke(exception) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_manipular)

        val usuario = intent.getStringExtra("usu")

        val txtBienvenida = findViewById<TextView>(R.id.bienvenido)

        txtBienvenida.text = "Menu Manipular, ¡Bienvenido $usuario!"

        val btnVolver: Button = findViewById(R.id.btnVolver)

        val btnActivar = findViewById<Button>(R.id.btnOn)
        val btnDesactivar = findViewById<Button>(R.id.btnOff)

        val tvEstado = findViewById<TextView>(R.id.tvEstado)

        btnActivar.setOnClickListener {
            setAlarma(true,
                        onSuccess = {
                            Toast.makeText(this, "Alarma ACTIVADA", Toast.LENGTH_SHORT).show()
                        },
                        onError = { exception ->
                            Toast.makeText(this, "Error al activar alarma: ${exception.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                )
        }

        btnDesactivar.setOnClickListener {
            setAlarma(false,
                onSuccess = {
                    Toast.makeText(this, "Alarma DESACTIVADA", Toast.LENGTH_SHORT).show()
                },
                onError = { exception ->
                    Toast.makeText(this, "Error al desactivar alarma: ${exception.message}",
                        Toast.LENGTH_SHORT).show()
                }
            )
        }

        alarmaListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val estado = snapshot.getValue(Boolean::class.java) ?: false
                tvEstado.text = if (estado) "Estado Alarma: ACTIVADA" else "Estado Alarma: DESACTIVADA"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Manipular, "Error leyendo estado: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        alarmaRef.addValueEventListener(alarmaListener as ValueEventListener)



        btnVolver.setOnClickListener {
            finish()
        }

    }

    override fun onStop() {
        super.onStop()
        alarmaListener?.let {alarmaRef.removeEventListener(it)}
    }

}