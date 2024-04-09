package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

/*
    Steps to implement a ListAdapter:
        1. Create a Custom ViewHolder
        2. Create a Diff Callback
        3. Inherit from ListAdapter using the Custom ViewHolder and Diff Callback
        4. Override onCreateViewHolder and onBindViewHolder methods
*/

class AsteroidsRadarAdapter (private val onClickListener: OnClickListener)
    : ListAdapter<Asteroid, AsteroidsRadarAdapter.AsteroidsViewHolder>(DiffCallback) {

    /**
     * Actual View Holder, that inherits from RecyclerView and binds the AsteroidItem layout
     */
    class AsteroidsViewHolder(private val binding: AsteroidItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        // bind the layout variables
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            // binding.clickListener = clickListener
            binding.itemIcon.setImageResource(
                when(asteroid.isPotentiallyHazardous) {
                    true -> R.drawable.ic_status_potentially_hazardous
                    false -> R.drawable.ic_status_normal
                }
            )


            binding.itemIcon.contentDescription =  binding.root.resources.getString(
                when(asteroid.isPotentiallyHazardous) {
                    true -> R.string.hazardous_asteroid
                    false -> R.string.not_hazardous_asteroid
                }
            )

            val format = binding.root.context.getString(R.string.astronomical_unit_format)
            binding.itemDistance.text = String.format(format, asteroid.distanceFromEarth)
            binding.executePendingBindings()
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AsteroidsViewHolder(AsteroidItemBinding.inflate(inflater, parent, false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(asteroid)
        }
        holder.bind(asteroid)
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Asteroid]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Asteroid]
     */
    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        private fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

}


