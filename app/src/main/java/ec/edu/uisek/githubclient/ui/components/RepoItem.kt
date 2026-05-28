package ec.edu.uisek.githubclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ec.edu.uisek.githubclient.models.GitHubUser
import ec.edu.uisek.githubclient.models.Repository

@Composable
fun RepoItem (
    repository: Repository,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ){
        Row (
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = repository.owner.avatarUrl,
                contentDescription = "Imagen de ${repository.name}",
                modifier = Modifier.size(size=60.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(width = 16.dp))
            Column {
                Text(
                    text = repository.name,
                    style= MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(width = 4.dp))
                repository.description?.let {
                    Text(
                        text = it,
                        style= MaterialTheme.typography.bodyMedium,
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(height = 4.dp))
                repository.language?.let{
                    Text(
                        text = it,
                        style= MaterialTheme.typography.labelSmall
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoItemPreview(){
    val repository = Repository (
        id = "1",
        name = "Repo Name",
        owner = GitHubUser(
            id = "1",
            login = "User Name",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4"
        ),
        description = "Repo Description",
        language = "Kotlin"
    )
    RepoItem (repository )
}
