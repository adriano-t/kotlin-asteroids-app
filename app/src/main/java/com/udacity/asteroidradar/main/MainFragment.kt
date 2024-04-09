package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.main.MainFragmentDirections

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidsRadarAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //initialize recycler
        adapter = AsteroidsRadarAdapter(AsteroidsRadarAdapter.OnClickListener {asteroid ->
            println("Asteroid Clicked!")
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(context)
        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Called immediately after onCreateView() has returned, and fragment's
     * view hierarchy has been created.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAsteroids(viewModel.asteroidsNextWeek)

        viewModel.pictureOfDay.observe(viewLifecycleOwner) {apod ->
            Picasso.get().load(apod.url).into(binding.activityMainImageOfTheDay)
            binding.activityMainImageOfTheDay.contentDescription = apod.explanation
        }
    }

    private fun showAsteroids(asteroidsList: LiveData<List<Asteroid>>) {
        asteroidsList.observe(viewLifecycleOwner) { asteroids ->
            adapter.submitList(asteroids)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val asteroidsList = when (item.itemId) {
            R.id.show_next_week_menu -> viewModel.asteroidsNextWeek
            R.id.show_today_menu -> viewModel.asteroidsToday
            else -> viewModel.asteroids
        }
        showAsteroids(asteroidsList)
        return true
    }


}
