package com.example.infratrackmobile.core.security.model

/**
 * LocalSession is the local authenticated session state holder.
 * The backend remains the source of truth for authentication validity, roles and permissions.
 */
data class LocalSession(
    val accessToken: String,
    val userId: Long,
    val email: String,
    val role: String
)
