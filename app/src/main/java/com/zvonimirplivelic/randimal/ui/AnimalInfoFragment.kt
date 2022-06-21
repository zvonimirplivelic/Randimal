package com.zvonimirplivelic.randimal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.zvonimirplivelic.randimal.R
import com.zvonimirplivelic.randimal.RandimalViewModel
import com.zvonimirplivelic.randimal.util.Constants.FEET_TO_METERS
import com.zvonimirplivelic.randimal.util.Constants.POUNDS_TO_KILOS
import com.zvonimirplivelic.randimal.util.Resource
import de.hdodenhof.circleimageview.CircleImageView
import java.math.RoundingMode
import java.text.DecimalFormat

class AnimalInfoFragment : Fragment() {

    private lateinit var viewModel: RandimalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_animal_info, container, false)

        val clDataLayout: ConstraintLayout = view.findViewById(R.id.cl_data_layout)
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
                    clDataLayout.isVisible = true

                    response.data?.let { animalInfoResponse ->

                        Picasso.get()
                            .load(animalInfoResponse.imageLink)
                            .centerCrop()
                            .fit()
                            .placeholder(R.drawable.ic_paw)
                            .into(ivImage)

                        tvName.text = animalInfoResponse.name
                        tvLatinName.text = animalInfoResponse.latinName

                        tvType.text = resources.getString(
                            R.string.animal_type_string,
                            animalInfoResponse.animalType
                        )

                        tvActiveTime.text = resources.getString(
                            R.string.active_time_string,
                            animalInfoResponse.activeTime
                        )

                        tvLength.text = resources.getString(
                            R.string.length_string,
                            convertHeight(animalInfoResponse.lengthMin),
                            convertHeight(animalInfoResponse.lengthMax)
                        )
                        tvWeight.text = resources.getString(
                            R.string.weight_string,
                            convertWeight(animalInfoResponse.lengthMin),
                            convertWeight(animalInfoResponse.lengthMax)
                        )

                        tvLifespan.text = resources.getString(
                            R.string.lifespan_string,
                            animalInfoResponse.lifespan
                        )

                        tvHabitat.text = resources.getString(
                            R.string.habitat_string,
                            animalInfoResponse.habitat
                        )

                        tvGeoRange.text = resources.getString(
                            R.string.geo_range_string,
                            animalInfoResponse.geoRange
                        )

                        tvDiet.text = resources.getString(
                            R.string.diet_string,
                            animalInfoResponse.diet
                        )
                    }
                }

                is Resource.Error -> {
                    progressBar.isVisible = false
                    clDataLayout.isVisible = false
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    progressBar.isVisible = true
                    clDataLayout.isVisible = false
                }
            }
        }

        return view
    }

    private fun convertHeight(value: String): String {

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        val convertedHeight = (value.toDouble() / FEET_TO_METERS)
        val roundedValue = df.format(convertedHeight)

        return roundedValue.toString()
    }

    private fun convertWeight(value: String): String {

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        val convertedWeight = (value.toDouble() / POUNDS_TO_KILOS)
        val roundedValue = df.format(convertedWeight)

        return roundedValue.toString()
    }
}