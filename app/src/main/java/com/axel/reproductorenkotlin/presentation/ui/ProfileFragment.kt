package com.axel.reproductorenkotlin.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.axel.reproductorenkotlin.common.hide
import com.axel.reproductorenkotlin.common.show
import com.axel.reproductorenkotlin.databinding.FragmentProfileBinding
import com.axel.reproductorenkotlin.domain.usecase.GetUserUseCase
import com.axel.reproductorenkotlin.presentation.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject

/**
 * Fragment of users profile
 */
class ProfileFragment : Fragment() {

    private var fragmentProfileBinding: FragmentProfileBinding? = null
    private val binding get() = fragmentProfileBinding!!

    private val getUserUseCase: GetUserUseCase by inject()

    private val viewModel: ProfileViewModel by activityViewModels(
        factoryProducer = { ProfileViewModel.ProfileViewModelFactory(getUserUseCase) }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.playAnimation()
        setUpObserveUser()
    }

    private fun setUpObserveUser() {
        viewModel.getUserLiveData().observe(viewLifecycleOwner, {
            binding.progress.cancelAnimation()
            binding.progress.hide()
            binding.image.show()
            binding.name.show()
            binding.numberFollowers.show()
            binding.labelFollowers.show()

            it?.let { user ->
                Glide.with(binding.root)
                    .load(user.images?.first()?.url)
                    .circleCrop()
                    .into(binding.image)

                binding.name.text = user.display_name
                binding.numberFollowers.text = user.followers?.total.toString()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentProfileBinding = null
    }
}