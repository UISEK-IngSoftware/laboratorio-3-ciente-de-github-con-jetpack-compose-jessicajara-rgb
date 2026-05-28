package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.ui.screens.RepoForm
import ec.edu.uisek.githubclient.ui.screens.RepoList
import ec.edu.uisek.githubclient.viewmodels.RepoListViewModel
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubClientTheme {

                val listViewModel: RepoListViewModel = viewModel()
                var currentScreen by remember { mutableStateOf("repoList") }
                var selectedRepo by remember { mutableStateOf<Repository?>(null) }

                when (currentScreen) {
                    "repoList" -> RepoList(
                        viewModel = listViewModel,
                        onNavigateToForm = { repo ->
                            selectedRepo = repo
                            currentScreen = "repoForm"
                        }
                    )
                    "repoForm" -> RepoForm(
                        repository = selectedRepo,
                        onBackClick = { 
                            selectedRepo = null
                            currentScreen = "repoList" 
                        },
                        onSaveSuccess = {
                            listViewModel.fetchRepos()
                            selectedRepo = null
                            currentScreen = "repoList"
                        }
                    )
                }
            }
        }
    }
}
