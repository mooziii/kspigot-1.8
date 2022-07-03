package net.axay.kspigot.ipaddress

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import net.axay.kspigot.ipaddress.badipdetectionservices.GetIPIntel
import org.bukkit.entity.Player
import java.net.HttpURLConnection
import java.net.URL

/**
 * Checks if the IP address of the player is not a
 * normal residential address.
 *
 * This function returns true if just one of all
 * available detection services detects a bad IP address.
 *
 * @param detector The compound of detection services.
 */
fun Player.hasBadIP(detector: BadIPDetector = BadIPDetector.DEFAULT) =
    checkIP(detector).filterValues { it.isBad }.isNotEmpty()

/**
 * Checks if the IP address of the player is not a
 * normal residential address.
 *
 * This function returns the result of each
 * detection service given by the [detector].
 *
 * @param detector The compound of detection services.
 */
fun Player.checkIP(
    detector: BadIPDetector = BadIPDetector.DEFAULT,
    breakOnHit: Boolean = true,
): Map<BadIPDetectionService, BadIPDetectionResult> {
    val ip = address?.hostString ?: return emptyMap()
    return detector.checkIP(ip, breakOnHit)
}

/**
 * @param services A list of [BadIPDetectionService]s.
 * The order matters!
 *
 * Available services:
 *  - [net.axay.kspigot.ipaddress.badipdetectionservices.IPInfo] (recommended, but requires login)
 *  - [net.axay.kspigot.ipaddress.badipdetectionservices.GetIPIntel]
 *  - [net.axay.kspigot.ipaddress.badipdetectionservices.IPHub]
 *  - [net.axay.kspigot.ipaddress.badipdetectionservices.VPNBlocker]
 */
class BadIPDetector(
    private val services: List<BadIPDetectionService>,
) {
    /**
     * Alternative constructor.
     * @see BadIPDetector
     */
    constructor(vararg services: BadIPDetectionService) : this(services.toList())

    companion object {
        val DEFAULT = BadIPDetector(
            listOf(GetIPIntel())
        )
    }

    fun checkIP(ip: String, breakOnHit: Boolean = true) =
        HashMap<BadIPDetectionService, BadIPDetectionResult>().apply {
            for (it in services) {
                val curResult = it.isBad(ip)
                this[it] = curResult

                if (curResult.isBad && breakOnHit) break
            }
        }
}

enum class BadIPDetectionResult(
    val isBad: Boolean,
    val typeName: String,
) {
    GENERAL_BAD(true, "bad ip"),
    VPN(true, "vpn"),
    PROXY(true, "proxy"),
    TOR(true, "tor network"),
    HOSTING(true, "hosting"),
    GOOD(false, "valid ip"),
    ERROR(false, "error"),
    LIMIT(false, "limit");
}

abstract class BadIPDetectionService(
    val name: String,
) {
    protected abstract fun requestString(ip: String): String
    protected open fun requestHeaders() = emptyMap<String, String>()

    protected abstract fun interpreteResult(result: JsonObject): BadIPDetectionResult

    fun isBad(ip: String): BadIPDetectionResult {
        val con = URL(requestString(ip)).openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        requestHeaders().forEach { (field, value) -> con.setRequestProperty(field, value) }
        con.connect()

        if (con.responseCode == 429)
            return BadIPDetectionResult.LIMIT
        else {
            val result = try {
                con.inputStream.use { Json.decodeFromString<JsonObject>(it.readAllBytes().decodeToString()) }
            } catch (exc: SerializationException) {
                null
            } ?: return BadIPDetectionResult.ERROR

            return try {
                interpreteResult(result)
            } catch (exc: Exception) {
                return BadIPDetectionResult.ERROR
            }
        }
    }
}
