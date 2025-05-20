package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.databinding.FragmentPqrsMenuBinding

class PqrsMenuFragment : Fragment() {
    private var _binding: FragmentPqrsMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPqrsMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnList.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            // Verifica rol del usuario
            val db = FirebaseDatabase.getInstance()

            db.getReference("AdminData").child(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    // Es administrador
                    findNavController().navigate(R.id.pqrsAdminListFragment)
                } else {
                    // Es usuario normal
                    findNavController().navigate(R.id.pqrsListFragment)
                }
            }.addOnFailureListener {
                Snackbar.make(binding.root, "Error al verificar el rol", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.btnCreate.setOnClickListener {
            findNavController().navigate(R.id.pqrsCreateFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
