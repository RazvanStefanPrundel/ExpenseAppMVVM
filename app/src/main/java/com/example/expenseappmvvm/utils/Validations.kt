package com.example.expenseappmvvm.utils

import android.util.Patterns
import java.util.regex.Pattern

object Validations {
    fun isEmailInvalid(email: String): Boolean {
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true
        }
        return false
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
                return false
            }
        }
        return true
    }

    fun passwordContainsLowercase(password: String): Boolean {
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (item.isLowerCase()) {
                return false
            }
        }
        return true
    }

    fun passwordContainsUppercase(password: String): Boolean {
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (item.isUpperCase()) {
                return false
            }
        }
        return true
    }

    fun passwordContainsSpecialChar(password: String): Boolean {
        val specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}"
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (specialCharactersString.contains(item)) {
                return false
            }
        }
        return true
    }

    fun passwordExcludesSpace(password: String): Boolean {
        val c: CharArray = password.toCharArray()
        for (item in c) {
            if (!item.isWhitespace()) {
                return false
            }
        }
        return true
    }

    fun passwordLength(password: String): Boolean {
        if(password.isEmpty() || password.length <= 6){
            return true
        }
        return false
    }

    fun isNameInvalid(name: String): Boolean {
        if (name.isEmpty() || name.length <= 2 || Pattern.matches("/^[a-zA-Z\\s]*$/", name)){
            return true
        }
        return false
    }

    fun isAmountInvalid(amount: String): Boolean {
        val amountPattern = "^(0*[1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)\$"
        val c: CharArray = amount.toCharArray()
        for (item in c) {
            if (!Pattern.matches(amountPattern, amount)) {
                return true
            }
        }
        return false
    }

    fun isAmountEmpty(amount: String): Boolean {
        if (amount.isEmpty()) {
            return true
        }
        return false
    }
}