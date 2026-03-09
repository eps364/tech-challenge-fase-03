package br.com.fiap.catalog.infra.product;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;
    
    @Column(name = "food_type", nullable = false)
    private String foodType;

    protected ProductEntity() {}

    public ProductEntity(Long id, String name, BigDecimal price, UUID restaurantId, String foodType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
        this.foodType = foodType;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public UUID getRestaurantId() { return restaurantId; }
    
    public String getFoodType() {
        return foodType;
    }
    
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
