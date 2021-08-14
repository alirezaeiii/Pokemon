package se.sample.android.refactoring.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import se.sample.android.refactoring.BR
import se.sample.android.refactoring.R
import se.sample.android.refactoring.databinding.FragmentDetailBinding
import se.sample.android.refactoring.domain.Pokemon
import se.sample.android.refactoring.util.Resource
import se.sample.android.refactoring.util.bindText
import se.sample.android.refactoring.util.setupActionBar
import se.sample.android.refactoring.viewmodels.DetailViewModel
import javax.inject.Inject

class DetailFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var factory: DetailViewModel.Factory

    @Inject
    lateinit var pokemon: Pokemon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        val binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            setVariable(BR.vm, viewModel)
            // Set the lifecycleOwner so DataBinding can observe LiveData
            lifecycleOwner = viewLifecycleOwner
            pokemon = this@DetailFragment.pokemon
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