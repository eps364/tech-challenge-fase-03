package br.com.fiap.restaurant.core.domain;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RestaurantTest {

    @Test
    void shouldCreateRestaurantWithCalculatedId() {
        Restaurant r = Restaurant.create("Foodie", "Street 1", "123", "Dist", "Comp", "City", "SP", "12345678", null);
        assertNotNull(r.getId());
        assertEquals("Foodie", r.getName());
    }

    @Test
    void shouldThrowExceptionWhenRequiredFieldIsMissing() {
        ValidationException ex = assertThrows(ValidationException.class, () -> 
            Restaurant.create("", "Street 1", "123", "Dist", "Comp", "City", "ST", "12345678", null)
        );
        assertEquals("name", ex.getField());
    }

    @Test
    void shouldActivateRestaurant() {
        Restaurant r = Restaurant.create("Foodie", "Street 1", "123", "Dist", "Comp", "City", "SP", "12345678", null);
        Restaurant activated = r.activate();
        assertTrue(activated.isActive());
    }

    @Test
    void shouldThrowWhenStateCodeIsInvalid() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
            Restaurant.create("Foodie", "Street 1", "123", "Dist", "Comp", "City", "XX", "12345678", null)
        );
        assertEquals("state", ex.getField());
    }

    @Test
    void shouldThrowWhenZipCodeFormatIsInvalid() {
        ValidationException ex = assertThrows(ValidationException.class, () ->
            Restaurant.create("Foodie", "Street 1", "123", "Dist", "Comp", "City", "SP", "12", null)
        );
        assertEquals("zipCode", ex.getField());
    }
}
