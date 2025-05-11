package com.personeriatocancipa.app.ui.admin.reports

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.personeriatocancipa.app.R
import com.personeriatocancipa.app.data.repository.FirebaseDateRepository
import com.personeriatocancipa.app.databinding.ActivityReportsBinding
import com.personeriatocancipa.app.domain.model.Date
import com.personeriatocancipa.app.domain.usecase.GetDatesUseCase
import kotlinx.coroutines.launch

class ReportsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportsBinding
    private val getDatesUseCase = GetDatesUseCase(FirebaseDateRepository())
    private lateinit var chartHelper: ChartHelper
    private lateinit var pdfHelper: PDFHelper
    private lateinit var dates: List<Date>
    private var chartsLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        chartHelper = ChartHelper(binding.barChart, binding.pieChart, binding.lineChart)
        pdfHelper = PDFHelper()
        getDates()
        initComponents()
    }

    private fun initUI() {
        if (dates.isEmpty()) return
        chartHelper.setupBarChart(dates)
        chartHelper.setupPieChart(dates)
        chartHelper.setupLineChart(dates)
        chartsLoaded = true
    }

    private fun initComponents() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivDownload.setOnClickListener {
            exportToPDF()
        }
    }

    private fun getDates() {
        lifecycleScope.launch {
            val result = getDatesUseCase.execute("", "")
            result.onSuccess {
                dates = it
                initUI()
            }
        }
    }

    private fun exportToPDF() {
        if (!chartsLoaded) {
            Toast.makeText(this, "Espere a que carguen las graficas", Toast.LENGTH_SHORT).show()
            return
        }
        val barCharBitMap = binding.barChart.chartBitmap
        val pieCharBitMap = binding.pieChart.chartBitmap
        val lineCharBitMap = binding.lineChart.chartBitmap

        val result = pdfHelper.exportToPDF(dates, barCharBitMap, pieCharBitMap, lineCharBitMap)
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

}