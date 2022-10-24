package com.example.eventsrestapi.service;

import com.example.eventsrestapi.dao.EventDao;
import com.example.eventsrestapi.dto.EventDto;
import com.example.eventsrestapi.exception.EventAlreadyTakenException;
import com.example.eventsrestapi.exception.EventNotExistException;
import com.example.eventsrestapi.exception.EventsNotFoundException;
import com.example.eventsrestapi.model.Event;
import com.example.eventsrestapi.model.SortType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_NOT_EXIST_EXCEPTION_MESSAGE = "Event not found by given id = %s";
    private static final String EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE = "Event already exist by given place = %s and time = %s";

    private final EventDao eventDao;
    private final ModelMapper modelMapper;

    @Override
    public List<EventDto> findAll() {
        List<Event> eventList = eventDao.findAll();
        if (eventDao.findAll().isEmpty())
            throw new EventsNotFoundException("No events have been found");

        return eventList.stream().map(this::convertToEventDto).collect(Collectors.toList());
    }

    @Override
    public List<EventDto> findAll(SortType sortType) {
        List<Event> list = new ArrayList<>();
        switch (sortType){
            case SUBJECT -> list = eventDao.findAll(Collections.singletonList("subject"));
            case ORGANIZER -> list = eventDao.findAll(Collections.singletonList("organizer"));
            case TIME -> list = eventDao.findAll(Collections.singletonList("event_time"));
        }
        if (list.isEmpty())
            throw new EventsNotFoundException("No events have been found");

        return list.stream().map(this::convertToEventDto).collect(Collectors.toList());

    }

    @Override
    public EventDto findById(long id) {
        return eventDao.findById(id).map(this::convertToEventDto)
                .orElseThrow(() ->
                        new EventNotExistException(EVENT_NOT_EXIST_EXCEPTION_MESSAGE.formatted(id)));
    }



    @Override
    public void register(@Valid EventDto eventDto) {

        if(eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime()).isPresent())
            throw new EventAlreadyTakenException(EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE.formatted(eventDto.getPlace(), eventDto.getEventTime()));


        eventDao.save(convertToEvent(eventDto));
    }



    @Override
    public void update(@Valid EventDto eventDto, long id) {
        //1 варик
        Event event = eventDao.findById(id)
                .orElseThrow(() -> new EventNotExistException(EVENT_NOT_EXIST_EXCEPTION_MESSAGE.formatted(id)));
        //comparing
        if(eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime()).isPresent())
            throw new EventAlreadyTakenException(EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE.formatted(eventDto.getPlace(),eventDto.getEventTime()));

        eventDao.update(convertToEvent(eventDto), id);
    }


    @Override
    public void delete(long id) {
        eventDao.findById(id)
                .orElseThrow(() -> new EventNotExistException(EVENT_NOT_EXIST_EXCEPTION_MESSAGE.formatted(id)));
        eventDao.delete(id);
    }

    private EventDto convertToEventDto(Event event) {
        return modelMapper.map(event, EventDto.class);
    }

    private Event convertToEvent(EventDto eventDto) {
        Event mappedEvent = modelMapper.map(eventDto, Event.class);
        mappedEvent.setCreatedAt(LocalDateTime.now());
        return mappedEvent;
    }


}
