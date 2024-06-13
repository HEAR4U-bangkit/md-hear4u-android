package com.bangkit.hear4u.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bangkit.hear4u.data.local.preferences.UserModel
import com.bangkit.hear4u.data.local.preferences.UserPreferences
import com.bangkit.hear4u.data.local.preferences.dataStore
import com.bangkit.hear4u.databinding.FragmentEditProfileBinding
import com.bangkit.hear4u.di.StateResult
import com.bangkit.hear4u.ui.ViewModelFactory
import kotlinx.coroutines.launch


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = user.fullname
            binding.edEmail.setText(user.email)
            binding.edFullname.setText(user.fullname)
            binding.btnSave.setOnClickListener {
                saveProfile(user.token, user.id)
            }
            binding.btnChangePassword.setOnClickListener {
                val oldPassword = binding.edOldPassword.text.toString()
                val newPassword = binding.edNewPassword.text.toString()
                val confirmPassword = binding.edConfirmNewPassword.text.toString()
                if(newPassword != confirmPassword){
                    showToast("Password baru dan konfirmasi password tidak sama")
                } else if(oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()){
                    showToast("Silahkan isi terlebih dahulu")
                } else {
//                    Log.d("EditProfileFragment", "onViewCreated: ${user.id}")
                    changePassword(user.id, oldPassword, newPassword, confirmPassword)
                }
            }
        }
    }


    private fun saveProfile(token: String, id: String) {

        val edEmail = binding.edEmail.text.toString()
        val edFullname = binding.edFullname.text.toString()
        if (binding.edEmail.text.toString().isNotEmpty() || binding.edFullname.text.toString()
                .isNotEmpty()
        ) {
            lifecycleScope.launch {
                viewModel.editProfile(id, edEmail, edFullname)
                    .observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is StateResult.Loading -> {
                                showToast("Loading")
                            }

                            is StateResult.Success -> {
                                lifecycleScope.launch {
                                    showToast(result.data.message.toString())
                                    viewModel.saveSession(
                                        UserModel(
                                            token,
                                            result.data.data?.id.toString(),
                                            edEmail,
                                            edFullname,
                                            true,
                                        )
                                    )
//                                    Log.d("EditProfileFragment", "saveProfile: ${result.data.data}")
                                }
                            }

                            is StateResult.Error -> {
                                showToast(result.error)
                            }
                        }
                    }
            }
        } else {
            Toast.makeText(requireContext(), "Silahkan isi terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun changePassword(
        id: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String,
    ) {
        lifecycleScope.launch {
            viewModel.changePassword(id, oldPassword, newPassword, confirmPassword)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is StateResult.Loading -> {
                            showToast("Loading")
                        }

                        is StateResult.Success -> {
                            lifecycleScope.launch {
//                                Log.d("dataPassword", "changePassword: ${result.data.data}")
                                showToast(result.data.message.toString())
                                binding.edNewPassword.setText("")
                                binding.edOldPassword.setText("")
                                binding.edConfirmNewPassword.setText("")
                            }
                        }

                        is StateResult.Error -> {
                            lifecycleScope.launch {
                                showToast(result.error)
                            }
                        }
                    }
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}