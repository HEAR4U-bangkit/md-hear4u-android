package com.bangkit.hear4u.ui.landingPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.hear4u.R
import com.bangkit.hear4u.databinding.FragmentLandingBinding
import com.bangkit.hear4u.databinding.FragmentWelcomeBinding


class LandingFragment : Fragment() {
    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnContinue.setOnClickListener {
            binding.container.setVisibility(View.VISIBLE)
            val landingFragment = WelcomeFragment()
            childFragmentManager.beginTransaction()
                .replace(binding.container.id, landingFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}