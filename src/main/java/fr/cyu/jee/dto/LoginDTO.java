package fr.cyu.jee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.util.Optional;

public class LoginDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String redirect;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public Optional<String> getRedirectOptional() {
        return Optional.ofNullable(redirect);
    }

    public String getRedirectOrHome() {
        return getRedirectOptional().filter(r -> !r.isBlank()).orElse("/");
    }
}
