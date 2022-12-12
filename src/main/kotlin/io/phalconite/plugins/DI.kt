package io.phalconite.plugins

import io.ktor.server.application.*
import io.phalconite.api.AuthService
import io.phalconite.api.user.UserService
import io.phalconite.data.facade.UserAccessTokenFacade
import io.phalconite.data.facade.UserAccessTokenFacadeImpl
import io.phalconite.data.facade.UserFacade
import io.phalconite.data.facade.UserFacadeImpl
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun Application.configureDI() {
    di {
        // Facades
        bind<UserFacade> { singleton { UserFacadeImpl } }
        bind<UserAccessTokenFacade> { singleton { UserAccessTokenFacadeImpl } }

        // Services & Providers
        bind { singleton { AuthService(instance(), instance()) } }
        bind { singleton { UserService(instance()) } }
    }
}