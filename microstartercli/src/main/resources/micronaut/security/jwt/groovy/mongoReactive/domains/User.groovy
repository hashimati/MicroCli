package ${securityPackage}.domains

{securityPackage}.domains

import groovy.transform.Canonical
import ${securityPackage}.domains.LoginStatus
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Canonical
@Serdeable
class User {

    String id

    @NotNull
    @Size(min = 5, max = 15)
    String username

    @NotNull
    @Email
    String email
    HashSet<String> roles = new HashSet<>()

    @NotNull
    String password

    Date dateCreated

    Date dateUpdated

    boolean active 

    boolean disabled 
    boolean locked
//    Date expiration
//
//    Date passwordExpiration
    Instant lastTimeLogin
    Instant lastTimeTryToLogin
    LoginStatus lastLoginStatus

    String activationCode
    String resetPasswordCode


    boolean addRole(String role)
    {
        return roles.add(role)
    }
    boolean deleteRole(String role)
    {
        return roles.remove(role)
    }
}