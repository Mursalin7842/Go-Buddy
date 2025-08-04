package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.Project

/**
 * Interface for the project repository. Defines the contract for all
 * project-related data operations.
 */
interface ProjectRepository {
    /**
     * Fetches all projects for the current user.
     * @return A Result containing the list of Projects on success, or an Exception on failure.
     */
    suspend fun getProjects(): Result<List<Project>>
}