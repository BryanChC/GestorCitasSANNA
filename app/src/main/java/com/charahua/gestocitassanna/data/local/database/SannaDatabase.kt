package com.charahua.gestocitassanna.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.charahua.gestocitassanna.data.local.dao.*
import com.charahua.gestocitassanna.data.local.entity.*
import com.charahua.gestocitassanna.domain.validation.ValidationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(
    entities = [
        UsuarioEntity::class,
        PacienteEntity::class,
        SedeEntity::class,
        EspecialidadEntity::class,
        SedeEspecialidadEntity::class,
        MedicoEntity::class,
        MedicoSedeEntity::class,
        MedicoEspecialidadEntity::class,
        HorarioMedicoEntity::class,
        CitaEntity::class,
        AseguradoraEntity::class,
        ConvenioCoberturaEntity::class,
        TarifaEntity::class,
        HistorialReprogramacionEntity::class,
        InsumoEntity::class,
        MaterialEntity::class,
        TiempoProductivoEntity::class,
        CostoSalarialDirectoEntity::class,
        GastoOperativoEntity::class,
        SueldoAdministrativoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SannaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun pacienteDao(): PacienteDao
    abstract fun sedeDao(): SedeDao
    abstract fun especialidadDao(): EspecialidadDao
    abstract fun sedeEspecialidadDao(): SedeEspecialidadDao
    abstract fun medicoDao(): MedicoDao
    abstract fun medicoSedeDao(): MedicoSedeDao
    abstract fun medicoEspecialidadDao(): MedicoEspecialidadDao
    abstract fun horarioMedicoDao(): HorarioMedicoDao
    abstract fun citaDao(): CitaDao
    abstract fun historialReprogramacionDao(): HistorialReprogramacionDao
    abstract fun aseguradoraDao(): AseguradoraDao
    abstract fun convenioCoberturaDao(): ConvenioCoberturaDao
    abstract fun tarifaDao(): TarifaDao
    abstract fun insumoDao(): InsumoDao
    abstract fun materialDao(): MaterialDao
    abstract fun tiempoProductivoDao(): TiempoProductivoDao
    abstract fun costoSalarialDirectoDao(): CostoSalarialDirectoDao
    abstract fun gastoOperativoDao(): GastoOperativoDao
    abstract fun sueldoAdministrativoDao(): SueldoAdministrativoDao

    companion object {
        @Volatile
        private var INSTANCE: SannaDatabase? = null

        fun getDatabase(context: Context): SannaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SannaDatabase::class.java,
                    "sanna_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    seedDatabase(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                CoroutineScope(Dispatchers.IO).launch {
                    ensureSeeded(instance)
                }
                instance
            }
        }

        suspend fun ensureSeeded(database: SannaDatabase) = withContext(Dispatchers.IO) {
            seedDatabase(database)
        }

        suspend fun seedDatabase(database: SannaDatabase) = withContext(Dispatchers.IO) {
            if (database.usuarioDao().getByEmail("ryax@gmail.com") == null) {
                val adminSalt = ValidationUtils.generateSalt()
                val adminHash = ValidationUtils.hashPassword("Sheldon25*", adminSalt)
                database.usuarioDao().insert(
                    UsuarioEntity(
                        email = "ryax@gmail.com",
                        passwordHash = adminHash,
                        salt = adminSalt,
                        rol = "ADMIN",
                        nombre = "Ryax",
                        apellido = "Admin",
                        dni = "12345678",
                        telefono = "987654321"
                    )
                )
            }

            if (database.usuarioDao().getByEmail("usuario@gmail.com") == null) {
                val patientSalt = ValidationUtils.generateSalt()
                val patientHash = ValidationUtils.hashPassword("Contrasena1*", patientSalt)
                val patientUserId = database.usuarioDao().insert(
                    UsuarioEntity(
                        email = "usuario@gmail.com",
                        passwordHash = patientHash,
                        salt = patientSalt,
                        rol = "PACIENTE",
                        nombre = "Juan",
                        apellido = "Pérez",
                        dni = "87654321",
                        telefono = "912345678"
                    )
                )

                if (database.pacienteDao().getByUsuarioId(patientUserId) == null) {
                    database.pacienteDao().insert(
                        PacienteEntity(
                            usuarioId = patientUserId,
                            fechaNacimiento = "1995-05-12",
                            genero = "Masculino",
                            direccion = "Av. Javier Prado Este 1066, San Isidro"
                        )
                    )
                }
            }

            if (database.sedeDao().getAll().isNotEmpty()) return@withContext

            val sedeSanBorjaId = database.sedeDao().insert(
                SedeEntity(nombre = "SANNA Sede San Borja", direccion = "Av. Guardia Civil 337", distrito = "San Borja", telefono = "01-635-5000")
            )
            val sedeElGolfId = database.sedeDao().insert(
                SedeEntity(nombre = "SANNA Sede El Golf", direccion = "Av. Toribio Tortry 380", distrito = "San Isidro", telefono = "01-635-6000")
            )
            val sedeLimaId = database.sedeDao().insert(
                SedeEntity(nombre = "SANNA Sede Lima Metropolitana", direccion = "Av. Arequipa 1234", distrito = "Lima", telefono = "01-635-7000")
            )

            val espCardioId = database.especialidadDao().insert(EspecialidadEntity(nombre = "Cardiología", descripcion = "Prevención, diagnóstico y tratamiento de enfermedades del corazón."))
            val espPediatriaId = database.especialidadDao().insert(EspecialidadEntity(nombre = "Pediatría", descripcion = "Cuidado integral de la salud de niños y adolescentes."))
            val espGeneralId = database.especialidadDao().insert(EspecialidadEntity(nombre = "Medicina General", descripcion = "Atención médica primaria y orientación clínica general."))
            val espOftalmoId = database.especialidadDao().insert(EspecialidadEntity(nombre = "Oftalmología", descripcion = "Diagnóstico y tratamiento de patologías oculares."))
            val espDermatologiaId = database.especialidadDao().insert(EspecialidadEntity(nombre = "Dermatología", descripcion = "Cuidado y salud de la piel, cabello y uñas."))

            val sedeSpecs = listOf(
                SedeEspecialidadEntity(sedeId = sedeSanBorjaId, especialidadId = espCardioId),
                SedeEspecialidadEntity(sedeId = sedeSanBorjaId, especialidadId = espPediatriaId),
                SedeEspecialidadEntity(sedeId = sedeSanBorjaId, especialidadId = espGeneralId),
                SedeEspecialidadEntity(sedeId = sedeElGolfId, especialidadId = espOftalmoId),
                SedeEspecialidadEntity(sedeId = sedeElGolfId, especialidadId = espDermatologiaId),
                SedeEspecialidadEntity(sedeId = sedeLimaId, especialidadId = espGeneralId),
                SedeEspecialidadEntity(sedeId = sedeLimaId, especialidadId = espCardioId)
            )
            for (ss in sedeSpecs) {
                database.sedeEspecialidadDao().insert(ss)
            }

            val med1Id = database.medicoDao().insert(MedicoEntity(nombre = "Carlos", apellido = "Mendoza", colegiatura = "CMP 45123", telefono = "998877665", email = "cmendoza@sanna.pe"))
            val med2Id = database.medicoDao().insert(MedicoEntity(nombre = "Ana", apellido = "Silva", colegiatura = "CMP 52341", telefono = "997766554", email = "asilva@sanna.pe"))
            val med3Id = database.medicoDao().insert(MedicoEntity(nombre = "Jorge", apellido = "Ramírez", colegiatura = "CMP 38912", telefono = "996655443", email = "jramirez@sanna.pe"))
            val med4Id = database.medicoDao().insert(MedicoEntity(nombre = "Lucía", apellido = "Fernández", colegiatura = "CMP 61234", telefono = "995544332", email = "lfernandez@sanna.pe"))
            val med5Id = database.medicoDao().insert(MedicoEntity(nombre = "Miguel", apellido = "Torres", colegiatura = "CMP 41982", telefono = "994433221", email = "mtorres@sanna.pe"))

            database.medicoSedeDao().insert(MedicoSedeEntity(medicoId = med1Id, sedeId = sedeSanBorjaId))
            database.medicoEspecialidadDao().insert(MedicoEspecialidadEntity(medicoId = med1Id, especialidadId = espCardioId))

            database.medicoSedeDao().insert(MedicoSedeEntity(medicoId = med2Id, sedeId = sedeSanBorjaId))
            database.medicoEspecialidadDao().insert(MedicoEspecialidadEntity(medicoId = med2Id, especialidadId = espPediatriaId))

            database.medicoSedeDao().insert(MedicoSedeEntity(medicoId = med3Id, sedeId = sedeSanBorjaId))
            database.medicoEspecialidadDao().insert(MedicoEspecialidadEntity(medicoId = med3Id, especialidadId = espGeneralId))

            database.medicoSedeDao().insert(MedicoSedeEntity(medicoId = med4Id, sedeId = sedeElGolfId))
            database.medicoEspecialidadDao().insert(MedicoEspecialidadEntity(medicoId = med4Id, especialidadId = espOftalmoId))

            database.medicoSedeDao().insert(MedicoSedeEntity(medicoId = med5Id, sedeId = sedeElGolfId))
            database.medicoEspecialidadDao().insert(MedicoEspecialidadEntity(medicoId = med5Id, especialidadId = espDermatologiaId))

            val dias = listOf("LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES")
            val medicos = listOf(med1Id, med2Id, med3Id, med4Id, med5Id)
            val sedes = listOf(sedeSanBorjaId, sedeSanBorjaId, sedeSanBorjaId, sedeElGolfId, sedeElGolfId)

            for (i in medicos.indices) {
                for (dia in dias) {
                    database.horarioMedicoDao().insert(
                        HorarioMedicoEntity(
                            medicoId = medicos[i],
                            sedeId = sedes[i],
                            diaSemana = dia,
                            horaInicio = "08:00",
                            horaFin = "14:00",
                            intervaloMinutos = 30
                        )
                    )
                }
            }

            val asegRimac = database.aseguradoraDao().insert(AseguradoraEntity(nombre = "Rímac Seguros", ruc = "20100041915", contacto = "01-411-1111"))
            val asegPacifico = database.aseguradoraDao().insert(AseguradoraEntity(nombre = "Pacífico Seguros", ruc = "2033297fa11", contacto = "01-513-5000"))
            val asegLaPositiva = database.aseguradoraDao().insert(AseguradoraEntity(nombre = "La Positiva Sanitas", ruc = "20100109123", contacto = "01-211-0200"))

            val especialidades = listOf(espCardioId, espPediatriaId, espGeneralId, espOftalmoId, espDermatologiaId)
            val costosBase = listOf(180.0, 150.0, 100.0, 160.0, 140.0)
            for (i in especialidades.indices) {
                database.tarifaDao().insert(TarifaEntity(especialidadId = especialidades[i], sedeId = sedeSanBorjaId, costoBase = costosBase[i]))
                database.tarifaDao().insert(TarifaEntity(especialidadId = especialidades[i], sedeId = sedeElGolfId, costoBase = costosBase[i] + 20.0))
                database.tarifaDao().insert(TarifaEntity(especialidadId = especialidades[i], sedeId = sedeLimaId, costoBase = costosBase[i] - 10.0))
            }

            database.convenioCoberturaDao().insert(ConvenioCoberturaEntity(aseguradoraId = asegRimac, especialidadId = espCardioId, porcentajeCobertura = 80))
            database.convenioCoberturaDao().insert(ConvenioCoberturaEntity(aseguradoraId = asegPacifico, especialidadId = espCardioId, porcentajeCobertura = 85))
            database.convenioCoberturaDao().insert(ConvenioCoberturaEntity(aseguradoraId = asegLaPositiva, especialidadId = espGeneralId, porcentajeCobertura = 90))

            database.insumoDao().insert(InsumoEntity(nombre = "Jeringas descartables 5ml", categoria = "Material Médico", costoUnitario = 0.50, stock = 1500))
            database.insumoDao().insert(InsumoEntity(nombre = "Guantes quirúrgicos estériles", categoria = "Protección", costoUnitario = 1.20, stock = 800))
            database.insumoDao().insert(InsumoEntity(nombre = "Alcohol en gel 1L", categoria = "Desinfección", costoUnitario = 18.50, stock = 120))

            database.materialDao().insert(MaterialEntity(nombre = "Ecógrafo portátil", tipo = "Equipamiento", costo = 25000.0, vidaUtilMeses = 60))
            database.materialDao().insert(MaterialEntity(nombre = "Camilla clínica hidráulica", tipo = "Mobiliario", costo = 3500.0, vidaUtilMeses = 120))

            database.tiempoProductivoDao().insert(TiempoProductivoEntity(descripcion = "Consulta Médica Especializada", horasMensuales = 160.0, costoHora = 75.0))
            database.tiempoProductivoDao().insert(TiempoProductivoEntity(descripcion = "Tópico y Procedimientos menores", horasMensuales = 120.0, costoHora = 50.0))

            database.costoSalarialDirectoDao().insert(CostoSalarialDirectoEntity(cargo = "Médico Especialista Senior", sueldoBase = 8500.0, beneficiosSociales = 2500.0, total = 11000.0))
            database.costoSalarialDirectoDao().insert(CostoSalarialDirectoEntity(cargo = "Enfermera Triage", sueldoBase = 3200.0, beneficiosSociales = 950.0, total = 4150.0))

            database.gastoOperativoDao().insert(GastoOperativoEntity(concepto = "Servicio de Electricidad e Internet Sede", tipoGasto = "Fijo", montoMensual = 14500.0))
            database.gastoOperativoDao().insert(GastoOperativoEntity(concepto = "Mantenimiento Preventivo de Equipos", tipoGasto = "Variable", montoMensual = 8200.0))

            database.sueldoAdministrativoDao().insert(SueldoAdministrativoEntity(puesto = "Gerente de Operaciones", departamento = "Administración", sueldo = 9500.0))
            database.sueldoAdministrativoDao().insert(SueldoAdministrativoEntity(puesto = "Asistente de Admisión y Citas", departamento = "Atención al Cliente", sueldo = 2400.0))
        }
    }
}
