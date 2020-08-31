package io.hashimati.micronautsecurityjwt.domains;

public enum LoginStatus {
    FAILED, SUCCEED, FAILED_DISABLED, FAILED_EXPIRED, FAILED_PASSWORD_EXPIRED, FAILED_LOCKED, FAILED_WRONG_PASSWORD
}