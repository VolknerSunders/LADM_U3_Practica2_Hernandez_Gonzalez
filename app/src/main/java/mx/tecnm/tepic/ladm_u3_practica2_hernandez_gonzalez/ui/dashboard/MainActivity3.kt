package mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.databinding.ActivityMain2Binding
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    var idSeleccionado=""
    lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        idSeleccionado = intent.extras!!.getString("idSeleccionado")!!

        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("area")
            .document(idSeleccionado)
            .get()
            .addOnSuccessListener {
                binding.descAREA.setText(it.getString("descripcion"))
                binding.divAREA.setText(it.getString("division"))
                binding.cantAREA.setText(it.getString("cantidad_empleados"))

            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage(it.message)
                    .show()
            }
        binding.regresarArea.setOnClickListener {
            finish()
        }

        binding.updateAREA.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            baseRemota.collection("area")
                .document(idSeleccionado)
                .update("descripcion",binding.descAREA.text.toString(),
                    "division", binding.divAREA.text.toString(), "cantidad_empleados",binding.cantAREA.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this,"SE ACTUALIZO CON EXITO", Toast.LENGTH_LONG)
                        .show()
                    binding.descAREA.setText("")
                    binding.divAREA.setText("")
                    binding.cantAREA.setText("")
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }
    }
}