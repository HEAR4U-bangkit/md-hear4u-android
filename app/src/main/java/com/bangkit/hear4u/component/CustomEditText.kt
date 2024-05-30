package com.bangkit.hear4u.component

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.bangkit.hear4u.R

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    init{
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString()
                val currentInputType = inputType

                when (currentInputType){
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_CLASS_TEXT ->{
                        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
                        if (!isValid){
                            setError("Email invalid", null)
                        }
                    }
                    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT ->{
                        if (input.length < 7){
                            setError("Password must be at least 8 characters", null)
                        } else if(input.contains(" ")){
                            setError("Password cannot contain spaces", null)
                        }
                    }
                    else -> {
                        error = null
                    }
                }
                if (error != null){
                    setBackgroundResource(R.drawable.edit_text_background_error)
                } else {
                    setBackgroundResource(R.drawable.edit_text_background)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }
}