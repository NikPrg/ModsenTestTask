package com.example.eventsrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EventsResponseDto {
    private List<EventDto> eventDtoList;
}
