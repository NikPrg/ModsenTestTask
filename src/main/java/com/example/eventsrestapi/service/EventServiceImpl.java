package com.example.eventsrestapi.service;

import com.example.eventsrestapi.dao.EventDao;
import com.example.eventsrestapi.dto.EventDto;
import com.example.eventsrestapi.exception.EventAlreadyTakenException;
import com.example.eventsrestapi.exception.EventNotExistException;
import com.example.eventsrestapi.exception.EventsNotFoundException;
import com.example.eventsrestapi.model.Event;
import com.example.eventsrestapi.model.SortType;
import com.example.eventsrestapi.valid.EventValidatorImpl;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.eventsrestapi.model.SortType.*;

@Service
@Transactional(readOnly = true)
@Validated
public class EventServiceImpl implements EventService {
    private static final String EVENT_NOT_EXIST_EXCEPTION_MESSAGE = "Event not found by given id = %s";
    private static final String EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE = "Event already exist by given place = %s and time = %s";

    private final EventDao eventDao;
    private final EventValidatorImpl eventValidatorImpl;
    private final ModelMapper modelMapper;

    public EventServiceImpl(EventDao eventDao, EventValidatorImpl eventValidatorImpl, ModelMapper modelMapper) {
        this.eventDao = eventDao;
        this.eventValidatorImpl = eventValidatorImpl;
        this.modelMapper = modelMapper;
    }

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


    @Transactional
    @Override
    public void register(@Valid EventDto eventDto, BindingResult bindingResult) {
        /*1 варик
        eventValidatorImpl.validate(eventDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new EventAlreadyTakenException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
         */

        //2 варик
        if(eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime()).isPresent())
            throw new EventAlreadyTakenException(EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE.formatted(eventDto.getPlace(), eventDto.getEventTime()));


        eventDao.save(convertToEvent(eventDto));
    }


    @Transactional
    @Override
    public void update(@Valid EventDto eventDto, long id) {
        //1 варик
        eventDao.findById(id)
                        .orElseThrow(() -> new EventNotExistException(EVENT_NOT_EXIST_EXCEPTION_MESSAGE.formatted(id)));

        if(eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime()).isPresent())
            throw new EventAlreadyTakenException(EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE.formatted(eventDto.getPlace(),eventDto.getEventTime()));

        eventDao.update(convertToEvent(eventDto), id);
    }

    @Transactional
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
