package com.catnip.egroceries.presentation.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.catnip.egroceries.R
import com.catnip.egroceries.databinding.FragmentHomeBinding
import com.catnip.egroceries.model.Product
import com.catnip.egroceries.presentation.feature.detailproduct.DetailProductActivity
import com.catnip.egroceries.presentation.feature.home.adapter.HomeAdapter
import com.catnip.egroceries.presentation.feature.settings.SettingsDialogFragment
import com.catnip.egroceries.utils.AssetWrapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    // field injection
    @Inject
    lateinit var assetWrapper: AssetWrapper

    private val adapter: HomeAdapter by lazy {
        HomeAdapter(onProductClicked = {
            navigateToDetail(it)
        }, onSettingsClicked = {
                Toast.makeText(requireContext(), assetWrapper.getString(R.string.text_toast), Toast.LENGTH_SHORT).show()
                openSettingDialog()
            }, onCategoriesClicked = {
                viewModel.setSelectedCategory(it.slug)
            })
    }

    private fun openSettingDialog() {
        SettingsDialogFragment().show(childFragmentManager, null)
    }

    private fun navigateToDetail(item: Product) {
        DetailProductActivity.startActivity(requireContext(), item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        fetchData()
    }

    private fun fetchData() {
        viewModel.homeData.observe(viewLifecycleOwner) {
            adapter.submitData(it)
        }
    }

    private fun setupList() {
        binding.rvHome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }
    }
}
