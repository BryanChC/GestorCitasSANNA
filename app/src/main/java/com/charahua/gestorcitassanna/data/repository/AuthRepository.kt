package com.charahua.gestorcitassanna.data.repository

import com.charahua.gestorcitassanna.data.local.dao.PacienteDao
import com.charahua.gestorcitassanna.data.local.dao.UsuarioDao
import com.charahua.gestorcitassanna.data.local.database.SannaDatabase
import com.charahua.gestorcitassanna.data.local.entity.PacienteEntity
import com.charahua.gestorcitassanna.data.local.entity.UsuarioEntity
import com.charahua.gestorcitassanna.domain.validation.ValidationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val database: SannaDatabase
) {
    private val usuarioDao: UsuarioDao = database.usuarioDao()
    private val pacienteDao: PacienteDao = database.pacienteDao()

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        SannaDatabase.ensureSeeded(database)
    }

    suspend fun login(email: String, password: String): UsuarioEntity? = withContext(Dispatchers.IO) {
        ensureSeeded()
        val cleanEmail = email.trim()
        val user = usuarioDao.getByEmail(cleanEmail) ?: return@withContext null
        val cleanPass = password.trim()
        var isValid = ValidationUtils.verifyPassword(cleanPass, user.salt, user.passwordHash)
        if (!isValid) {
            val altPass = if (cleanPass.contains("ñ", ignoreCase = true)) {
                cleanPass.replace("ñ", "n").replace("Ñ", "N")
            } else {
                cleanPass.replace("n", "ñ").replace("N", "Ñ")
            }
            isValid = ValidationUtils.verifyPassword(altPass, user.salt, user.passwordHash)
        }
        if (isValid) user else null
    }

    suspend fun registerPatient(
        nombre: String,
        apellido: String,
        email: String,
        dni: String,
        telefono: String,
        password: String,
        fechaNacimiento: String,
        genero: String,
        direccion: String
    ): Result<Pair<UsuarioEntity, Long>> = withContext(Dispatchers.IO) {
        ensureSeeded()
        val cleanEmail = email.trim()
        if (usuarioDao.getByEmail(cleanEmail) != null) {
            return@withContext Result.failure(Exception("El correo electrónico ya está registrado."))
        }
        val salt = ValidationUtils.generateSalt()
        val passwordHash = ValidationUtils.hashPassword(password.trim(), salt)

        val usuario = UsuarioEntity(
            email = cleanEmail,
            passwordHash = passwordHash,
            salt = salt,
            rol = "PACIENTE",
            nombre = nombre.trim(),
            apellido = apellido.trim(),
            dni = dni.trim(),
            telefono = telefono.trim()
        )
        val userId = usuarioDao.insert(usuario)

        val pacienteId = pacienteDao.insert(
            PacienteEntity(
                usuarioId = userId,
                fechaNacimiento = fechaNacimiento.trim(),
                genero = genero.trim(),
                direccion = direccion.trim()
            )
        )

        Result.success(Pair(usuario.copy(id = userId), pacienteId))
    }

    suspend fun updatePatientProfile(
        userId: Long,
        telefono: String,
        email: String,
        direccion: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim()
        val cleanPhone = telefono.trim()
        val cleanDir = direccion.trim()

        if (!ValidationUtils.isValidEmail(cleanEmail)) {
            return@withContext Result.failure(Exception("El correo electrónico ingresado no es válido."))
        }
        if (!ValidationUtils.isValidPhone(cleanPhone)) {
            return@withContext Result.failure(Exception("El número celular debe tener 9 dígitos e iniciar con 9."))
        }
        if (cleanDir.isBlank()) {
            return@withContext Result.failure(Exception("La dirección de domicilio no puede estar vacía."))
        }

        usuarioDao.updateContactInfo(userId, cleanPhone, cleanEmail)
        pacienteDao.updateDireccion(userId, cleanDir)
        Result.success(Unit)
    }

    suspend fun getUserById(id: Long): UsuarioEntity? = withContext(Dispatchers.IO) {
        usuarioDao.getById(id)
    }

    suspend fun getPacienteByUsuarioId(usuarioId: Long): PacienteEntity? = withContext(Dispatchers.IO) {
        pacienteDao.getByUsuarioId(usuarioId)
    }
}
