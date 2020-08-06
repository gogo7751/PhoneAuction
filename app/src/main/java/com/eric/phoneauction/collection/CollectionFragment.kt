package com.eric.phoneauction.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.eric.phoneauction.databinding.FragmentCollectionBinding
import com.eric.phoneauction.ext.getVmFactory

/**
 * A simple [Fragment] subclass.
 */
class CollectionFragment : Fragment() {

    val viewModel: CollectionViewModel by viewModels { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCollectionBinding.inflate(
            inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = CollectionAdapter(viewModel)
        adapter.setHasStableIds(true)
        binding.recyclerviewCollection.adapter = adapter

        viewModel.liveCollections.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isEmpty()) {
                    binding.textNoContent.visibility = View.VISIBLE
                }
                else {
                    binding.textNoContent.visibility = View.GONE
                    adapter.submitList(it)
                }
            }
        })

        binding.imageCollectionBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

}
