package com.example.rml.cartapi.model;

import java.util.HashMap;

public class Cart {

    private Long id;
    private HashMap<Long, Product> products;
    private long timestamp;

    public Cart(Long id){
        this.id = id;
        this.products = new HashMap<Long, Product>();
        this.timestamp = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HashMap<Long,Product> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Long,Product> products) {
        this.products = products;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void updateCartTimestamp(){
        this.timestamp = System.currentTimeMillis();
    }



}