package ec.edu.uisek.githubclient.models
import com.google.gson.annotations.SerializedName

data class GitHubUser(
    val id: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)
