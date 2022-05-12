package mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.MainActivity
import mx.tecnm.tepic.ladm_u3_practica2_hernandez_gonzalez.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    val baseRemota = FirebaseFirestore.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.insertAREA.setOnClickListener {
            val datos = hashMapOf(
                "descripcion" to binding.descAREA.text.toString(),
                "division" to binding.divAREA.text.toString(),
                "cantidad_empleados" to binding.cantAREA.text.toString()
                )


            baseRemota.collection("area")
                .add(datos)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"EXITO SE INSERTO", Toast.LENGTH_LONG)
                        .show()

                    binding.descAREA.setText("")
                    binding.divAREA.setText("")
                    binding.cantAREA.setText("")
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
}