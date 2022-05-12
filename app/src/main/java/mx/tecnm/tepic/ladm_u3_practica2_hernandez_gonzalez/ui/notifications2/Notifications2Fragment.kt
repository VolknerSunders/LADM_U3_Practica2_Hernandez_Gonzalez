package mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.ui.notifications2

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.databinding.FragmentNotifications2Binding

class Notifications2Fragment : Fragment() {

    private var _binding: FragmentNotifications2Binding? = null
    val baseRemota = FirebaseFirestore.getInstance()
    var listaIDs = ArrayList<String>()

    var arreglo = ArrayList<String>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(Notifications2ViewModel::class.java)

        _binding = FragmentNotifications2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        baseRemota
            .collection("subdepartamentos")
            .addSnapshotListener { query, error ->
                if (error != null) {
                    //SI HUBO ERROR!
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }
                arreglo.clear()
                listaIDs.clear()
                for (documento in query!!) {
                    var cadena = "idSubdepartamento: ${documento.getString("idsubdepto")}\n" +
                            "IdEdificio: ${documento.getString("idedificio")}\n" +
                            "Piso: ${documento.getString("piso")}\n" +
                            "idarea: ${documento.getString("idarea")}\n"
                    arreglo.add(cadena)
                    listaIDs.add(documento.id.toString())
                }

                binding.ListaSub.adapter = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.simple_list_item_1, arreglo
                )
                binding.ListaSub.setOnItemClickListener { adapterView, view, posicion, l ->
                    val idSeleccionado = listaIDs.get(posicion)
                    AlertDialog.Builder(requireContext())
                        .setTitle("ATENCION")
                        .setMessage("Que deseas hacer?")
                        .setNeutralButton("ELIMINAR"){d,i->
                            eliminar(idSeleccionado)
                        }
                        .setPositiveButton("ACTUALIZAR"){d,i->
                            actualizar(idSeleccionado)
                        }
                        .setNegativeButton("ACEPTAR"){d,i->

                        }
                        .show()
                }

            }

        binding.consultarSub.setOnClickListener {
            var consultadesub= baseRemota.collection("subdepartamentos").whereEqualTo("idedificio", binding.idedifSUB.text.toString())

            if (binding.idedifSUB.text.toString().equals("") && binding.DivAreaEnSub.text.toString().equals("") && binding.descAreaEnSub.text.toString().equals("")){
                Toast.makeText(
                    requireContext(),
                    "NO TIENES NADA EN LOS CAMPOS",
                    Toast.LENGTH_LONG
                )
                    .show()
                return@setOnClickListener
            }else if (!binding.idedifSUB.text.toString().equals("")){
                Toast.makeText(
                    requireContext(),
                    "NO EXISTE",
                    Toast.LENGTH_LONG
                )
                    .show()
                return@setOnClickListener
            }
            arreglo.clear()
            listaIDs.clear()

            consultadesub.get()
                .addOnSuccessListener {
                    for (documento in it) {
                        var cadena = "idSubdepartamento: ${documento.getString("idsubdepto")}\n" +
                                "IdEdificio: ${documento.getString("idedificio")}\n" +
                                "Piso: ${documento.getString("piso")}\n" +
                                "idarea: ${documento.getString("idarea")}\n"
                        arreglo.add(cadena)
                        listaIDs.add(documento.id.toString())
                    }
                    Toast.makeText(
                        requireContext(),
                        "EXITO EN LA CONSULTA",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    binding.DivAreaEnSub.setText("")
                    binding.descAreaEnSub.setText("")
                    binding.idedifSUB.setText("")
                    binding.ListaSub.adapter = ArrayAdapter<String>(
                        requireContext(),
                        R.layout.simple_list_item_1, arreglo
                    )

                    binding.ListaSub.setOnItemClickListener { adapterView, view, posicion, l ->
                        val idSeleccionado = listaIDs.get(posicion)
                        AlertDialog.Builder(requireContext())
                            .setTitle("ATENCION")
                            .setMessage("Que deseas hacer?")
                            .setNeutralButton("ELIMINAR"){d,i->
                                eliminar(idSeleccionado)
                            }
                            .setPositiveButton("ACTUALIZAR"){d,i->
                                actualizar(idSeleccionado)
                            }
                            .setNegativeButton("ACEPTAR"){d,i->

                            }
                            .show()
                    }
                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun eliminar(idSeleccionado: String) {
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("subdepartamentos")
            .document(idSeleccionado)
            .delete()
            .addOnFailureListener {
                Toast.makeText(requireContext(),"SE ELIMINO ID CORRECTAMENTE", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                AlertDialog.Builder(requireContext())
                    .setMessage(it.message)
                    .show()
            }
    }

    private fun actualizar(idSeleccionado: String) {
        var otraVentana = Intent(requireContext(), MainActivity2::class.java)

        otraVentana.putExtra("idSeleccionado",idSeleccionado)
        startActivity(otraVentana)
    }
}