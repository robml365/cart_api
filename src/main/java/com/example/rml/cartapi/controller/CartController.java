package com.example.rml.cartapi.controller;

import com.example.rml.cartapi.model.Cart;
import com.example.rml.cartapi.model.Product;
import com.example.rml.cartapi.service.CartService;
import jakarta.el.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(){

        return ResponseEntity.ok(cartService.createCart());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) throws Exception {

        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PutMapping("/add/{id}")
    public ResponseEntity<Cart> addProductToCartById(@PathVariable Long id, @RequestBody ArrayList<Product> products){

        return ResponseEntity.ok(cartService.addProductToCartById(id, products));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCartById(@PathVariable Long id){
        if(cartService.deleteCartById(id) != null) {
            return ResponseEntity.ok("Cart deleted succesfully");
        }else{
            throw new PropertyNotFoundException();
        }
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCartNotFoundException(){
        return "Cart not found.";
    }



}