package com.personeriatocancipa.app.ui.pqrs.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.personeriatocancipa.app.data.repository.FirebasePqrsRepository
import com.personeriatocancipa.app.databinding.FragmentPqrsDetailBinding
import com.personeriatocancipa.app.domain.usecase.GetPqrsByIdUseCase
import kotlinx.coroutines.launch
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

class PqrsDetailFragment : Fragment() {

    private var _b: FragmentPqrsDetailBinding? = null
    private val b get() = _b!!
    private val args: PqrsDetailFragmentArgs by navArgs()
    private val detailUseCase = GetPqrsByIdUseCase(FirebasePqrsRepository())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _b = FragmentPqrsDetailBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            detailUseCase.execute(args.pqrsId).onSuccess { p ->
                b.tvType.text = p.type
                b.tvTitle.text = p.title
                b.tvDescription.text = p.description
                val formatted = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(p.date))
                b.tvDate.text = formatted
            }
        }

        b.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
