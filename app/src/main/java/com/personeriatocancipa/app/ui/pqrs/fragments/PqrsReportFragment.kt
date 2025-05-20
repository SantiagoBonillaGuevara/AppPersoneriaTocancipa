package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.personeriatocancipa.app.databinding.FragmentPqrsReportBinding
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.domain.usecase.GetAllPqrsUseCase
import com.personeriatocancipa.app.ui.common.export.CsvExporter
import com.personeriatocancipa.app.ui.common.export.PdfExporter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PqrsReportFragment : Fragment() {
    private var _b: FragmentPqrsReportBinding? = null
    private val b get() = _b!!
    private val allUseCase = GetAllPqrsUseCase(FirebasePqrsRepository())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentPqrsReportBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b.btnExportCsv.setOnClickListener {
            lifecycleScope.launch {
                val list = allUseCase().first()
                CsvExporter(requireContext()).export(list, "pqrs.csv")
                Snackbar.make(b.root, "CSV exportado", Snackbar.LENGTH_SHORT).show()
            }
        }

        b.btnExportPdf.setOnClickListener {
            lifecycleScope.launch {
                val list = allUseCase().first()
                PdfExporter(requireContext()).export(list, "pqrs.pdf")
                Snackbar.make(b.root, "PDF exportado", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
