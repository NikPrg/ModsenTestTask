package com.example.eventsrestapi.service;

import com.example.eventsrestapi.dao.EventDao;
import com.example.eventsrestapi.dto.EventDto;
import com.example.eventsrestapi.exception.EventAlreadyTakenException;
import com.example.eventsrestapi.exception.EventNotExistException;
import com.example.eventsrestapi.exception.EventsNotFoundException;
import com.example.eventsrestapi.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_NOT_EXIST_EXCEPTION_MESSAGE = "Event not found by given id = %s";
    private static final String EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE = "Event already exist by given place = %s and time = %s";

    private final EventDao eventDao;
    private final ModelMapper modelMapper;

    @Override
    public List<EventDto> findAll() {
        log.info("Getting all events without any parameters");
        List<Event> eventList = eventDao.findAll();
        if (eventList.isEmpty())
            throw new EventsNotFoundException("No events have been found");

        return eventList.stream().map(this::convertToEventDto).collect(Collectors.toList());
    }

    @Override
    public List<EventDto> findAll(List<String> list) {
        log.info("Getting all events sorting by: {}", list.toString());
        List<Event> eventList = eventDao.findAll(list);
        if (eventList.isEmpty())
            throw new EventsNotFoundException("No events have been found");

        return eventList.stream().map(this::convertObjectToEventDto).collect(Collectors.toList());
    }

    @Override
    public EventDto findById(long id) {
        log.info("Trying to find event by given id: {}", id);
        return eventDao.findById(id).map(this::convertToEventDto)
                .orElseThrow(() ->
                        new EventNotExistException(EVENT_NOT_EXIST_EXCEPTION_MESSAGE.formatted(id)));
    }

    @Override
    public EventDto register(@Valid EventDto eventDto) {
        log.info("Trying to register the event: {}", eventDto.toString());
        if (eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime()).isPresent())
            throw new EventAlreadyTakenException(EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE.formatted(eventDto.getPlace(), eventDto.getEventTime()));
        eventDao.save(convertToEvent(eventDto));
        return eventDto;
    }

    @Override
    public void update(@Valid EventDto eventDto, long id) {
        log.info("Trying to update the event by given id: {}", id);
        if (eventDao.findById(id).isEmpty())
            throw new EventNotExistException(EVENT_NOT_EXIST_EXCEPTION_MESSAGE.formatted(id));

        if (eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime()).isPresent())
            throw new EventAlreadyTakenException(EVENT_ALREADY_EXIST_EXCEPTION_MESSAGE.formatted(eventDto.getPlace(), eventDto.getEventTime()));

        eventDao.update(convertToEvent(eventDto), id);
    }

    @Override
    public void delete(long id) {
        log.info("Trying to delete event by given id: {}", id);
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
    private EventDto convertObjectToEventDto(Object o) {
        return modelMapper.map(o, EventDto.class);
    }

}
