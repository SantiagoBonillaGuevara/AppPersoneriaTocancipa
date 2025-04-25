package com.personeriatocancipa.app.data.datasource

import com.personeriatocancipa.app.domain.model.Role

class UserCollectionProvider {

    constructor() {
    }

    fun getCollections(): List<Pair<String, Role>> {
        return listOf(
            "abogadoData" to Role.LAWYER,
            "AdminData" to Role.ADMIN,
            "userData" to Role.USER
        )
    }

    fun getCollectionsName():List<String>{
        return getCollections().map { it.first }
    }
}