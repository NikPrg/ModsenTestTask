package com.example.eventsrestapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "events")
@org.hibernate.annotations.NamedNativeQueries({
        @org.hibernate.annotations.NamedNativeQuery(name = "SORT_BY_ONE_PARAMETER", query = "SELECT * FROM events ORDER BY ?", resultClass = Event.class),
        @org.hibernate.annotations.NamedNativeQuery(name = "SORT_BY_TWO_PARAMETERS", query = "SELECT * FROM events ORDER BY ?, ?", resultClass = Event.class),
        @org.hibernate.annotations.NamedNativeQuery(name = "SORT_BY_THREE_PARAMETERS", query = "SELECT * FROM events ORDER BY ?, ?, ?", resultClass = Event.class)
})
public class Event {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description")
    private String description;

    @Column(name = "organizer")
    private String organizer;

    @Column(name = "place")
    private String place;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
