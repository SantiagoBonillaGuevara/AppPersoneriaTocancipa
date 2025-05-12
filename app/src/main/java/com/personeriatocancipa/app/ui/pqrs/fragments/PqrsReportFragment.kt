package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.personeriatocancipa.app.databinding.FragmentPqrsReportBinding

class PqrsReportFragment : Fragment() {
    private var _binding: FragmentPqrsReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentPqrsReportBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnExportCsv.setOnClickListener {
            // TODO: ExportPqrsReportUseCase("csv")
            Toast.makeText(requireContext(),"CSV exportado",Toast.LENGTH_SHORT).show()
        }
        binding.btnExportPdf.setOnClickListener {
            // TODO: ExportPqrsReportUseCase("pdf")
            Toast.makeText(requireContext(),"PDF exportado",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
