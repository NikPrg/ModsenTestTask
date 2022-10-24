package com.example.eventsrestapi.service;

import com.example.eventsrestapi.dto.EventDto;
import com.example.eventsrestapi.model.Event;
import com.example.eventsrestapi.model.SortType;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {

    List<EventDto> findAll();

    List<EventDto> findAll(SortType sortType);

    EventDto findById(long id);

    void register(@Valid EventDto event);

    void update(@Valid EventDto eventDto, long id);

    void delete(long id);


}
