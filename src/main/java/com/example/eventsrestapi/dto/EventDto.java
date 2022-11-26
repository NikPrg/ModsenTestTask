package com.example.eventsrestapi.dto;

import lombok.*;


import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDto {

    @NotBlank(message = "This field should not be empty!")
    @Size(max = 50, message = "The size should be less than 50 characters")
    private String subject;

    @NotBlank(message = "This field should not be empty!")
    private String description;

    @NotBlank(message = "This field should not be empty!")
    @Size(max = 30, message = "The size should be less than 30 characters")
    private String organizer;

    @NotBlank(message = "This field should not be empty!")
    @Size(max = 30, message = "The size should be less than 30 characters")
    private String place;

    private LocalDateTime eventTime;
}
