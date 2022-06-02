package com.zvonimirplivelic.randimal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.zvonimirplivelic.randimal.R
import com.zvonimirplivelic.randimal.RandimalViewModel
import com.zvonimirplivelic.randimal.util.Resource
import timber.log.Timber

class AnimalInfoFragment : Fragment() {

    private lateinit var viewModel: RandimalViewModel

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animal_info, container, false)

        progressBar = view.findViewById(R.id.progress_bar)

        viewModel = ViewModelProvider(this)[RandimalViewModel::class.java]

        viewModel.getAnimalInformation()

        viewModel.randomAnimalInfo.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar.isVisible = false
                    response.data?.let { animalInfoResponse ->

                        Timber.d("AnimalInfoResponse $animalInfoResponse")

                    }
                }

                is Resource.Error -> {
                    progressBar.isVisible = false
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    progressBar.isVisible = true
                }
            }
        }

        return view
    }
}