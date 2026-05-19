package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import ec.edu.uisek.githubclient.ui.components.RepoItem
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

@Composable
fun RepoList() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(all=16.dp)
    ) {

        RepoItem(
            name = "Proyecto de Djangp",
            description = "Un proyecto relizado en Django 5.2 para la materia de deasarrollo web",
            avatarImg = "https://avatars.githubusercontent.com/u/1?v=4",
            language = "Python"
        )
        RepoItem(
            name = "Proyecto de React",
            description = "Un proyecto relizado en React para la materia de deasarrollo web",
            avatarImg = "https://avatars.githubusercontent.com/u/238549896?v=4",
            language = "Typescript"
        )
        RepoItem(
            name = "Proyecto de Android",
            description = "Un proyecto relizado en Andriod para la materia de deasarrollo movil",
            avatarImg = "https://avatars.githubusercontent.com/u/1?v=4",
            language = "Kotlin"
        )
        RepoItem(
            name = "Proyecto de iOS",
            description = "Un proyecto relizado en iOS para la materia de deasarrollo movil",
            avatarImg = "https://avatars.githubusercontent.com/u/238549896?v=44",
            language = "Swift"
        )
    }

}