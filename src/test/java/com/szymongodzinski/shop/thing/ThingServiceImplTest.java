package com.szymongodzinski.shop.thing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ThingServiceImplTest {

    @Test
    void findAll_ShouldReturnAllThingsFromRepository() {

        List<Thing> things = prepareThingData();
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);
        given(thingRepository.findAll()).willReturn(things);

        List<Thing> thingList = thingService.findAll();

        assertAll(
                () -> assertThat(thingList, hasSize(4)),
                () -> assertThat(thingList, hasItem(new Thing(1L, "Bread", new BigDecimal(2), 5))),
                () -> assertThat(thingList, hasItem(new Thing(2L, "Cheese", new BigDecimal(3), 2))),
                () -> assertThat(thingList, hasItem(new Thing(3L, "Egg", new BigDecimal("0.4"), 50))),
                () -> assertThat(thingList, hasItem(new Thing(4L, "Sandwich", new BigDecimal(5), 1)))
                );
    }

    @Test
    void findAll_ShouldReturnEmptyListIfRepositoryReturnEmptyList() {

        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);
        given(thingRepository.findAll()).willReturn(Collections.emptyList());

        List<Thing> thingList = thingService.findAll();

        assertThat(thingList, hasSize(0));
    }

    @Test
    void getById_ShouldReturnNullWhenGivenIdIsEqualZero() {

        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);
        given(thingRepository.findById(0L)).willReturn(null);

        Thing thing = thingService.getById(0L);

        assertThat(thing, nullValue());
    }

    @Test
    void getById_ShouldReturnNullWhenGivenIdIsLowerThanZero() {

        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);
        given(thingRepository.findById(-1L)).willReturn(null);

        Thing thing = thingService.getById(-1L);

        assertThat(thing, nullValue());
    }

    @Test
    void getById_ShouldReturnNullWhenThingNotExist() {

        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);
        given(thingRepository.findById(22L)).willReturn(Optional.empty());

        Thing thing = thingService.getById(22L);

        assertThat(thing, nullValue());
    }

    @Test
    void getById_ShouldReturnThingWhenRepositoryFindThing() {

        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(22L, "Bread", new BigDecimal(2), 5);
        Optional<Thing> optionalThing = Optional.of(thing);
        given(thingRepository.findById(22L)).willReturn(optionalThing);

        Thing receivedThing = thingService.getById(22L);

        assertThat(receivedThing, is(thing));
    }

    @Test
    void add_ShouldReturnFalseIfThingIsNull() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        boolean result = thingService.add(null);
        assertThat(result, is(false));
    }

    @Test
    void add_ShouldReturnFalseIfThingHaveNotName() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"", BigDecimal.ONE,1);

        boolean result = thingService.add(thing);
        assertThat(result, is(false));
    }

    @Test
    void add_ShouldReturnFalseIfThingHaveNotQuantity() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"name", BigDecimal.ONE,0);

        boolean result = thingService.add(thing);
        assertThat(result, is(false));
    }

    @Test
    void add_ShouldReturnFalseIfThingHavePriceEqualZero() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"name", new BigDecimal(0),1);

        boolean result = thingService.add(thing);
        assertThat(result, is(false));
    }

    @Test
    void add_ShouldReturnFalseIfThingHavePriceLowerThanZero() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"name", new BigDecimal(-1),1);

        boolean result = thingService.add(thing);
        assertThat(result, is(false));
    }

    @Test
    void add_ShouldCallSaveMethodAndIncrementQuantityWithRepositoryWhenThingAlreadyExist() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);
        Thing thing = new Thing(1L,"name", new BigDecimal(1),1);
        given(thingRepository.findByName(thing.getName())).willReturn(thing);

        thingService.add(thing);

        verify(thingRepository).save(thing);

        boolean result = thingService.add(thing);
        assertThat(result, is(true));
    }

    @Test
    void add_ShouldCallSaveMethodIfThingNotExist() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);
        Thing thing = new Thing(1L,"name", new BigDecimal(1),1);
        given(thingRepository.findByName(thing.getName())).willReturn(null);

        thingService.add(thing);

        verify(thingRepository).save(thing);

        boolean result = thingService.add(thing);
        assertThat(result, is(true));
    }

    @Test
    void delete_ShouldReturnFalseIfThingIsNull() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        boolean result = thingService.delete(null);

        assertThat(result, is(false));
    }

    @Test
    void delete_ShouldReturnFalseIfThingHaveNotNameAndId() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(0L,"", new BigDecimal(1),1);
        boolean result = thingService.delete(thing);

        assertThat(result, is(false));
    }

    @Test
    void delete_ShouldCallMethod_FindByName_IfNameExist() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(0L,"name", new BigDecimal(1),1);
        thingService.delete(thing);

        verify(thingRepository).findByName("name");
    }

    @Test
    void delete_ShouldCallMethod_FindById_IfIdExist() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"", new BigDecimal(1),1);

        Optional<Thing> optionalThing = Optional.of(thing);
        given(thingRepository.findById(1L)).willReturn(optionalThing);

        thingService.delete(thing);

        verify(thingRepository).findById(1L);
    }

    @Test
    void delete_ShouldReturnFalseIfRepositoryNotFindThing() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        given(thingRepository.findById(1L)).willReturn(null);
        given(thingRepository.findByName("name")).willReturn(null);

        Thing thing = new Thing(1L,"name", new BigDecimal(1),1);
        boolean result = thingService.delete(thing);

        assertThat(result, is(false));
    }

    @Test
    void delete_ShouldReturnTrueIfRepositoryFindThing() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"name", new BigDecimal(1),1);

        given(thingRepository.findByName("name")).willReturn(thing);
        boolean result = thingService.delete(thing);

        assertThat(result, is(true));
    }

    @Test
    void delete_ShouldCall_Delete_MethodIfEverythingIsOk() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"name", new BigDecimal(1),1);

        given(thingRepository.findByName("name")).willReturn(thing);
        thingService.delete(thing);

        verify(thingRepository).delete(thing);
    }

    @Test
    void getByName_ShouldReturnNullIfNameIsEmpty() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = thingService.getByName("");

        assertThat(thing, equalTo(null));
    }

    @Test
    void getByName_ShouldCall_FindByName_MethodIfEverythingIsOk() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        thingService.getByName("aaa");

        verify(thingRepository).findByName("aaa");
    }

    @Test
    void getByName_ShouldReturnValueIfRepositoryFindThing() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"aaa", new BigDecimal(1),1);
        given(thingRepository.findByName("aaa")).willReturn(thing);

        Thing receivedThing = thingService.getByName("aaa");

        assertThat(receivedThing, is(thing));
    }

    @Test
    void getByName_ShouldReturnNullIfRepositoryNothingFind() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        given(thingRepository.findByName("aaa")).willReturn(null);

        Thing receivedThing = thingService.getByName("aaa");

        assertThat(receivedThing, equalTo(null));
    }

    @Test
    void update_ShouldReturnFalseIfNameIsEmpty() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"", new BigDecimal(1),1);
        boolean isSuccess = thingService.update(thing);

        assertThat(isSuccess, is(false));
    }

    @Test
    void update_ShouldCall_FindByName_MethodIfNameExist() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"Egg", new BigDecimal(1),1);
        thingService.update(thing);

        verify(thingRepository).findByName("Egg");
    }

    @Test
    void update_ShouldReturnFalseIfThingNotExistInRepository() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        given(thingRepository.findByName("Egg")).willReturn(null);

        Thing thing = new Thing(1L,"Egg", new BigDecimal(1),1);
        boolean isSuccess = thingService.update(thing);

        assertThat(isSuccess, is(false));
    }

    @Test
    void update_ShouldReturnTrueIfRepositoryFindThing() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"Egg", new BigDecimal(1),1);
        given(thingRepository.findByName("Egg")).willReturn(thing);

        boolean isSuccess = thingService.update(thing);

        assertThat(isSuccess, is(true));
    }

    @Test
    void update_ShouldCall_Save_MethodIfEverythingIsOk() {
        ThingRepository thingRepository = mock(ThingRepository.class);
        ThingService thingService = new ThingServiceImpl(thingRepository);

        Thing thing = new Thing(1L,"Egg", new BigDecimal(1),1);
        given(thingRepository.findByName("Egg")).willReturn(thing);

        thingService.update(thing);

        verify(thingRepository).save(thing);
    }

    private List<Thing> prepareThingData() {
        Thing thing1 = new Thing(1L, "Bread", new BigDecimal(2), 5);
        Thing thing2 = new Thing(2L, "Cheese", new BigDecimal(3), 2);
        Thing thing3 = new Thing(3L, "Egg", new BigDecimal("0.4"), 50);
        Thing thing4 = new Thing(4L, "Sandwich", new BigDecimal(5), 1);

        List<Thing> thingList = new ArrayList<>();

        thingList.add(thing1);
        thingList.add(thing2);
        thingList.add(thing3);
        thingList.add(thing4);

        return thingList;
    }

}
