package com.pactogg.app.core.navigation

import androidx.lifecycle.ViewModel
import com.pactogg.app.core.environment.EnvironmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ShellViewModel @Inject constructor(repository: EnvironmentRepository) : ViewModel() {
    private val mutableEnvironment = MutableStateFlow(repository.current())
    val environment = mutableEnvironment.asStateFlow()
}
