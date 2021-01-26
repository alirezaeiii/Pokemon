package se.appshack.android.refactoring.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import se.appshack.android.refactoring.BR
import se.appshack.android.refactoring.R
import se.appshack.android.refactoring.databinding.FragmentMainBinding
import se.appshack.android.refactoring.util.Resource
import se.appshack.android.refactoring.util.setupActionBar
import se.appshack.android.refactoring.viewmodels.MainViewModel
import javax.inject.Inject

class MainFragment @Inject
constructor() // Required empty public constructor
    : DaggerFragment() {

    @Inject
    lateinit var factory: MainViewModel.Factory

    /**
     * RecyclerView Adapter for converting a list of pokemon to items.
     */
    private lateinit var viewModelAdapter: MainAdapter

    private var binding: FragmentMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {

            val viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

            binding = FragmentMainBinding.inflate(inflater, container, false).apply {
                setVariable(BR.vm, viewModel)
                // Set the lifecycleOwner so DataBinding can observe LiveData
                lifecycleOwner = viewLifecycleOwner
            }

            viewModel.liveData.observe(viewLifecycleOwner, Observer { resource ->
                if (resource is Resource.Success) {
                    viewModelAdapter.submitList(resource.data)
                }
            })

            viewModelAdapter =
                MainAdapter(MainAdapter.OnClickListener { pokemon ->
                    val destination = MainFragmentDirections.actionMainFragmentToDetailFragment(pokemon)
                    with(findNavController()) {
                        currentDestination?.getAction(destination.actionId)?.let { navigate(destination) }
                    }
                })

            with(binding!!) {

                recyclerview.apply {
                    adapter = viewModelAdapter
                }

                (activity as AppCompatActivity).setupActionBar(toolbar) {
                    setTitle(R.string.title_fragment_main)
                }
            }
        }
        return binding?.root
    }
}