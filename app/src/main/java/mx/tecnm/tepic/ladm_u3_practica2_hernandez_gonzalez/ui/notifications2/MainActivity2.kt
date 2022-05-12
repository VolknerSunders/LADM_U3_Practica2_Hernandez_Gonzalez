package mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.ui.notifications2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    var idSeleccionado = ""
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        idSeleccionado = intent.extras!!.getString("idSeleccionado")!!

        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("subdepartamentos")
            .document(idSeleccionado)
            .get()
            .addOnSuccessListener {
                binding.idSubDep.setText(it.getString("idsubdepto"))
                binding.idedifSUB.setText(it.getString("idedificio"))
                binding.pisoSUB.setText(it.getString("piso"))

            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage(it.message)
                    .show()
            }
        binding.regresarSub.setOnClickListener {
            finish()
        }

        binding.updateSub.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            baseRemota.collection("subdepartamentos")
                .document(idSeleccionado)
                .update("idsubdepto",binding.idSubDep.text.toString(),
                    "idedificio", binding.idedifSUB.text.toString(), "piso",binding.pisoSUB.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this,"SE ACTUALIZO CON EXITO", Toast.LENGTH_LONG)
                        .show()
                    binding.idSubDep.setText("")
                    binding.idedifSUB.setText("")
                    binding.pisoSUB.setText("")
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }
    }
}