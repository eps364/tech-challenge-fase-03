package br.com.fiap.authservice.core.domain;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class User {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9._-]{3,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final UUID id;
    private final String username;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final List<String> roles;

    public User(UUID id, String username, String email, String firstName, String lastName, List<String> roles) {
        validate(username, email, firstName, lastName, roles);
        this.id = id != null ? id : UUID.randomUUID();
        this.username = username.trim();
        this.email = email.trim();
        this.firstName = normalizeOptional(firstName);
        this.lastName = normalizeOptional(lastName);
        this.roles = roles != null
                ? roles.stream().map(String::trim).toList()
                : List.of();
    }

    public static User create(String username, String email, String firstName, String lastName) {
        return new User(null, username, email, firstName, lastName, null);
    }

    private void validate(String username, String email, String firstName, String lastName, List<String> roles) {
        if (username == null || username.isBlank()) throw new ValidationException("username", "The username is required");
        if (email == null || email.isBlank()) throw new ValidationException("email", "The email is required");
        if (!USERNAME_PATTERN.matcher(username.trim()).matches()) {
            throw new ValidationException("username", "The username must contain 3 to 50 valid characters");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("email", "The email format is invalid");
        }
        validateOptionalName(firstName, "firstName");
        validateOptionalName(lastName, "lastName");
        if (roles != null && roles.stream().anyMatch(role -> role == null || role.isBlank())) {
            throw new ValidationException("roles", "The roles cannot contain blank values");
        }
    }

    private void validateOptionalName(String name, String field) {
        if (name == null) {
            return;
        }
        String normalized = name.trim();
        if (normalized.isEmpty()) {
            throw new ValidationException(field, "The " + field + " cannot be blank");
        }
        if (normalized.length() > 80) {
            throw new ValidationException(field, "The " + field + " must have up to 80 characters");
        }
    }

    private String normalizeOptional(String value) {
        return value == null ? null : value.trim();
    }

    public User withId(UUID id) {
        return new User(id, username, email, firstName, lastName, roles);
    }

    public User withRoles(List<String> roles) {
        return new User(id, username, email, firstName, lastName, roles);
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public List<String> getRoles() { return roles; }
}
