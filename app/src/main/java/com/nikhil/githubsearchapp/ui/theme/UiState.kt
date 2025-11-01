package com.nikhil.githubsearchapp.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.githubsearchapp.data.GHRepo
import com.nikhil.githubsearchapp.network.Result
import com.nikhil.githubsearchapp.repo.RepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
    val username: String = "",
    val filterQuery: String = "",
    val repos: List<GHRepo> = emptyList(),
    val filteredRepos: List<GHRepo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repoRepository: RepoRepository
) : ViewModel() {

    private val TAG = "SearchViewModel"

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateUsername(newUsername: String) {
        Log.d(TAG, "Username updated: $newUsername")
        _uiState.update { it.copy(username = newUsername) }
    }

    fun updateFilter(query: String) {
        Log.d(TAG, "Applying filter: $query")
        _uiState.update { state ->
            val filtered = state.repos.filter {
                it.name.contains(query, ignoreCase = true) || it.id.toString().contains(query)
            }
            Log.d(TAG, "Filter result: ${filtered.size} repos matched")
            state.copy(filterQuery = query, filteredRepos = filtered)
        }
    }

    fun fetchRepos() {
        val username = _uiState.value.username
        if (username.isBlank()) {
            Log.w(TAG, "fetchRepos() called with blank username")
            return
        }

        Log.d(TAG, "Fetching repos for user: $username")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = repoRepository.getRepos(username)
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "Repos fetched successfully: ${result.data.size} repos")
                    _uiState.update {
                        it.copy(
                            repos = result.data,
                            filteredRepos = result.data,
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    Log.e(TAG, "Error fetching repos: ${result.exception.message}")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }
}
