package com.example.eventsrestapi.api.v1.controller;

import com.example.eventsrestapi.dto.EventDto;
import com.example.eventsrestapi.dto.EventsResponseDto;
import com.example.eventsrestapi.model.Event;
import com.example.eventsrestapi.model.SortType;
import com.example.eventsrestapi.service.EventService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/v1/events")
public class EventController {
    private final EventService eventService;
    //  @PageableDefault
    @GetMapping
    public EventsResponseDto getEvents(
            @RequestParam(value = "sort_by_subject", required = false) boolean sortBySubject,
            @RequestParam(value = "sort_by_organizer", required = false) boolean sortByOrganizer,
            @RequestParam(value = "sort_by_event_time", required = false) boolean sortByEventTime) {
        List<EventDto> list;
        if (sortBySubject)
            list = eventService.findAll(SortType.SUBJECT);
        else if (sortByOrganizer)
            list = eventService.findAll(SortType.ORGANIZER);
        else if (sortByEventTime)
            list = eventService.findAll(SortType.TIME);
        else
            list = eventService.findAll();

        return new EventsResponseDto(list);
    }
    //log
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getEventById(@PathVariable("id") long id) {
        return eventService.findById(id);
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerEvent(@RequestBody EventDto eventDto) {
        eventService.register(eventDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("{id}")
    public ResponseEntity<HttpStatus> updateEvent(
            @PathVariable("id") long id,
            @RequestBody EventDto eventDto) {
        eventService.update(eventDto, id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") long id) {
        eventService.delete(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }


}
