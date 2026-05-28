package ec.edu.uisek.githubclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.services.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepoListViewModel : ViewModel (){
    private val apiService = RetrofitClient.apiService
    
    private val _repos = MutableStateFlow<List<Repository>>(emptyList())
    val repos: StateFlow<List<Repository>> = _repos

    private val _isLoading = MutableStateFlow(value = false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(value = null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    init {
        fetchRepos()
    }

    fun fetchRepos(){
        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null
            try {
                // Forzamos actualización con un timestamp para evitar cachés
                val result = apiService.getRepositories(t = System.currentTimeMillis().toString())
                _repos.value = result
            } catch (e: Exception){
                _errorMsg.value = "Error al cargar: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteRepo(repo: Repository) {
        viewModelScope.launch {
            _repos.update { list -> 
                list.filterNot { it.id == repo.id } 
            }
            
            try {
                apiService.deleteRepository(repo.owner.login, repo.name)
            } catch (e: Exception) {
                _errorMsg.value = "Error al eliminar en GitHub: ${e.localizedMessage}"
                fetchRepos() 
            }
        }
    }
}
