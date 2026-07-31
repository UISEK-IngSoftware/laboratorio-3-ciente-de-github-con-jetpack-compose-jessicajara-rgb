package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.ui.components.RepoItem
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme
import ec.edu.uisek.githubclient.viewmodels.RepoListViewModel
import kotlin.math.roundToInt

enum class DragValue { Start, Center }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RepoList(
     modifier: Modifier = Modifier,
     viewModel: RepoListViewModel = viewModel(),
     onNavigateToForm: (Repository?) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val repos by viewModel.repos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRepos()
    }

    var repoToDelete by remember { mutableStateOf<Repository?>(null) }
    val density = LocalDensity.current

    Scaffold (
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End){
                FloatingActionButton(
                    onClick = { onNavigateToForm(null) },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = onLogout,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                }
            }
        }
    ) { innerPadding ->
        Box (modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)){
            if (isLoading && repos.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            
            errorMsg?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            LazyColumn (modifier = Modifier.fillMaxSize()) {
                items(repos, key = { it.id }) { repo ->
                    val maxActionSize = 140.dp
                    val maxActionSizePx = with(density) { maxActionSize.toPx() }
                    
                    val state = remember {
                        AnchoredDraggableState(
                            initialValue = DragValue.Center,
                            anchors = DraggableAnchors {
                                DragValue.Center at 0f
                                DragValue.Start at -maxActionSizePx
                            },
                            positionalThreshold = { distance: Float -> distance * 0.5f },
                            velocityThreshold = { with(density) { 100.dp.toPx() } },
                            snapAnimationSpec = tween(),
                            decayAnimationSpec = exponentialDecay()
                        )
                    }

                    LaunchedEffect(repoToDelete) {
                        if (repoToDelete == null && state.currentValue == DragValue.Start) {
                            state.animateTo(DragValue.Center)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .background(Color.Red.copy(alpha = 0.8f))
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .width(maxActionSize)
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { onNavigateToForm(repo) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.White)
                            }
                            IconButton(onClick = { repoToDelete = repo }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .offset {
                                    val offset =
                                        state.requireOffset().coerceIn(-maxActionSizePx, 0f)
                                    IntOffset(x = offset.roundToInt(), y = 0)
                                }
                                .anchoredDraggable(state, Orientation.Horizontal)
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            RepoItem(repository = repo)
                        }
                    }
                }
            }
        }

        repoToDelete?.let { repo ->
            AlertDialog(
                onDismissRequest = { repoToDelete = null },
                title = { Text("Eliminar Repositorio") },
                text = { Text("¿Estás seguro de eliminar '${repo.name}'?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteRepo(repo) // Elimina el repositorio
                            repoToDelete = null
                        }
                    ) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { repoToDelete = null }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoListPreview(){
    GithubClientTheme { 
        RepoList()
    }
}
