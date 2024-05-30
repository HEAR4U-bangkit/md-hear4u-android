package com.bangkit.hear4u.ui.landingPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.hear4u.databinding.FragmentWelcomeBinding
import com.bangkit.hear4u.ui.register.RegisterActivity

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGetStarted.setOnClickListener {
            val registIntent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(registIntent)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}