package mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.ui.dashboard

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.databinding.FragmentDashboardBinding
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.ui.notifications2.MainActivity2
import java.lang.NullPointerException
import kotlin.coroutines.EmptyCoroutineContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

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
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        baseRemota
            .collection("area")
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
                    var cadena = "idArea: ${documento.id}\n" +
                            "descripcion: ${documento.getString("descripcion")}\n" +
                            "Division: ${documento.getString("division")}\n" +
                            "Cantidad de empleados: ${documento.getString("cantidad_empleados")}\n"
                    arreglo.add(cadena)
                    listaIDs.add(documento.id.toString())
                }

                binding.ListaArea.adapter = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.simple_list_item_1, arreglo
                )

                binding.ListaArea.setOnItemClickListener { adapterView, view, posicion, l ->
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
        binding.consultarArea.setOnClickListener {
            var consultadearea= baseRemota.collection("area").whereEqualTo("descripcion", binding.descAREA.text.toString())
            if(!binding.descAREA.text.toString().equals("")) {
                consultadearea= baseRemota.collection("area").whereEqualTo("descripcion", binding.descAREA.text.toString())
            }else if(!binding.divAREA.text.toString().equals("")) {
                consultadearea = baseRemota.collection("area").whereEqualTo("division", binding.divAREA.text.toString())
            }else if (binding.divAREA.text.toString().equals("") && binding.descAREA.text.toString().equals("")){
                Toast.makeText(
                    requireContext(),
                    "NO TIENES NADA EN LOS CAMPOS",
                    Toast.LENGTH_LONG
                )
                    .show()
                return@setOnClickListener
            } else{
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
            consultadearea.get()
                .addOnSuccessListener {
                    for (documento in it) {
                        var cadena = "idArea: ${documento.id}\n" +
                                "descripcion: ${documento.getString("descripcion")}\n" +
                                "Division: ${documento.getString("division")}\n" +
                                "Cantidad de empleados: ${documento.getString("cantidad_empleados")}\n"
                        arreglo.add(cadena)
                        listaIDs.add(documento.id.toString())
                    }
                    Toast.makeText(
                        requireContext(),
                        "EXITO EN LA CONSULTA",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    binding.descAREA.setText("")
                    binding.divAREA.setText("")
                    binding.ListaArea.adapter = ArrayAdapter<String>(
                        requireContext(),
                        R.layout.simple_list_item_1, arreglo
                    )

                    binding.ListaArea.setOnItemClickListener { adapterView, view, posicion, l ->
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
        baseRemota.collection("area")
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
        var otraVentana = Intent(requireContext(), MainActivity3::class.java)

        otraVentana.putExtra("idSeleccionado",idSeleccionado)
        startActivity(otraVentana)
    }
}