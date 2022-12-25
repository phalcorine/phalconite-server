package io.phalconite.infra

sealed class AppException(override val message: String) : RuntimeException(message) {
    object UnauthorizedException : AppException("Unauthorized Access!!!")
}

typealias UnauthorizedException = AppException.UnauthorizedException