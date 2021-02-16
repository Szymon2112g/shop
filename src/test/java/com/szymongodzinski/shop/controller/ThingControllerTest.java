package com.szymongodzinski.shop.controller;

import com.szymongodzinski.shop.thing.Thing;
import com.szymongodzinski.shop.thing.ThingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.mockito.BDDMockito.given;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ThingController.class)
public class ThingControllerTest {

    @MockBean
    ThingService thingService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void findAll_ShouldReturnAllThingFromThingService() throws Exception{

        given(thingService.findAll()).willReturn(prepareThingData());

        mockMvc.perform(get("/thing/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Bread"))
                .andExpect(jsonPath("$[0].price").value(new BigDecimal(2)))
                .andExpect(jsonPath("$[0].quantity").value(5))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Cheese"))
                .andExpect(jsonPath("$[1].price").value(new BigDecimal(3)))
                .andExpect(jsonPath("$[1].quantity").value(2));
    }

    @Test
    void findAll_ShouldReturnBadRequestIfServiceReturnNull() throws Exception {

        given(thingService.findAll()).willReturn(Collections.EMPTY_LIST);

        mockMvc.perform(get("/thing/all"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyIsEmpty() throws Exception {

        mockMvc.perform(post("/thing/"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyHaveNoName() throws Exception {

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"quantity\":\"1\",\"price\":\"2\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyHaveNoQuantity() throws Exception {

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"\",\"price\":\"2\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyHaveNoPrice() throws Exception {

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"1\",\"price\":\"\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyHaveNoValidPrice1() throws Exception {

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"1\",\"price\":\"sss\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyHaveNoValidPrice2() throws Exception {

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"1\",\"price\":\"2.2.2\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyHaveNoValidQuantity() throws Exception {

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"2s\",\"price\":\"1\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyHaveQuantityIsLowerThanZero() throws Exception {

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"-10\",\"price\":\"1\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfBodyHaveQuantityIsEqualZero() throws Exception {

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"0\",\"price\":\"1\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnBadRequestIfServiceThingDoNotAddThing() throws Exception {

        given(thingService.add(new Thing())).willReturn(false);

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"1\",\"price\":\"1\"}")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addThing_ShouldReturnResponseOkIfEverythingIsOk() throws Exception {

        given(thingService.add(new Thing(0L, "EGG", new BigDecimal(1), 1))).willReturn(true);

        mockMvc.perform(post("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"EGG\",\"quantity\":\"1\",\"price\":\"1\"}")
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateThing_ShouldReturnBadRequestIfNameIsEmpty() throws Exception {

        mockMvc.perform(put("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","")
                .param("quantity","5")
                .param("price","2")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateThing_ShouldReturnBadRequestIfQuantityIsEqualZero() throws Exception {

        mockMvc.perform(put("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","aaa")
                .param("quantity","0")
                .param("price","2")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateThing_ShouldReturnBadRequestIfQuantityIsLowerThanZero() throws Exception {

        mockMvc.perform(put("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","aaa")
                .param("quantity","-1")
                .param("price","2")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateThing_ShouldReturnBadRequestIfPriceIsEqualZero() throws Exception {

        mockMvc.perform(put("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","aaa")
                .param("quantity","1")
                .param("price","0")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateThing_ShouldReturnBadRequestIfPriceIsLowerThanZero() throws Exception {

        mockMvc.perform(put("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","aaa")
                .param("quantity","1")
                .param("price","-2")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateThing_ShouldReturnNotFoundIfServiceFindNothing() throws Exception {

        given(thingService.update(new Thing(0L, "Bread", new BigDecimal(2), 5))).willReturn(false);

        mockMvc.perform(put("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","Bread")
                .param("quantity","5")
                .param("price","2")
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateThing_ShouldReturnResponseOkIfServiceFindThing() throws Exception {

        given(thingService.update(new Thing(0L, "Bread", new BigDecimal(2), 5))).willReturn(true);

        mockMvc.perform(put("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","Bread")
                .param("quantity","5")
                .param("price","2")
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateThing_ShouldCall_Update_MethodIfEverythingIsOk() throws Exception {

        given(thingService.update(new Thing(0L, "Bread", new BigDecimal(2), 5))).willReturn(true);

        mockMvc.perform(put("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name","Bread")
                .param("quantity","5")
                .param("price","2")
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(thingService).update(new Thing(0L, "Bread", new BigDecimal(2), 5));
    }

    @Test
    void deleteThing_ShouldReturnBadRequestIfNameAndIdAreEmpty() throws Exception {

        mockMvc.perform(delete("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("name","")
                .header("id","-1")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteThing_ShouldReturnNotFoundIfServiceReturnFalse() throws Exception {

        given(thingService.delete(new Thing(1L, "Bread", new BigDecimal(2), 5))).willReturn(false);

        mockMvc.perform(delete("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("name","Bread")
                .header("id","1")
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteThing_ShouldReturnResponseOkIfServiceReturnTrue() throws Exception {

        given(thingService.delete(new Thing(1L, "Bread", BigDecimal.ZERO, 0))).willReturn(true);

        mockMvc.perform(delete("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("name","Bread")
                .header("id","1")
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteThing_ShouldCall_Delete_IfEverythingIsOk() throws Exception {

        given(thingService.delete(new Thing(1L, "Bread", BigDecimal.ZERO, 0))).willReturn(true);

        mockMvc.perform(delete("/thing/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("name","Bread")
                .header("id","1")
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(thingService).delete(new Thing(1L, "Bread", BigDecimal.ZERO, 0));
    }

    @Test
    void getThingByName_ShouldReturnNotFoundIfServiceFindNothing() throws Exception {

        given(thingService.getByName("Bread"))
                .willReturn(null);

        mockMvc.perform(get("/thing/Bread")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getThingByName_ShouldReturnNotFoundIfServiceFindThing() throws Exception {

        given(thingService.getByName("Bread"))
                .willReturn(new Thing(1L, "Bread", new BigDecimal(2), 5));

        mockMvc.perform(get("/thing/Bread")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getThingByName_ShouldCall_getByName_Method() throws Exception {

        given(thingService.getByName("Bread"))
                .willReturn(new Thing(1L, "Bread", new BigDecimal(2), 5));

        mockMvc.perform(get("/thing/Bread")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        verify(thingService).getByName("Bread");
    }

    private List<Thing> prepareThingData() {
        Thing thing1 = new Thing(1L, "Bread", new BigDecimal(2), 5);
        Thing thing2 = new Thing(2L, "Cheese", new BigDecimal(3), 2);

        List<Thing> thingList = new ArrayList<>();

        thingList.add(thing1);
        thingList.add(thing2);

        return thingList;
    }
}
