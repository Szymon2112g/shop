package com.szymongodzinski.shop.thing;

import java.util.List;

public interface ThingService {

    public List<Thing> findAll();
    public Thing getById(Long id);
    public boolean add(Thing thing);
    public boolean delete(Thing thing);
    public Thing getByName(String name);
    public boolean update(Thing thing);

}
