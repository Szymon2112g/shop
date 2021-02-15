package com.szymongodzinski.shop.thing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ThingServiceImpl implements ThingService {

    private ThingRepository thingRepository;

    @Autowired
    public ThingServiceImpl(ThingRepository thingRepository) {
        this.thingRepository = thingRepository;
    }

    @Override
    public List<Thing> findAll() {
        return thingRepository.findAll();
    }

    @Override
    public Thing getById(Long id) {

        if (id == null || id <= 0) {
            return null;
        }

        Optional<Thing> optionalThing = thingRepository.findById(id);

        if (optionalThing.isEmpty()) {
            return null;
        }

        return optionalThing.get();
    }

    @Override
    public boolean add(Thing thing) {

        if (thing == null || thing.getName() == null || thing.getName().equals("")) {
            return false;
        }

        if (thing.getQuantity() <= 0) {
            return false;
        }

        if (thing.getPrice().compareTo(BigDecimal.ZERO) != 1) {
            return false;
        }

        Thing thingFoundInDataBase = thingRepository.findByName(thing.getName());

        if (thingFoundInDataBase != null) {
            thingFoundInDataBase.setQuantity(thingFoundInDataBase.getQuantity() + thing.getQuantity());
            thingRepository.save(thingFoundInDataBase);
            return true;
        }

        thingRepository.save(thing);
        return true;
    }

    @Override
    public boolean delete(Thing thing) {

        if (thing == null) {
            return false;
        }

        Thing thingFoundInDataBase;

        if (thing.getName() == null || thing.getName().equals("")) {

            if (thing.getId() == null || thing.getId() <= 0) {
                return false;
            }

            thingFoundInDataBase = thingRepository.findById(thing.getId()).get();

        } else {
            thingFoundInDataBase = thingRepository.findByName(thing.getName());
        }

        if (thingFoundInDataBase == null) {
            return false;
        }

        thingRepository.delete(thingFoundInDataBase);
        return true;
    }

    @Override
    public Thing getByName(String name) {

        if (name == null || name.equals("")) {
            return null;
        }

        return thingRepository.findByName(name);
    }

    @Override
    public boolean update(Thing thing) {

        if (thing.getName() == null || thing.getName().equals("")) {
            return false;
        }

        Thing thingFoundInDataBase = thingRepository.findByName(thing.getName());

        if (thingFoundInDataBase == null) {
            return false;
        }

        thingFoundInDataBase.setQuantity(thing.getQuantity());

        if (thing.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            thingFoundInDataBase.setPrice(thing.getPrice());
        }

        thingRepository.save(thingFoundInDataBase);

        return true;
    }
}
