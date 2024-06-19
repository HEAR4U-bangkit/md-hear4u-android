package com.bangkit.hear4u.utils

fun formatLabel(label: String): String {
    return label.split("_").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}
