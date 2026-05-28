package ec.edu.uisek.githubclient.services
import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.models.RepositoryPayload
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("user/repos")
    suspend fun getRepositories(
        @Query("sort") sort: String = "created",
        @Query("direction") direction: String = "desc",
        @Query("per_page") perPage: Int = 100,
        @Query("affiliation") affiliation: String = "owner",
        @Query("t") t: String = "${System.currentTimeMillis()}"
    ): List<Repository>

    @POST(value = "user/repos")
    suspend fun createRepository(
        @Body repository: RepositoryPayload
    ): Repository
}