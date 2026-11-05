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
