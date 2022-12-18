package io.phalconite.domain

sealed interface DomainError {
    val message: String
    sealed interface AuthException : DomainError {
        object InvalidCredentials : AuthException {
            override val message: String
                get() = "Invalid Credentials!"
        }
    }
    object EntityNotFoundError : DomainError {
        override val message: String
            get() = "A record with the specified criteria was not found!"
    }
    data class ConflictRecordError(override val message: String) : DomainError
    object DatabaseError : DomainError {
        override val message: String
            get() = "A database error occurred!"
    }
    data class UnknownError(override val message: String) : DomainError
    data class BadRequestError(override val message: String): DomainError
    object UnprocessableEntity : DomainError {
        override val message: String
            get() = "The request body is in an invalid format. Please check and try again..."
    }
}

typealias AuthInvalidCredentials = DomainError.AuthException.InvalidCredentials
typealias AppUnknownError = DomainError.UnknownError
typealias EntityNotFoundError = DomainError.EntityNotFoundError
typealias ConflictRecordError = DomainError.ConflictRecordError
typealias DatabaseError = DomainError.DatabaseError
typealias BadRequestError = DomainError.BadRequestError
typealias UnprocessableEntity = DomainError.UnprocessableEntity