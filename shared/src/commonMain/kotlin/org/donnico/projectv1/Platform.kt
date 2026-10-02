package org.donnico.projectv1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform