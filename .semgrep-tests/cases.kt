// Synthetic fixtures only. Copied to an isolated production-shaped directory by the rule verifier.
// ruleid: pactogg-no-cleartext-source-url
val insecure = "http://example.invalid"
// ok: pactogg-no-cleartext-source-url
val secure = "https://example.invalid"
// ruleid: pactogg-no-trust-all-hostname-verifier
client.hostnameVerifier { _, _ -> true }
// ok: pactogg-no-trust-all-hostname-verifier
client.hostnameVerifier { _, session -> validate(session) }
// ruleid: pactogg-no-javascript-interface
view.addJavascriptInterface(bridge, "bridge")
// ok: pactogg-no-javascript-interface
view.removeJavascriptInterface("bridge")
// ruleid: pactogg-no-log-sensitive
Log.d("PactoGG", "token=$token")
// ok: pactogg-no-log-sensitive
Log.d("PactoGG", "pantalla abierta")
// ruleid: pactogg-no-world-accessible-files
openFileOutput("datos", Context.MODE_WORLD_READABLE)
// ok: pactogg-no-world-accessible-files
openFileOutput("datos", Context.MODE_PRIVATE)
// ruleid: pactogg-no-empty-trust-manager
override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
// ok: pactogg-no-empty-trust-manager
override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) { delegate.checkServerTrusted(chain, authType) }
// ruleid: pactogg-no-webview-file-access
settings.allowFileAccess = true
// ok: pactogg-no-webview-file-access
settings.allowFileAccess = false
