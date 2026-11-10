package com.pactogg.app.feature.account.presentation

internal fun parseAccountLink(raw: String?): Pair<ExtrasPage, String>? = runCatching {
    val uri = java.net.URI(requireNotNull(raw))
    require(uri.scheme == "pactogg" && uri.host == "auth")
    require(uri.userInfo == null && uri.port == -1 && uri.fragment == null)
    val page = mapOf("/verify-email" to ExtrasPage.VERIFY, "/reset-password" to ExtrasPage.RESET)[uri.path]
    requireNotNull(page)
    val parts = requireNotNull(uri.rawQuery).split("&")
    require(parts.size == 1 && parts[0].startsWith("token="))
    val token = java.net.URLDecoder.decode(parts[0].substringAfter("="), "UTF-8")
    require(token.length in 16..128 && token.none(Char::isWhitespace))
    page to token
}.getOrNull()
