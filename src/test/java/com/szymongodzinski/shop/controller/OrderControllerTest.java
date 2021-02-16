package com.szymongodzinski.shop.controller;

import com.szymongodzinski.shop.order.OrderThings.OrderThings;
import com.szymongodzinski.shop.order.Orders;
import com.szymongodzinski.shop.order.OrdersService;
import com.szymongodzinski.shop.thing.Thing;
import com.szymongodzinski.shop.thing.ThingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdersController.class)
public class OrderControllerTest {

    @MockBean
    ThingService thingService;

    @MockBean
    OrdersService ordersService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void findAll_ShouldReturnAllThingFromThingService() throws Exception {

        given(ordersService.findAll()).willReturn(prepareOrdersData());

        mockMvc.perform(get("/orders/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].customerName").value("Szymon"))
                .andExpect(jsonPath("$[0].ordersThings[0].id").value("1"))
                .andExpect(jsonPath("$[0].ordersThings[0].thingId").value("1"))
                .andExpect(jsonPath("$[0].ordersThings[0].quantity").value("2"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].customerName").value("Szymon2"));
    }

    @Test
    void findAll_ShouldReturnNotFoundIfServiceReturnNull() throws Exception {

        given(ordersService.findAll()).willReturn(null);

        mockMvc.perform(get("/orders/all"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_ShouldCall_FindAll_IfEverythingIsOk() throws Exception {

        given(ordersService.findAll()).willReturn(prepareOrdersData());

        mockMvc.perform(get("/orders/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].customerName").value("Szymon"))
                .andExpect(jsonPath("$[0].ordersThings[0].id").value("1"))
                .andExpect(jsonPath("$[0].ordersThings[0].thingId").value("1"))
                .andExpect(jsonPath("$[0].ordersThings[0].quantity").value("2"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].customerName").value("Szymon2"));

        verify(ordersService).findAll();
    }

    @Test
    void findOrderById_ShouldReturnBadRequestIfOrderIdIsNull() throws Exception {

        mockMvc.perform(get("/orders/")
                .header("orderId","")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findOrderById_ShouldReturnBadRequestIfOrderIdIsLowerThanZero() throws Exception {

        mockMvc.perform(get("/orders/")
                .header("orderId","-1")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findOrderById_ShouldReturnBadRequestIfOrderIdIsEqualZero() throws Exception {

        mockMvc.perform(get("/orders/")
                .header("orderId","0")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findOrderById_ShouldReturnNotFoundIfServiceFindNothing() throws Exception {

        given(ordersService.findById(1L)).willReturn(null);

        mockMvc.perform(get("/orders/")
                .header("orderId","1")
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void findOrderById_ShouldReturnResponseOkIfServiceFindOrders() throws Exception {

        given(ordersService.findById(1L))
                .willReturn(new Orders(1L, "Szymon2", Collections.EMPTY_LIST));

        mockMvc.perform(get("/orders/")
                .header("orderId","1")
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findOrderById_ShouldCall_FindById_Method() throws Exception {

        given(ordersService.findById(1L))
                .willReturn(new Orders(1L, "Szymon2", Collections.EMPTY_LIST));

        mockMvc.perform(get("/orders/")
                .header("orderId","1")
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(ordersService).findById(1L);
    }

    @Test
    void addOrder_ShouldReturnBadRequestIfCustomerNameIsEmpty() throws Exception {

        mockMvc.perform(post("/orders/")
                .header("customerName","")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addOrder_ShouldReturnBadRequestIfThingsIsEmpty() throws Exception {

        mockMvc.perform(post("/orders/")
                .header("customerName","Szymon")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"1\":\"\"")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addOrder_ShouldReturnResponseOkIfServiceFindThingAndOrders() throws Exception {

        given(thingService.getById(1L)).willReturn(new Thing(1L,"name1",BigDecimal.ONE,2));
        given(thingService.getById(2L)).willReturn(new Thing(2L,"name2",BigDecimal.ONE,2));

        given(ordersService.addOrder(any(), any())).willReturn(true);

        mockMvc.perform(post("/orders/")
                .header("customerName","Szymon")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"1\":\"1\",\"2\":\"2\"}")
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addOrder_ShouldReturnBadRequestIfOrderServiceReturnFalse() throws Exception {

        given(thingService.getById(1L)).willReturn(new Thing(1L,"name1",BigDecimal.ONE,2));
        given(thingService.getById(2L)).willReturn(new Thing(2L,"name2",BigDecimal.ONE,2));

        given(ordersService.addOrder(any(), any())).willReturn(false);

        mockMvc.perform(post("/orders/")
                .header("customerName","Szymon")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"1\":\"1\",\"2\":\"2\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteOrder_ShouldReturnBadRequestIfOrderIdIsEmpty() throws Exception {

        mockMvc.perform(delete("/orders/")
                .header("ordersId","")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteOrder_ShouldReturnBadRequestIfServiceReturnFalse() throws Exception {

        given(ordersService.deleteOrder(1L)).willReturn(false);

        mockMvc.perform(delete("/orders/")
                .header("ordersId","1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteOrder_ShouldReturnResponseOkIfServiceReturnTrue() throws Exception {

        given(ordersService.deleteOrder(1L)).willReturn(true);

        mockMvc.perform(delete("/orders/")
                .header("ordersId","1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(ordersService).deleteOrder(1L);
    }

    private List<Orders> prepareOrdersData() {

        List<OrderThings> orderThingsList = new ArrayList<>();
        orderThingsList.add(new OrderThings(1L,1L,2));

        Orders orders1 = new Orders(1L, "Szymon", orderThingsList);
        Orders orders2 = new Orders(2L, "Szymon2", Collections.EMPTY_LIST);

        List<Orders> ordersList = new ArrayList<>();

        ordersList.add(orders1);
        ordersList.add(orders2);

        return ordersList;
    }
}
