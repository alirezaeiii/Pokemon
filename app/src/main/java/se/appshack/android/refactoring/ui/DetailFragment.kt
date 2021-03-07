package se.appshack.android.refactoring.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import se.appshack.android.refactoring.BR
import se.appshack.android.refactoring.R
import se.appshack.android.refactoring.databinding.FragmentDetailBinding
import se.appshack.android.refactoring.util.Resource
import se.appshack.android.refactoring.util.bindText
import se.appshack.android.refactoring.util.setupActionBar
import se.appshack.android.refactoring.viewmodels.DetailViewModel
import javax.inject.Inject

class DetailFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var factory: DetailViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        val binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
            pokemon = arguments?.let { DetailFragmentArgs.fromBundle(it).pokemon }
        }

        with(binding) {

            (activity as AppCompatActivity).setupActionBar(toolbar) {
                setTitle(R.string.title_fragment_pokemon_details)
            }

            viewModel.liveData.observe(viewLifecycleOwner, Observer { resource ->
                if (resource is Resource.Success) {
                    pokemonDetails = resource.data?.pokemonDetail
                    pokemonSpecies.bindText(getString(R.string.species, resource.data?.genus))
                    pokemonTypes.bindText(getString(R.string.types, resource.data?.type))
                }
            })
        }

        return binding.root
    }
}