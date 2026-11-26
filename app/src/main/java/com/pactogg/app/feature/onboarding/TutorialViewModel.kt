package com.pactogg.app.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pactogg.app.core.preferences.TutorialStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** PL-15: abre el tutorial la primera vez que alguien entra en este teléfono y cuando se pide desde Perfil. */
@HiltViewModel
class TutorialViewModel @Inject constructor(private val store: TutorialStore) : ViewModel() {
    private val mutable = MutableStateFlow(false)
    val visible = mutable.asStateFlow()
    private val pagerState = MutableStateFlow(TutorialPager())
    val pager = pagerState.asStateFlow()

    init {
        viewModelScope.launch { if (!store.seen.first()) mutable.value = true }
    }

    fun open() {
        pagerState.value = TutorialPager()
        mutable.value = true
    }

    fun next() = pagerState.update { it.next() }
    fun back() = pagerState.update { it.back() }

    /** "¡Listo!" o "Saltar": no se vuelve a abrir solo. */
    fun finish() {
        mutable.value = false
        viewModelScope.launch { store.markSeen() }
    }
}
