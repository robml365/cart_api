package com.example.rml.cartapi.service;

import com.example.rml.cartapi.model.Cart;
import com.example.rml.cartapi.model.Product;
import com.example.rml.cartapi.util.Constants;
import jakarta.el.PropertyNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartService {

    private HashMap<Long, Cart> cartsMap = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Cart createCart() {
        Long cartId = idCounter.getAndIncrement();
        Cart cart = new Cart(cartId);
        cartsMap.put(cartId, cart);
        return cart;
    }

    public Cart getCartById(Long cartId) {

        Cart cart = cartsMap.get(cartId);

        if (cart == null) {
            throw new NoSuchElementException();
        }
        cart.updateCartTimestamp();

        return cart;
    }

    public Cart addProductToCartById(Long cartId, ArrayList<Product> products) {

        Cart cart = cartsMap.get(cartId);

        if (cart == null) {
            throw new NoSuchElementException();
        }

        for (Product product : products) {

            validateProduct(product);

            if (cart.getProducts().containsKey(product.getId())) {

                Product productTemp = cart.getProducts().get(product.getId());
                productTemp.setAmount(productTemp.getAmount() + product.getAmount());

            } else {
                cart.getProducts().put(product.getId(), product);
            }
        }

        cart.updateCartTimestamp();

        cartsMap.put(cartId, cart);

        return cartsMap.get(cartId);
    }

    public Cart deleteCartById(long cartId) {

        return cartsMap.remove(cartId);

    }

    public void validateProduct(Product product) {
        if (product.getId() == null || product.getId() <= 0 || product.getAmount() <= 0) {
            throw new PropertyNotFoundException("Product or amount missing");
        }
    }

    @Scheduled(fixedRate = Constants.SCHEDULER_LAP_TIME)
    private void cleanCarts() {

        long actualTimestamp = System.currentTimeMillis();

        cartsMap.entrySet().removeIf(entry ->
                calculateDiffInMillis(entry.getValue().getTimestamp(), actualTimestamp)
        );

    }

    private Boolean calculateDiffInMillis(long timestamp1, long timestamp2) {

        long diffInMillis = Math.abs(timestamp1 - timestamp2);

        return diffInMillis >= Constants.CART_TTL;

    }

}