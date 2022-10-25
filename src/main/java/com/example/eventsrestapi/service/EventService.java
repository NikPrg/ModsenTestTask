package com.example.eventsrestapi.service;

import com.example.eventsrestapi.dto.EventDto;

import javax.validation.Valid;
import java.util.List;

public interface EventService {

    List<EventDto> findAll();

    List<EventDto> findAll(List<String> list);

    EventDto findById(long id);

    EventDto register(@Valid EventDto event);

    void update(@Valid EventDto eventDto, long id);

    void delete(long id);

}
