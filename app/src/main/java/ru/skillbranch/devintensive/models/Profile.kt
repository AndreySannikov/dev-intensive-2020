package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils.transliteration

data class Profile(
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {

    init {

    }

    val nickname = listOf(transliteration(firstName), transliteration(lastName)).filter { it.isNotBlank() }.joinToString("_")
    val rank: String = "Junior Android Developer"

    fun toMap() = mapOf<String, Any>(
        "nickname" to nickname,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )

    companion object {

        private const val domain = "github.com"
        private val validSchemes = setOf("https://", "https://www.", "www.", "")
        private val forbiddenNames = setOf("enterprise", "features", "topics", "collections", "trending", "events",
            "marketplace", "pricing", "nonprofit", "customer-stories", "security", "login", "join")
        private val pathRegex = Regex("^/[a-z0-9]+(-?[a-z0-9]+)*/?$")

        fun validateRepository(url: String): Boolean {
            if (url.isEmpty()) {
                return true
            }

            val lowCased = url.toLowerCase()
            val domainIdx = lowCased.indexOf(domain)
            if (domainIdx < 0) {
                return false
            }

            val scheme = lowCased.substring(0, domainIdx)
            val path = lowCased.substring(domainIdx + domain.length)

            return validSchemes.contains(scheme)
                    && path.length > 1 && path.startsWith("/") && pathRegex.matches(path)
                    && !forbiddenNames.contains(path.substring(1))
        }
    }
}