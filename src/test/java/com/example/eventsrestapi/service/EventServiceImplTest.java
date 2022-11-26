//package com.example.eventsrestapi.service;
//
//import com.example.eventsrestapi.dao.EventDao;
//import com.example.eventsrestapi.dto.EventDto;
//import com.example.eventsrestapi.exception.EventNotExistException;
//import com.example.eventsrestapi.exception.EventsNotFoundException;
//import com.example.eventsrestapi.model.Event;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.api.function.Executable;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class EventServiceImplTest {
//
//    @Mock
//    EventDao eventDao;
//
//    @Mock
//    ModelMapper modelMapper;
//
//    @InjectMocks
//    EventServiceImpl eventService;
//    private Event event = new Event();
//    private EventDto eventDto;
//
//
//    @BeforeEach
//    public void setUp() {
//        eventDto = EventDto.builder()
//                .id(1)
//                .subject("New Java features")
//                .description("I swear, it should be interesting")
//                .organizer("Apsent")
//                .place("Chizhovka Arena")
//                .eventTime(LocalDateTime.now())
//                .build();
//    }
//
//    @Test
//    void should_ReturnEventDtoList_When_EventListIsNotEmpty() {
//        //given
//        List<Event> list = new ArrayList<>();
//        Event eventOne = Event.builder()
//                .id(1)
//                .subject("New Java features")
//                .description("I swear, it should be interesting")
//                .organizer("Apsent")
//                .place("Chizhovka Arena")
//                .eventTime(LocalDateTime.now())
//                .createdAt(LocalDateTime.now().minusDays(2))
//                .build();
//
//        Event eventTwo = Event.builder()
//                .id(2)
//                .subject("New C# features")
//                .description("I swear, it should be NOT interesting")
//                .organizer("LQW")
//                .place("Chizhovka Arena")
//                .eventTime(LocalDateTime.now().plusDays(2))
//                .createdAt(LocalDateTime.now().minusDays(4))
//                .build();
//
//        list.add(eventOne);
//        list.add(eventTwo);
//        given(eventDao.findAll()).willReturn(list);
//
//
//        //when
//        List<EventDto> eventDtoList = eventService.findAll();
//
//
//        //then
//        assertEquals(2, eventDtoList.size());
//        verify(eventDao, times(1)).findAll();
//
//    }
//
//    @Test
//    void should_ThrowEventsNotFoundException_When_EventListIsEmpty(){
//        //given
//        List<Event> list = Collections.emptyList();
//        given(eventDao.findAll()).willReturn(list);
//
//        //when
//        Executable executable = () -> eventService.findAll();
//
//        //then
//        assertThrows(EventsNotFoundException.class, executable);
//        verify(eventDao, times(1)).findAll();
//    }
//
//    @Test
//    void should_ReturnEventDto_When_GivenIdIsPresent(){
//        //given
//        long givenId = 1;
//        given(eventDao.findById(givenId)).willReturn(Optional.ofNullable(event));
//        given(modelMapper.map(any(), any())).willReturn(eventDto);
//
//        //when
//        EventDto eventDto = eventService.findById(givenId);
//
//        //then
//        assertAll(
//                () -> assertEquals(givenId, eventDto.getId()),
//                () -> assertEquals("New Java features", eventDto.getSubject()),
//                () -> assertEquals("I swear, it should be interesting", eventDto.getDescription()),
//                () -> assertEquals("Apsent", eventDto.getOrganizer()),
//                () -> assertEquals("Chizhovka Arena", eventDto.getPlace())
//        );
//        verify(eventDao, times(1)).findById(givenId);
//
//    }
//
//    @Test
//    void should_ThrowEventNotExistException_When_GivenIdIsNotPresent(){
//        //given
//        long givenId = 2;
//        given(eventDao.findById(givenId)).willReturn(Optional.empty());
//
//        //when
//        Executable executable = () -> eventService.findById(givenId);
//
//        //then
//        assertThrows(EventNotExistException.class, executable);
//        verify(eventDao, times(1)).findById(givenId);
//    }
//
//    @Test
//    void TestRegisterEvent_ValidEventDto_ShouldReturnEventDto(){
//        //given
//        given(eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime())).willReturn(Optional.empty());
//        doNothing().when(eventDao).save(event);
//
//        Optional<Event> eventOptional = eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime());
//    }
//
//
//    @Test
//    void testFindAll() {
//    }
//
//    @Test
//    void findById() {
//
//    }
//
//    @Test
//    void register() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//
//    }
//
//}