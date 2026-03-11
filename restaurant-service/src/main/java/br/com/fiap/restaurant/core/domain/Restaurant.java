package br.com.fiap.restaurant.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class Restaurant {
    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");
    private static final Set<String> VALID_STATES = Set.of(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
            "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    private final UUID id;
    private final String name;
    private final boolean active;
    private final String street;
    private final String number;
    private final String district;
    private final String complement;
    private final String city;
    private final String state;
    private final String zipCode;
    private final List<UUID> owners;

    public Restaurant(UUID id, String name, boolean active,
                       String street, String number, String district, String complement,
                       String city, String state, String zipCode,
                       List<UUID> owners) {
        validate(name, street, number, city, state, zipCode);
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name.trim();
        this.active = active;
        this.street = street.trim();
        this.number = number.trim();
        this.district = normalizeOptional(district);
        this.complement = normalizeOptional(complement);
        this.city = city.trim();
        this.state = state.trim().toUpperCase(Locale.ROOT);
        this.zipCode = zipCode.trim().replace("-", "");
        this.owners = owners != null ? new ArrayList<>(owners) : new ArrayList<>();
    }

    public static Restaurant create(String name, String street, String number, String district, 
                                     String complement, String city, String state, String zipCode, List<UUID> owners) {
        return new Restaurant(null, name, false, street, number, district, complement, city, state, zipCode, owners);
    }

    private void validate(String name, String street, String number, String city, String state, String zipCode) {
        if (name == null || name.isBlank()) throw new ValidationException("name", "The name is required");
        if (street == null || street.isBlank()) throw new ValidationException("street", "The street is required");
        if (number == null || number.isBlank()) throw new ValidationException("number", "The number is required");
        if (city == null || city.isBlank()) throw new ValidationException("city", "The city is required");
        if (state == null || state.isBlank()) throw new ValidationException("state", "The state is required");
        if (zipCode == null || zipCode.isBlank()) throw new ValidationException("zipCode", "The zip code is required");
        if (!VALID_STATES.contains(state.trim().toUpperCase(Locale.ROOT))) {
            throw new ValidationException("state", "The state code is invalid");
        }
        if (!ZIP_CODE_PATTERN.matcher(zipCode.trim()).matches()) {
            throw new ValidationException("zipCode", "The zip code format is invalid");
        }
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    public UUID getId()           { return id; }
    public String getName()       { return name; }
    public boolean isActive()      { return active; }
    public String getStreet()     { return street; }
    public String getNumber()     { return number; }
    public String getDistrict()   { return district; }
    public String getComplement() { return complement; }
    public String getCity()       { return city; }
    public String getState()      { return state; }
    public String getZipCode()    { return zipCode; }
    public List<UUID> getOwners() { return owners; }

    public Restaurant activate() {
        return new Restaurant(this.id, this.name, true,
                this.street, this.number, this.district, this.complement,
                this.city, this.state, this.zipCode, this.owners);
    }

    public Restaurant addOwner(UUID ownerId) {
        List<UUID> updated = new ArrayList<>(this.owners);
        if (!updated.contains(ownerId)) {
            updated.add(ownerId);
        }
        return new Restaurant(this.id, this.name, this.active,
                this.street, this.number, this.district, this.complement,
                this.city, this.state, this.zipCode, updated);
    }
}