package com.bangkit.hear4u.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkit.hear4u.R
import com.bangkit.hear4u.databinding.FragmentProfileBinding
import com.bangkit.hear4u.ui.ViewModelFactory
import com.bangkit.hear4u.ui.landingPage.LandingActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.logout.setOnClickListener {
            viewModel.logout()
            val intent  = Intent(requireContext(), LandingActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.editProfile.setOnClickListener {
            editProfileFragment()
        }

        binding.about.setOnClickListener {
            aboutFragment()
        }

        getSession()

    }

    private fun getSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = user.fullname
        }
    }

    private fun editProfileFragment() {
        val fragmentManager = requireActivity().supportFragmentManager
        val childFragment = EditProfileFragment()
        fragmentManager.beginTransaction()
            .replace(R.id.profile_container, childFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun aboutFragment() {
        val fragmentManager = requireActivity().supportFragmentManager
        val childFragment = AboutFragment()
        fragmentManager.beginTransaction()
            .replace(R.id.profile_container, childFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}