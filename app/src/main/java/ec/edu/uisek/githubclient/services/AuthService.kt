package ec.edu.uisek.githubclient.services

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AuthService (context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("github_prefs", Context.MODE_PRIVATE)

    fun saveAuth (username: String, token: String){
        sharedPreferences.edit {
            putString("username", username).putString("token", token)
        }
    }

    fun getUserName(): String? = sharedPreferences.getString("username", null)
    fun getToken(): String? = sharedPreferences.getString("token", null)

    fun logout(){
        sharedPreferences.edit{ clear() }
    }

    fun isLoggedIn(): Boolean = getUserName() != null && getToken() != null
}