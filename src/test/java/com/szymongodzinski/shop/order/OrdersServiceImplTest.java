package com.szymongodzinski.shop.order;

import com.szymongodzinski.shop.order.OrderThings.OrderThings;
import com.szymongodzinski.shop.order.OrderThings.OrderThingsRepository;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OrdersServiceImplTest {

    @Test
    void findAll_ShouldReturnAllProcessingReceivedFromRepository() {

        List<Orders> ordersList = prepareData();
        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        given(ordersService.findAll()).willReturn(ordersList);

        List<Orders> receivedOrders = ordersRepository.findAll();

        assertAll(
                () -> assertThat(receivedOrders, hasSize(4)),
                () -> assertThat(receivedOrders, hasItem(new Orders(1L, "Szymon", Collections.EMPTY_LIST))),
                () -> assertThat(receivedOrders, hasItem(new Orders(2L, "Szymon", Collections.EMPTY_LIST))),
                () -> assertThat(receivedOrders, hasItem(new Orders(3L, "Szymon", Collections.EMPTY_LIST))),
                () -> assertThat(receivedOrders, hasItem(new Orders(4L, "Szymon", Collections.EMPTY_LIST)))
        );
    }

    @Test
    void findAll_ShouldReturnNullIfRepositoryFindNothing() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        given(ordersService.findAll()).willReturn(Collections.emptyList());

        List<Orders> receivedOrders = ordersRepository.findAll();

        assertThat(receivedOrders, is(Collections.EMPTY_LIST));
    }

    @Test
    void findAll_ShouldCall_FindAll_Method() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        ordersRepository.findAll();

        verify(ordersRepository).findAll();
    }

    @Test
    void findById_ShouldReturnNullIfOrderIdIsNull() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        Orders orders = ordersService.findById(null);

        assertThat(orders, equalTo(null));
    }

    @Test
    void findById_ShouldReturnNullIfOrderIdIsEqualZero() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        Orders orders = ordersService.findById(0L);

        assertThat(orders, equalTo(null));
    }

    @Test
    void findById_ShouldReturnNullIfOrderIdIsLowerThanZero() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        Orders orders = ordersService.findById(-1L);

        assertThat(orders, equalTo(null));
    }

    @Test
    void findById_ShouldReturnNullIfRepositoryFindNothing() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        given(ordersRepository.findById(1L)).willReturn(Optional.empty());

        Orders orders = ordersService.findById(1L);

        assertThat(orders, equalTo(null));
    }

    @Test
    void findById_ShouldReturnValueGivenByRepository() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        Orders givenOrders = new Orders(1L, "Szymon", Collections.EMPTY_LIST);
        given(ordersRepository.findById(1L)).willReturn(Optional.of(givenOrders));

        Orders orders = ordersService.findById(1L);

        assertThat(orders, is(givenOrders));
    }

    @Test
    void findById_ShouldCall_FindById_IfEverythingIsOk() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        ordersService.findById(1L);

        verify(ordersRepository).findById(1L);
    }

    @Test
    void addOrder_ShouldReturnFalseIfCustomerNameIsNull() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        List<OrderThings> orderThingsList = List.of(new OrderThings(1L,1L,1));
        boolean isSuccess = ordersService.addOrder(null, orderThingsList);

        assertThat(isSuccess, is(false));
    }

    @Test
    void addOrder_ShouldReturnFalseIfCustomerNameIsEmpty() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        List<OrderThings> orderThingsList = List.of(new OrderThings(1L,1L,1));
        boolean isSuccess = ordersService.addOrder("", orderThingsList);

        assertThat(isSuccess, is(false));
    }

    @Test
    void addOrder_ShouldReturnFalseIfOrderThingsIsNull() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        boolean isSuccess = ordersService.addOrder("Szymon", null);

        assertThat(isSuccess, is(false));
    }

    @Test
    void addOrder_ShouldReturnFalseIfOrderThingsHaveNotThingId() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        List<OrderThings> orderThingsList = List.of(new OrderThings(0L, null, 1));
        boolean isSuccess = ordersService.addOrder("Szymon", orderThingsList);

        assertThat(isSuccess, is(false));
    }

    @Test
    void addOrder_ShouldReturnFalseIfOrderThingsHaveQuantityEqualZero() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        List<OrderThings> orderThingsList = List.of(new OrderThings(0L, 1L, 0));
        boolean isSuccess = ordersService.addOrder("Szymon", orderThingsList);

        assertThat(isSuccess, is(false));
    }

    @Test
    void addOrder_ShouldReturnFalseIfOrderThingsHaveQuantityLowerThanZero() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        List<OrderThings> orderThingsList = List.of(new OrderThings(0L, 1L, -1));
        boolean isSuccess = ordersService.addOrder("Szymon", orderThingsList);

        assertThat(isSuccess, is(false));
    }

    @Test
    void addOrder_ShouldReturnTrueIfEverythingIsOk() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        List<OrderThings> orderThingsList = List.of(new OrderThings(0L, 1L, 1));
        boolean isSuccess = ordersService.addOrder("Szymon", orderThingsList);

        assertThat(isSuccess, is(true));
    }

    @Test
    void addOrder_ShouldCall_Save_Method() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        List<OrderThings> orderThingsList = List.of(new OrderThings(0L, 1L, 1));
        ordersService.addOrder("Szymon", orderThingsList);

        new Orders(0L, "Szymon", orderThingsList);

        verify(orderThingsRepository).save(orderThingsList.get(0));
    }

    @Test
    void deleteOrder_ShouldReturnFalseIfOrderIdIsNull() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        boolean isSuccess = ordersService.deleteOrder(null);

        assertThat(isSuccess, is(false));
    }

    @Test
    void deleteOrder_ShouldReturnFalseIfOrderIdIsEqualZero() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        boolean isSuccess = ordersService.deleteOrder(0L);

        assertThat(isSuccess, is(false));
    }

    @Test
    void deleteOrder_ShouldReturnFalseIfOrderIdIsLowerThanZero() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        boolean isSuccess = ordersService.deleteOrder(-1L);

        assertThat(isSuccess, is(false));
    }

    @Test
    void deleteOrder_ShouldReturnFalseIfRepositoryFindNothing() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        given(ordersRepository.findById(1L)).willReturn(Optional.empty());

        boolean isSuccess = ordersService.deleteOrder(1L);

        assertThat(isSuccess, is(false));
    }

    @Test
    void deleteOrder_ShouldReturnTrueIfRepositoryFindObject() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        given(ordersRepository.findById(1L))
                .willReturn(Optional.of(new Orders(1L, "szymon", Collections.EMPTY_LIST)));

        boolean isSuccess = ordersService.deleteOrder(1L);

        assertThat(isSuccess, is(true));
    }

    @Test
    void deleteOrder_ShouldCall_Delete_Method() {

        OrdersRepository ordersRepository = mock(OrdersRepository.class);
        OrderThingsRepository orderThingsRepository = mock(OrderThingsRepository.class);
        OrdersService ordersService = new OrdersServiceImpl(ordersRepository,orderThingsRepository);

        Orders orders = new Orders(1L, "szymon", Collections.EMPTY_LIST);

        given(ordersRepository.findById(1L))
                .willReturn(Optional.of(orders));

        ordersService.deleteOrder(1L);

        verify(ordersRepository).delete(orders);
    }

    private List<Orders> prepareData() {
        Orders orders1 = new Orders(1L, "Szymon", Collections.EMPTY_LIST);
        Orders orders2 = new Orders(2L, "Szymon", Collections.EMPTY_LIST);
        Orders orders3 = new Orders(3L, "Szymon", Collections.EMPTY_LIST);
        Orders orders4 = new Orders(4L, "Szymon", Collections.EMPTY_LIST);

        List<Orders> ordersList = List.of(orders1, orders2, orders3, orders4);

        return ordersList;
    }

}
