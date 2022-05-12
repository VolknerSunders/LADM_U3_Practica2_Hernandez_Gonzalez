package mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    val baseRemota = FirebaseFirestore.getInstance()
    var iddelarea = ""
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.insertDescSUB.setOnClickListener {
            baseRemota.collection("area")
                .whereEqualTo("descripcion", binding.descSub.text.toString())
                .get()
                .addOnSuccessListener {
                    for (documento in it) {
                        val datos = hashMapOf(
                            "idsubdepto" to binding.idSubDep.text.toString(),
                            "idedificio" to binding.idedifSUB.text.toString(),
                            "piso" to binding.pisoSUB.text.toString(),
                            "idarea" to documento.id
                        )

                        baseRemota.collection("subdepartamentos")
                            .add(datos)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "EXITO SE INSERTO",
                                    Toast.LENGTH_LONG
                                )
                                    .show()

                                binding.idSubDep.setText("")
                                binding.idedifSUB.setText("")
                                binding.pisoSUB.setText("")
                                binding.divSub.setText("")
                                binding.descSub.setText("")
                            }
                            .addOnFailureListener {
                                AlertDialog.Builder(requireContext())
                                    .setMessage(it.message)
                                    .show()
                            }
                    }
                }
        }

        binding.insertDivSUB.setOnClickListener {
            baseRemota.collection("area")
                .whereEqualTo("division", binding.divSub.text.toString())
                .get()
                .addOnSuccessListener {
                    for (documento in it) {
                        val datos = hashMapOf(
                            "idsubdepto" to binding.idSubDep.text.toString(),
                            "idedificio" to binding.idedifSUB.text.toString(),
                            "piso" to binding.pisoSUB.text.toString(),
                            "idarea" to documento.id
                        )

                        baseRemota.collection("subdepartamentos")
                            .add(datos)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "EXITO SE INSERTO",
                                    Toast.LENGTH_LONG
                                )
                                    .show()

                                binding.idSubDep.setText("")
                                binding.idedifSUB.setText("")
                                binding.pisoSUB.setText("")
                                binding.divSub.setText("")
                                binding.descSub.setText("")
                            }
                            .addOnFailureListener {
                                AlertDialog.Builder(requireContext())
                                    .setMessage(it.message)
                                    .show()
                            }
                    }
                }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}