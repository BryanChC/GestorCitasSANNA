package com.charahua.gestorcitassanna

import com.charahua.gestorcitassanna.domain.validation.ValidationUtils
import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun testDniValidation() {
        assertTrue(ValidationUtils.isValidDni("12345678"))
        assertFalse(ValidationUtils.isValidDni("1234567"))
        assertFalse(ValidationUtils.isValidDni("123456789"))
        assertFalse(ValidationUtils.isValidDni("ABCDEFGH"))
    }

    @Test
    fun testTestPhoneValidation() {
        assertTrue(ValidationUtils.isValidPhone("987654321"))
        assertFalse(ValidationUtils.isValidPhone("887654321"))
        assertFalse(ValidationUtils.isValidPhone("98765432"))
        assertFalse(ValidationUtils.isValidPhone("9876543210"))
    }

    @Test
    fun testPasswordValidation() {
        assertTrue(ValidationUtils.isValidPassword("Sheldon25*"))
        assertTrue(ValidationUtils.isValidPassword("Contraseña1*"))
        assertFalse(ValidationUtils.isValidPassword("short"))
    }

    @Test
    fun testPasswordHashingAndVerification() {
        val password = "Sheldon25*"
        val salt = ValidationUtils.generateSalt()
        val hash = ValidationUtils.hashPassword(password, salt)

        assertNotNull(salt)
        assertNotNull(hash)
        assertTrue(ValidationUtils.verifyPassword(password, salt, hash))
        assertFalse(ValidationUtils.verifyPassword("WrongPassword1*", salt, hash))
    }
}
