package cl.inacap.app1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CambiarContrasena : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrasena)

        // Elementos del layout
        val txtContraActual: EditText = findViewById(R.id.contraseña)
        val txtNuevaContra: EditText = findViewById(R.id.nuevaContraseña)
        val btnConfirmar: Button = findViewById(R.id.btnconfirmar)
        val btnVolver: Button = findViewById(R.id.btnvolver)

        // Conexión a Firebase -> tabla Usuarios
        val database = FirebaseDatabase.getInstance().getReference("Usuarios")

        // Obtener el usuario enviado desde el menú
        val usuario = intent.getStringExtra("usu")

        // Cuando el usuario presiona CONFIRMAR
        btnConfirmar.setOnClickListener {

            val contraActual = txtContraActual.text.toString().trim()
            val nuevaContra = txtNuevaContra.text.toString().trim()

            // Validación: ¿vino el usuario?
            if (usuario.isNullOrEmpty()) {
                Toast.makeText(this, "Error: usuario no recibido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación: ¿campos vacíos?
            if (contraActual.isEmpty() || nuevaContra.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buscar usuario en Firebase
            database.orderByChild("nombre").equalTo(usuario)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        // Verificar si el usuario existe
                        if (!snapshot.exists()) {
                            Toast.makeText(
                                this@CambiarContrasena,
                                "Usuario no encontrado",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        // Recorrer los resultados (normalmente solo habrá uno)
                        for (dato in snapshot.children) {

                            // Leer contraseña actual desde Firebase
                            val passwordActualDB =
                                dato.child("password").value.toString()

                            // Comparar contra actual con lo que escribió el usuario
                            if (passwordActualDB != contraActual) {
                                Toast.makeText(
                                    this@CambiarContrasena,
                                    "La contraseña actual es incorrecta",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return
                            }

                            // Actualizar contraseña
                            dato.ref.child("password").setValue(nuevaContra)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@CambiarContrasena,
                                        "Contraseña actualizada con éxito",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish() // cerrar pantalla
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        this@CambiarContrasena,
                                        "Error al actualizar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@CambiarContrasena,
                            "Error al conectarse con Firebase",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        // Acción del botón VOLVER
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
