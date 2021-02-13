package com.szymongodzinski.shop.controller;

import com.szymongodzinski.shop.thing.Thing;
import com.szymongodzinski.shop.thing.ThingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class ThingController {

    private ThingService thingService;

    @Autowired
    public ThingController(ThingService thingService) {
        this.thingService = thingService;
    }

    @GetMapping(path = "/thing/all")
    public List<Thing> findAllThings() {
        return thingService.findAll();
    }

    @PostMapping(path = "/thing/", produces = { "application/json" })
    public ResponseEntity<?> addThing(@RequestBody Map<String, String> thingBody) {

        if (!thingBody.containsKey("name") || !thingBody.containsKey("quantity") || !thingBody.containsKey("price")) {
            return ResponseEntity.badRequest().header("Cause", "body should have field \"name\", " +
                    "\"quantity\" and \"price\"").build();
        }

        String name = thingBody.get("name");
        String quantity = thingBody.get("quantity");
        String price = thingBody.get("price");

        if (checkIfStringIsEmpty(name) || checkIfStringIsEmpty(quantity) || checkIfStringIsEmpty(price)) {
            return ResponseEntity.badRequest().header("Cause", "name, quantity, price should have value").build();
        }

        if (!checkIfStringIsValueFloatingPoint(price) && !checkIfStringIsInteger(price)) {
            return ResponseEntity.badRequest().header("Cause", "price is not valid").build();
        }

        if (!checkIfStringIsInteger(quantity)) {
            return ResponseEntity.badRequest().header("Cause", "quantity is not valid").build();
        }

        int quantityInt = Integer.parseInt(quantity);
        if (quantityInt <= 0){
            return ResponseEntity.badRequest().header("Cause", "quantity is 0 or lower").build();
        }

        Thing thing = new Thing(0L, name, new BigDecimal(price), quantityInt);

        if (!thingService.add(thing)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    private boolean checkIfStringIsValueFloatingPoint(String s) {
        return s.matches("\\d*+[.]+\\d*");
    }

    private boolean checkIfStringIsInteger(String s) {
        return s.matches("\\d*");
    }

    private boolean checkIfStringIsEmpty(String s) {
        if (s == null || s.equals("")) {
            return true;
        }
        return false;
    }

    @PutMapping(path = "/thing/", produces = { "application/json" })
    public ResponseEntity<?> updateThing(@RequestParam("name") String name,
                                         @RequestParam("quantity") int quantity,
                                         @RequestParam("price") BigDecimal price) {

        Thing thing = new Thing(0L, name, price, quantity);

        boolean isSuccess = thingService.update(thing);

        if (!isSuccess) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/thing/", produces = { "application/json" })
    public ResponseEntity<?> deleteThing(@RequestHeader("name") String name) {

        Thing thing = new Thing(0L, name, BigDecimal.ZERO , 0);

        boolean isSuccess = thingService.delete(thing);

        if (!isSuccess) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/thing/{name}", produces = { "application/json" })
    public ResponseEntity<?> getThingByName(@PathVariable String name) {

        Thing thing = thingService.getByName(name);

        if (thing == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(thing);
    }
}
