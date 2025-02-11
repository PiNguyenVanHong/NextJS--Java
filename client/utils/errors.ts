import { AuthError } from "next-auth";

export class CustomAuthError extends AuthError {
    static type: string;

    constructor(message?: any) {
        super();

        this.type = message;
    }
}

export class InvalidEmailPassordError extends AuthError {
    static type = "Email/Password is not valid"
}

export class InactiveAccountError extends AuthError {
    static type = "Your account is not active"
}

export class NotFoundAccountError extends AuthError {
    static type = "Your account is not exist"
}