package com.example.devhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.example.devhub.ui.theme.DevHubTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// --- Data Models ---
data class User(
    val login: String,
    val name: String?,
    val bio: String?,
    val avatar_url: String,
    val followers: Int,
    val following: Int
)

data class Repo(
    val name: String,
    val description: String?,
    val stargazers_count: Int
)

// --- Retrofit API Service ---
interface GitHubApiService {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): User

    @GET("users/{username}/repos")
    suspend fun getRepos(@Path("username") username: String): List<Repo>
}

// --- Retrofit Client ---
object RetrofitClient {
    val api: GitHubApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }
}

// --- ViewModel ---
class ProfileViewModel : ViewModel() {
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _repos = mutableStateOf<List<Repo>>(emptyList())
    val repos: State<List<Repo>> = _repos

    fun loadProfile(username: String) {
        viewModelScope.launch {
            try {
                _user.value = RetrofitClient.api.getUser(username)
                _repos.value = RetrofitClient.api.getRepos(username)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// --- Main Activity ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen()
                }
            }
        }
    }
}

// --- Composable Screen ---
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val user = viewModel.user.value
    val repos = viewModel.repos.value

    LaunchedEffect(Unit) {
        viewModel.loadProfile("octocat") // replace with your GitHub username
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            if (user != null) {
                AsyncImage(
                    model = user.avatar_url,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = user.name ?: "", style = MaterialTheme.typography.headlineSmall)
                Text(text = "@${user.login}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = user.bio ?: "", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Followers: ${user.followers} | Following: ${user.following}")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Repositories:", style = MaterialTheme.typography.titleMedium)
            } else {
                Text("Loading profile...")
            }
        }

        items(repos) { repo ->
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(text = repo.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = repo.description ?: "", style = MaterialTheme.typography.bodyMedium)
                Text(text = "⭐ ${repo.stargazers_count}")
            }
        }
    }
}
