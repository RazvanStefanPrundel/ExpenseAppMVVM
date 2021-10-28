package com.example.expenseappmvvm.utils

import android.util.Patterns
import androidx.databinding.BindingAdapter
import java.util.regex.Pattern

object Validations {
    fun emailValidation(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun passwordEmpty(password: String): Boolean {
        if (password == "" || password.isEmpty()) {
            return true
        }
        return false
    }

    fun passwordContainsDigits(password: String): Boolean {
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (item.isDigit()) {
                return true
            }
        }
        return false
    }

    fun passwordContainsLowercase(password: String): Boolean {
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (item.isLowerCase()) {
                return true
            }
        }
        return false
    }

    fun passwordContainsUppercase(password: String): Boolean {
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (item.isUpperCase()) {
                return true
            }
        }
        return false
    }

    fun passwordContainsSpecialChar(password: String): Boolean {
        val specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}"
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (specialCharactersString.contains(item)) {
                return true
            }
        }
        return false
    }

    fun passwordExcludesSpace(password: String): Boolean {
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (item.isWhitespace()) {
                return false
            }
        }
        return true
    }

    fun passwordLength(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }

    fun nameValidation(name: String): Boolean {
        return name.isNotEmpty() && name.split(" ").size >= 2 && !Pattern.matches(
            "/^[a-zA-Z\\s]*$/",
            name
        )
    }
}