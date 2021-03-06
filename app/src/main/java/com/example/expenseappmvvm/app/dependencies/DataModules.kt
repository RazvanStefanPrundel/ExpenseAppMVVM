package com.example.expenseappmvvm.app.dependencies

import com.example.expenseappmvvm.data.database.RoomDB
import com.example.expenseappmvvm.data.database.repositories.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val databaseModule: Module = module {
    single { RoomDB(androidContext()).appDatabase }
}

val repositoryModule: Module = module {
    single { UserRepository(get()) }
}

//Add preferenceModule for shared pref