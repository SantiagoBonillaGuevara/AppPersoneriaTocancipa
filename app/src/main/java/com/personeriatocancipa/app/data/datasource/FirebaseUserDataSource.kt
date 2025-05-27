package com.personeriatocancipa.app.data.datasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.personeriatocancipa.app.domain.model.Admin
import com.personeriatocancipa.app.domain.model.Horario
import com.personeriatocancipa.app.domain.model.HorarioDia
import com.personeriatocancipa.app.domain.model.Lawyer
import com.personeriatocancipa.app.domain.model.RegistrableUser
import com.personeriatocancipa.app.domain.model.Role
import com.personeriatocancipa.app.domain.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseUserDataSource {

    private val db = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun getUserRole(userId: String): Result<Role> = suspendCoroutine { cont ->
        val paths = UserCollectionProvider().getCollections()

        fun checkNextPath(paths: List<Pair<String, Role>>) {
            if (paths.isEmpty()) {
                cont.resume(Result.failure(Exception("Rol no encontrado")))
                return
            }

            val (path, role) = paths.first()
            db.getReference(path).child(userId).get()
                .addOnSuccessListener {
                    val estado = it.child("estado").value?.toString()
                    if (it.exists() && estado == "Activo") {
                        cont.resume(Result.success(role))
                    } else {
                        Log.i("FirebaseUserDataSource", "Cuenta inactiva o no existente en $path")
                        checkNextPath(paths.drop(1))
                    }
                }
                .addOnFailureListener {
                    cont.resume(Result.failure(it))
                }
        }

        checkNextPath(paths)
    }

    suspend fun isParamRegistered(param: String, value: String): Boolean = suspendCoroutine { cont ->
        val paths: List<String> = UserCollectionProvider().getCollectionsName()

        fun check(pathsLeft: List<String>) {
            if (pathsLeft.isEmpty()) {
                cont.resume(false)
                return
            }

            val ref = db.getReference(pathsLeft.first())
            ref.orderByChild(param).equalTo(value).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        cont.resume(true)
                    } else {
                        check(pathsLeft.drop(1))
                    }
                }
                .addOnFailureListener {
                    cont.resume(false)
                }
        }

        check(paths)
    }

    suspend fun registerWithEmailAndPassword(email: String, password: String): Result<String> = suspendCoroutine { cont ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { cont.resume(Result.success(it.user?.uid ?: "")) }
            .addOnFailureListener { cont.resume(Result.failure(it)) }
    }

    suspend fun addUserToNode(node: String, user: RegistrableUser): Result<Unit> = suspendCoroutine { cont ->
        val uid = user.uid ?: return@suspendCoroutine cont.resume(Result.failure(Exception("UID es null")))
        db.getReference().child(node).child(uid).setValue(user)
            .addOnSuccessListener { cont.resume(Result.success(Unit)) }
            .addOnFailureListener { cont.resume(Result.failure(it)) }
    }

    suspend fun getUserById(node: String, uid: String): Result<RegistrableUser> = suspendCoroutine { cont ->
        val id = if (uid.isEmpty()) auth.currentUser?.uid!! else uid
        Log.i("FirebaseUserDataSource", "recibi uid = $uid entonces voy a buscar por el id ID: $id")
        db.getReference(node).child(id).get()
            .addOnSuccessListener { snapshot ->
                val user = when (node) {
                    "userData"      -> snapshot.getValue(User::class.java)
                    "AdminData",
                    "AdminPqrsData" -> snapshot.getValue(Admin::class.java)
                    "abogadoData"   -> snapshot.getValue(Lawyer::class.java)
                    else            -> null
                }
                if (user is RegistrableUser) cont.resume(Result.success(user))
                else cont.resume(Result.failure(Exception("No se pudo mapear el usuario desde $node")))
            }
            .addOnFailureListener {
                cont.resume(Result.failure(it))
            }
    }

    suspend fun getUsers(node: String): Result<List<RegistrableUser>> = suspendCoroutine { cont ->
        db.getReference(node).get()
            .addOnSuccessListener { snapshot ->
                // para cada hijo, primero verifico que snapshot.value sea un Map
                val users = snapshot.children.mapNotNull {
                    // ahora sí lo convierto, sabiendo que hay estructura de objeto
                    when (node) {
                        "userData" -> it.getValue(User::class.java)
                        "AdminData", "AdminPqrsData" -> it.getValue(Admin::class.java)
                        "abogadoData" -> {
                            val lawyer = it.getValue(Lawyer::class.java)
                            val horarioSnapshot = it.child("horario")
                            val horario = Horario(
                                Lunes = horarioSnapshot.child("lunes").getValue(HorarioDia::class.java),
                                Martes = horarioSnapshot.child("martes").getValue(HorarioDia::class.java),
                                Miércoles = horarioSnapshot.child("miércoles").getValue(HorarioDia::class.java),
                                Jueves = horarioSnapshot.child("jueves").getValue(HorarioDia::class.java),
                                Viernes = horarioSnapshot.child("viernes").getValue(HorarioDia::class.java)
                            )
                            lawyer?.copy(horario = horario)
                        }
                        else -> null
                    }
                }
                cont.resume(Result.success(users))
            }
            .addOnFailureListener { cont.resume(Result.failure(it)) }
    }
}
