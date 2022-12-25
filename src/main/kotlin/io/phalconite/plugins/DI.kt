package io.phalconite.plugins

import io.ktor.server.application.*
import io.phalconite.api.AuthService
import io.phalconite.data.facade.*
import io.phalconite.services.project.ProjectCommentService
import io.phalconite.services.project.ProjectService
import io.phalconite.services.project.ProjectTypeService
import io.phalconite.services.user.UserGroupService
import io.phalconite.services.user.UserService
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton

fun Application.configureDI() {
    di {
        // Facades
        bind<UserFacade> { singleton { UserFacadeImpl } }
        bind<UserAccessTokenFacade> { singleton { UserAccessTokenFacadeImpl } }
        bind<UserGroupFacade> { singleton { UserGroupFacadeImpl } }
        bind<UsersToUserGroupsFacade> { singleton { UsersToUserGroupsFacadeImpl } }
        bind<ProjectTypeFacade> { singleton { ProjectTypeFacadeImpl } }
        bind<ProjectFacade> { singleton { ProjectFacadeImpl } }
        bind<ProjectCommentFacade> { singleton { ProjectCommentFacadeImpl } }

        // Services & Providers
        bind { singleton { AuthService(instance(), instance()) } }
        bind { singleton { UserService(instance(), instance()) } }
        bind { singleton { UserGroupService(instance(), instance(), instance()) } }
        bind { singleton { ProjectTypeService(instance()) } }
        bind { singleton { ProjectService(instance(), instance()) } }
        bind { singleton { ProjectCommentService(instance(), instance(), instance()) } }
    }
}