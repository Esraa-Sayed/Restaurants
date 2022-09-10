package com.esraa.restaurants.UI.Feature.Restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esraa.restaurants.Domain.Entity.Restaurant
import com.esraa.restaurants.R
import com.esraa.restaurants.databinding.FragmentRestaurantDetailsBinding

private const val ARG_RESTAURANT = "restaurant"
class RestaurantDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var restaurant:Restaurant? = null
    private var binding:FragmentRestaurantDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {bundle ->
           bundle.getParcelable<Restaurant>(ARG_RESTAURANT).also {
                restaurant = it
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater,container,false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       restaurant?.address.let{
           if (it.isNullOrEmpty())
               binding!!.address.text = getString(R.string.unKnown)
            binding!!.address.text = it
        }
        restaurant?.name.let{
            if (it.isNullOrEmpty())
                binding!!.Name.text = getString(R.string.unKnown)
            binding!!.Name.text = it
        }
        restaurant?.city.let{
            if (it.isNullOrEmpty())
                binding!!.City.text = getString(R.string.unKnown)
            binding!!.City.text = it
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RestaurantDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(restaurant: Restaurant?) =
            RestaurantDetails().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_RESTAURANT, restaurant)

                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}