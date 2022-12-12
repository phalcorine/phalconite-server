package io.phalconite.util

import at.favre.lib.crypto.bcrypt.BCrypt

object BcryptService {
    private const val SALT_ROUNDS = 10

    fun hash(value: String): String {
        val hashedValue = BCrypt.withDefaults().hashToString(SALT_ROUNDS, value.toCharArray())
        return hashedValue.toString()
    }

    fun verify(attemptedValue: String, hashedValue: String): Boolean {
        val result = BCrypt.verifyer().verify(attemptedValue.toCharArray(), hashedValue)
        return result.verified
    }
}