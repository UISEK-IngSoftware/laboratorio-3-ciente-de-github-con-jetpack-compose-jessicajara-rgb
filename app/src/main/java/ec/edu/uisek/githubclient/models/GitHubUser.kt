package ec.edu.uisek.githubclient.models
import com.google.gson.annotations.SerializedName

data class GitHubUser(
    val id: String,
    val name: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)
