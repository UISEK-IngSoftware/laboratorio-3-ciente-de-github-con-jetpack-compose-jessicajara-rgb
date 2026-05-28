package ec.edu.uisek.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.RepositoryPayload
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepoFormViewModel: ViewModel() {
    private val apiService = RetrofitClient.apiService
    
    private val _isLoading = MutableStateFlow(value = false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    private val _isSuccess = MutableStateFlow(value = false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    fun createRepo(name: String, description: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val repository = RepositoryPayload(name = name, description = description)
                apiService.createRepository(repository)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMsg.value = "Error al crear repositorio: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateRepo(owner: String, oldName: String, newName: String, description: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                val payload = RepositoryPayload(name = newName, description = description)
                apiService.updateRepository(owner, oldName, payload)
                _isSuccess.value = true
            } catch (e: Exception) {
                _errorMsg.value = "Error al editar repositorio: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSuccess() {
        _isSuccess.value = false
    }
}