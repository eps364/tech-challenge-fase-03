package br.com.fiap.orchestrator.core.dto;

import java.math.BigInteger;

public class CatalogFoodResponse {
    private String id;
    private BigInteger price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }
}