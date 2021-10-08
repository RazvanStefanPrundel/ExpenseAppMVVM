package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.data.database.repositories.UserRepository
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    single { UserRepository(get()) }
}