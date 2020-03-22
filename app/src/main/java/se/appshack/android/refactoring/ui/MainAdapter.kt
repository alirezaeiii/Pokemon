package se.appshack.android.refactoring.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import se.appshack.android.refactoring.databinding.ViewholderPokemonListBinding
import se.appshack.android.refactoring.domain.Pokemon
import se.appshack.android.refactoring.util.layoutInflater

class MainAdapter(
    private val callback: OnClickListener
) : ListAdapter<Pokemon, MainAdapter.MainViewHolder>(DiffCallback) {

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainViewHolder.from(parent)

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position), callback)
    }

    /**
     * ViewHolder for Post items. All work is done by data binding.
     */
    class MainViewHolder(private val binding: ViewholderPokemonListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: Pokemon, pokemonCallback: OnClickListener) {
            with(binding) {
                pokemonItem = pokemon
                callback = pokemonCallback
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): MainViewHolder {
                val binding = ViewholderPokemonListBinding.inflate(
                    parent.context.layoutInflater,
                    parent,
                    false
                )
                return MainViewHolder(binding)
            }
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Pokemon]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(
            oldItem: Pokemon, newItem: Pokemon
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: Pokemon, newItem: Pokemon
        ): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Pokemon]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Pokemon]
     */
    class OnClickListener(val clickListener: (pokemon: Pokemon) -> Unit) {
        fun onClick(pokemon: Pokemon) =
            clickListener(pokemon)
    }
}