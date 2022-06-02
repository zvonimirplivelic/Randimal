package com.zvonimirplivelic.randimal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.zvonimirplivelic.randimal.R
import com.zvonimirplivelic.randimal.RandimalViewModel
import com.zvonimirplivelic.randimal.util.Resource
import de.hdodenhof.circleimageview.CircleImageView
import timber.log.Timber

class AnimalInfoFragment : Fragment() {

    private lateinit var viewModel: RandimalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animal_info, container, false)

        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        val fabRefresh: FloatingActionButton = view.findViewById(R.id.fab_get_animal_info)

        val ivImage: CircleImageView = view.findViewById(R.id.iv_image)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvLatinName: TextView = view.findViewById(R.id.tv_latin_name)
        val tvType: TextView = view.findViewById(R.id.tv_type)
        val tvActiveTime: TextView = view.findViewById(R.id.tv_active_time)
        val tvLength: TextView = view.findViewById(R.id.tv_length)
        val tvWeight: TextView = view.findViewById(R.id.tv_weight)
        val tvLifespan: TextView = view.findViewById(R.id.tv_lifespan)
        val tvHabitat: TextView = view.findViewById(R.id.tv_habitat)
        val tvGeoRange: TextView = view.findViewById(R.id.tv_geo_range)
        val tvDiet: TextView = view.findViewById(R.id.tv_diet)

        viewModel = ViewModelProvider(this)[RandimalViewModel::class.java]

        viewModel.getAnimalInformation()

        fabRefresh.setOnClickListener {
            viewModel.getAnimalInformation()
        }

        viewModel.randomAnimalInfo.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    progressBar.isVisible = false
                    response.data?.let { animalInfoResponse ->

                        Picasso.get()
                            .load(animalInfoResponse.imageLink)
                            .noFade()
                            .resize(640, 640)
                            .into(ivImage)

                        tvName.text = animalInfoResponse.name
                        tvLatinName.text = animalInfoResponse.latinName
                        tvType.text = animalInfoResponse.animalType
                        tvActiveTime.text = animalInfoResponse.activeTime
                        tvLength.text = animalInfoResponse.lengthMax
                        tvWeight.text = animalInfoResponse.weightMax
                        tvLifespan.text = animalInfoResponse.lifespan
                        tvHabitat.text = animalInfoResponse.habitat
                        tvGeoRange.text = animalInfoResponse.geoRange
                        tvDiet.text = animalInfoResponse.diet
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