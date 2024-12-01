package com.example.pokemonexplorerapp.utils

import com.example.pokemonexplorerapp.base.composables.DialogData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent

class AppDialogManager {
    private val dialogData = MutableStateFlow<DialogData?>(null)
    private val dialogVisibility = MutableStateFlow(false)

    fun show(data: DialogData) {
        CoroutineScope(Dispatchers.Main).launch {
            runBlocking {
                dialogData.emit(data)
            }
            dialogVisibility.emit(true)
        }
    }

    fun hide() {
        CoroutineScope(Dispatchers.Main).launch {
            dialogVisibility.emit(false)
        }
    }
}

fun showDialog(data: DialogData) {
    KoinJavaComponent.get<AppDialogManager>(AppDialogManager::class.java).show(data)
}

fun hideDialog() {
    KoinJavaComponent.get<AppDialogManager>(AppDialogManager::class.java).hide()
}