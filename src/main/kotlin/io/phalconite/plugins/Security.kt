package io.phalconite.plugins

import io.ktor.server.auth.*
import io.ktor.server.application.*
import io.phalconite.api.AuthLoggedInUserDto
import io.phalconite.data.facade.UserAccessTokenFacade
import io.phalconite.data.facade.UserFacade
import io.phalconite.infra.UnauthorizedException
import io.phalconite.util.daysInMinutes
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.time.Period
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

const val CLIENT_AUTH_NAME = "client_auth"

fun Application.configureSecurity() {

    install(Authentication) {
        bearer(CLIENT_AUTH_NAME) {
            authenticate { tokenCredential ->
                val userAccessTokenFacade by closestDI().instance<UserAccessTokenFacade>()
                val userFacade by closestDI().instance<UserFacade>()
                val userAccessToken = userAccessTokenFacade.findByUid(tokenCredential.token) ?: throw UnauthorizedException
                val createdAtTime = userAccessToken.createdAt.toJavaLocalDateTime()
                val currentTime = java.time.LocalDateTime.now()
                val diffMinutes = ChronoUnit.MINUTES.between(createdAtTime, currentTime)
                if (diffMinutes > daysInMinutes) throw UnauthorizedException
                val user = userFacade.findByUid(userAccessToken.userUid) ?: return@authenticate null
                AuthLoggedInUserDto(
                    uid = user.uid,
                    fullName = user.fullName
                )
            }
        }
    }

    /*authentication {
        jwt {
            val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    .withAudience(jwtAudience)
                    .withIssuer(this@configureSecurity.environment.config.property("jwt.domain").getString())
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }*/
}
