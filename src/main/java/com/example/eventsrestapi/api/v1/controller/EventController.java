package com.example.eventsrestapi.api.v1.controller;

import com.example.eventsrestapi.dto.EventDto;
import com.example.eventsrestapi.dto.EventsResponseDto;
import com.example.eventsrestapi.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/v1/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public EventsResponseDto getEvents(
            @RequestParam(value = "sort_by_subject", required = false) boolean sortBySubject,
            @RequestParam(value = "sort_by_organizer", required = false) boolean sortByOrganizer,
            @RequestParam(value = "sort_by_event_time", required = false) boolean sortByEventTime) {
        log.info("Received a request to get all events");
        List<String> sortList = new ArrayList<>();
        List<EventDto> eventDtos;

        if (sortBySubject) sortList.add("subject");
        if (sortByOrganizer) sortList.add("organizer");
        if (sortByEventTime) sortList.add("eventTime");


        if (sortList.isEmpty()) eventDtos = eventService.findAll();
        else eventDtos = eventService.findAll(sortList);
        return new EventsResponseDto(eventDtos);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto  getEventById(@PathVariable("id") long id) {
        log.info("Received a request to get event by id {}", id);
        return eventService.findById(id);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto registerEvent(@RequestBody EventDto eventDto) {
        log.info("Received request to register an event: {}", eventDto);
        return eventService.register(eventDto);
    }


    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEvent(
            @PathVariable("id") long id,
            @RequestBody EventDto eventDto) {
        log.info("Received request to update an event: {} with id: {}", eventDto, id);
        eventService.update(eventDto, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable("id") long id) {
        log.info("Received a request to delete event by id {}", id);
        eventService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent() {
        log.info("Received a request to delete all events");
        eventService.delete();
    }

}
