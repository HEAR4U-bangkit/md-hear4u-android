package com.bangkit.hear4u.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.hear4u.R
import com.bangkit.hear4u.databinding.FragmentEditProfileBinding


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding.toolbar.setNavigationOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}