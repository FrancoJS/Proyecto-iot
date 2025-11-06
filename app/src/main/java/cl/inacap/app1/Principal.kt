package cl.inacap.app1

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.jvm.java

class Principal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)

        val btnini: Button = findViewById(R.id.btnini)
        val txtusu: EditText = findViewById(R.id.txtusu)
        val txtpas: EditText = findViewById(R.id.txtpas)

        val database = FirebaseDatabase.getInstance().getReference("Usuarios")

        btnini.setOnClickListener {
            val usuingresado = txtusu.text.toString().trim()
            val pasingresado = txtpas.text.toString().trim()

            if(usuingresado.isNotEmpty() && pasingresado.isNotEmpty()){
                validarusuario(usuingresado, pasingresado)
            }else{
                Toast.makeText(this@Principal, "Porfavor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarusuario(usuingresado:String, pasingresado:String){
        val database = FirebaseDatabase.getInstance().getReference("Usuarios")

        database.orderByChild("nombre").equalTo(usuingresado).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()) {

                    for (dato in snapshot.children) {
                        val usu = dato.child("nombre").value.toString()
                        val pas = dato.child("password").value.toString()
                        val ema = dato.child("email").value.toString()
                        val tip = dato.child("tipo").value.toString()

                        if (pasingresado == pas) {
                            Toast.makeText(this@Principal, "Acceso Correcto", Toast.LENGTH_SHORT)
                                .show()

//                            val i = Intent(this@Principal, Menu::class.java)
//                            i.putExtra("usu", usu)
//                            i.putExtra("pas", pas)
//                            i.putExtra("ema", ema)
//                            i.putExtra("tip", tip)
//                            startActivity(i)
//                            finish()
                            return
                        } else {
                            Toast.makeText(
                                this@Principal,
                                "Contraseña Incorrecta",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                    }

                }else{
                    Toast.makeText(this@Principal, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Principal, "Error de inicio de sesion", Toast.LENGTH_SHORT).show()
            }
        })
    }

}