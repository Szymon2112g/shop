package com.szymongodzinski.shop.processing;

import com.szymongodzinski.shop.order.Orders;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProcessingServiceImplTest {

    @Test
    void findAll_ShouldReturnAllProcessingReceivedFromRepository() {

        List<Processing> processingList = prepareData();
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        given(processingRepository.findAll()).willReturn(processingList);

        List<Processing> receivedProcessing = processingService.findAll();

        assertAll(
                () -> assertThat(receivedProcessing, hasSize(5)),
                () -> assertThat(receivedProcessing, hasItem(new Processing(1L, State.REJECTED, new Orders()))),
                () -> assertThat(receivedProcessing, hasItem(new Processing(2L, State.READY, new Orders()))),
                () -> assertThat(receivedProcessing, hasItem(new Processing(3L, State.IN_PROGRESS, new Orders()))),
                () -> assertThat(receivedProcessing, hasItem(new Processing(5L, State.REJECTED, new Orders()))),
                () -> assertThat(receivedProcessing, hasItem(new Processing(5L, State.REJECTED, new Orders())))
        );
    }

    @Test
    void findAll_ShouldReturnNullIfRepositoryReturnNull() {

        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        given(processingRepository.findAll()).willReturn(null);

        List<Processing> receivedProcessing = processingService.findAll();

        assertThat(receivedProcessing, equalTo(null));
    }

    @Test
    void findAll_ShouldUse_FindAll_MethodFromRepository() {

        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        processingService.findAll();

        verify(processingRepository).findAll();
    }

    @Test
    void findById_ShouldReturnNullIfProcessingIdIsNull() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = processingService.findById(null);

        assertThat(processing, equalTo(null));
    }

    @Test
    void findById_ShouldReturnNullIfProcessingIdIsEqualZero() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = processingService.findById(0L);

        assertThat(processing, equalTo(null));
    }

    @Test
    void findById_ShouldReturnNullIfProcessingIdIsLowerThanZero() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = processingService.findById(-1L);

        assertThat(processing, equalTo(null));
    }

    @Test
    void findById_ShouldReturnNullIfRepositoryNotFindObject() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        given(processingRepository.findById(1L)).willReturn(Optional.empty());

        Processing processing = processingService.findById(1L);

        assertThat(processing, equalTo(null));
    }

    @Test
    void findById_ShouldReturnExpectedProcessing() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing givenProcessing = new Processing(1L, State.REJECTED, new Orders());
        Optional<Processing> optionalProcessing = Optional.of(givenProcessing);

        given(processingRepository.findById(1L)).willReturn(optionalProcessing);

        Processing processing = processingService.findById(1L);

        assertThat(processing, equalTo(givenProcessing));
    }

    @Test
    void findById_ShouldCall_FindByID_IfEverythingIsOk() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        processingService.findById(1L);

        verify(processingRepository).findById(1L);
    }

    @Test
    void addProcessingForOrder_ShouldReturnFalseIfOrdersIsNull() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        boolean isSuccess = processingService.addProcessingForOrder(null);

        assertThat(isSuccess, is(false));
    }

    @Test
    void addProcessingForOrder_ShouldReturnTrueIfOrdersIsNotNull() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Orders orders = new Orders(1L, "ss", new ArrayList<>());

        boolean isSuccess = processingService.addProcessingForOrder(orders);

        assertThat(isSuccess, is(true));
    }

    @Test
    void addProcessingForOrder_ShouldCall_Save_MethodIfEverythingIsOk() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Orders orders = new Orders(0L, "ss", new ArrayList<>());
        processingService.addProcessingForOrder(orders);

        Processing processing = new Processing(0L, State.IN_PROGRESS, orders);

        verify(processingRepository).save(processing);
    }

    @Test
    void changeStateOfProcessing_ShouldReturnFalseIfProcessingIdIsNull() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        boolean isSuccess = processingService.changeStateOfProcessing(null, State.IN_PROGRESS);

        assertThat(isSuccess, is(false));
    }

    @Test
    void changeStateOfProcessing_ShouldReturnFalseIfProcessingIdIsEqualZero() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        boolean isSuccess = processingService.changeStateOfProcessing(0L, State.IN_PROGRESS);

        assertThat(isSuccess, is(false));
    }

    @Test
    void changeStateOfProcessing_ShouldReturnFalseIfProcessingIdIsLowerThanZero() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        boolean isSuccess = processingService.changeStateOfProcessing(-1L, State.IN_PROGRESS);

        assertThat(isSuccess, is(false));
    }

    @Test
    void changeStateOfProcessing_ShouldReturnFalseIfRepositoryReturnEmptyValue() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Optional<Processing> optionalProcessing = Optional.empty();

        given(processingRepository.findById(1L)).willReturn(optionalProcessing);

        boolean isSuccess = processingService.changeStateOfProcessing(1L, State.IN_PROGRESS);

        assertThat(isSuccess, is(false));
    }

    @Test
    void changeStateOfProcessing_ShouldReturnFalseIfStateIsNull() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = new Processing(1L, State.REJECTED, new Orders());
        Optional<Processing> optionalProcessing = Optional.of(processing);

        given(processingRepository.findById(1L)).willReturn(optionalProcessing);

        boolean isSuccess = processingService.changeStateOfProcessing(1L, null);

        assertThat(isSuccess, is(false));
    }

    @Test
    void changeStateOfProcessing_ShouldReturnTrueIfEverythingIsOk() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = new Processing(1L, State.REJECTED, new Orders());
        Optional<Processing> optionalProcessing = Optional.of(processing);

        given(processingRepository.findById(1L)).willReturn(optionalProcessing);

        boolean isSuccess = processingService.changeStateOfProcessing(1L, State.REJECTED);

        assertThat(isSuccess, is(true));
    }

    @Test
    void changeStateOfProcessing_ShouldCall_Save_MethodIfEverythingIsOk() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = new Processing(1L, State.REJECTED, new Orders());
        Optional<Processing> optionalProcessing = Optional.of(processing);

        given(processingRepository.findById(1L)).willReturn(optionalProcessing);

        processingService.changeStateOfProcessing(1L, State.REJECTED);

        verify(processingRepository).save(processing);
    }

    @Test
    void deleteProcessing_ShouldReturnFalseIfProcessingIdIsNull() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        boolean isSuccess = processingService.deleteProcessing(null);

        assertThat(isSuccess, is(false));
    }

    @Test
    void deleteProcessing_ShouldReturnFalseIfProcessingIdIsEqualZero() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        boolean isSuccess = processingService.deleteProcessing(0L);

        assertThat(isSuccess, is(false));
    }

    @Test
    void deleteProcessing_ShouldReturnFalseIfProcessingIdIsLowerThanZero() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        boolean isSuccess = processingService.deleteProcessing(-1L);

        assertThat(isSuccess, is(false));
    }

    @Test
    void deleteProcessing_ShouldReturnFalseIfRepositoryReturnEmptyValue() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        given(processingRepository.findById(1L)).willReturn(Optional.empty());

        boolean isSuccess = processingService.deleteProcessing(1L);

        assertThat(isSuccess, is(false));
    }

    @Test
    void deleteProcessing_ShouldReturnTrueIfRepositoryReturnValue() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = new Processing(1L, State.REJECTED, new Orders());

        given(processingRepository.findById(1L)).willReturn(Optional.of(processing));

        boolean isSuccess = processingService.deleteProcessing(1L);

        assertThat(isSuccess, is(true));
    }

    @Test
    void deleteProcessing_ShouldCall_Delete_MethodIfEverythingIsOk() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = new Processing(1L, State.REJECTED, new Orders());
        given(processingRepository.findById(1L)).willReturn(Optional.of(processing));

        processingService.deleteProcessing(1L);

        verify(processingRepository).delete(processing);
    }

    @Test
    void findByOrdersId_ShouldReturnNullIfOrdersIdIsNull() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = processingService.findByOrdersId(null);

        assertThat(processing, equalTo(null));
    }

    @Test
    void findByOrdersId_ShouldReturnNullIfOrdersIdIsEqualZero() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = processingService.findByOrdersId(0L);

        assertThat(processing, equalTo(null));
    }

    @Test
    void findByOrdersId_ShouldReturnNullIfOrdersIdIsLowerThanZero() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing processing = processingService.findByOrdersId(-1L);

        assertThat(processing, equalTo(null));
    }

    @Test
    void findByOrdersId_ShouldReturnProcessingReceivedFromRepository() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        Processing givenProcessing = new Processing(1L, State.REJECTED, new Orders());

        given(processingRepository.findByOrders_Id(1L)).willReturn(givenProcessing);

        Processing processing = processingService.findByOrdersId(1L);

        assertThat(processing, is(givenProcessing));
    }

    @Test
    void findByOrdersId_ShouldCall_FindByOrdersId_IfEverythingIsOk() {
        ProcessingRepository processingRepository = mock(ProcessingRepository.class);
        ProcessingService processingService = new ProcessingServiceImpl(processingRepository);

        processingService.findByOrdersId(1L);

        verify(processingRepository).findByOrders_Id(1L);
    }

    private List<Processing> prepareData() {

        Processing processing1 = new Processing(1L, State.REJECTED, new Orders());
        Processing processing2 = new Processing(2L, State.READY, new Orders());
        Processing processing3 = new Processing(3L, State.IN_PROGRESS, new Orders());
        Processing processing4 = new Processing(4L, State.DELIVERED, new Orders());
        Processing processing5 = new Processing(5L, State.REJECTED, new Orders());

        List<Processing> processingList = List.of(processing1,
                processing2, processing3, processing4, processing5);

        return processingList;
    }

}
