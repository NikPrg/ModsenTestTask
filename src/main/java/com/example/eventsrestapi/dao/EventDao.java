package com.example.eventsrestapi.dao;

import com.example.eventsrestapi.exception.EventNotExistException;
import com.example.eventsrestapi.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDao {
    private static final String EVENT_NOT_EXIST_EXCEPTION_MESSAGE = "Event not found by given id = %s";

    private static final String SQL_FIND_BY_PLACE_AND_EVENT_TIME = "SELECT e FROM Event e WHERE e.place = :place AND e.eventTime = :eventTime";

    private final SessionFactory sessionFactory;


    public List<Event> findAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("SELECT e FROM Event e", Event.class).getResultList();
    }

    public List<Event> findAll(List<String> list) {
        Session currentSession = sessionFactory.openSession();

        List<Event> eventList;
        if (list.size() == 1) {
            eventList = currentSession.createNamedQuery("SORT_BY_ONE_PARAMETER", Event.class)
                    .setParameter(1, list.get(0)).getResultList();
        } else if (list.size() == 2) {
            eventList = currentSession.createNamedQuery("SORT_BY_TWO_PARAMETERS", Event.class)
                    .setParameter(1, list.get(0))
                    .setParameter(2, list.get(1)).getResultList();
        } else {
            eventList = currentSession.createNamedQuery("SORT_BY_THREE_PARAMETERS", Event.class)
                    .setParameter(1, list.get(0))
                    .setParameter(2, list.get(1))
                    .setParameter(3, list.get(2)).getResultList();
        }

        return eventList;
    }

    public Optional<Event> findById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.byId(Event.class).loadOptional(id);
    }

    public Optional<Event> findByPlaceAndTime(String place, LocalDateTime eventTime) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery(SQL_FIND_BY_PLACE_AND_EVENT_TIME, Event.class)
                .setParameter("place", place)
                .setParameter("eventTime", eventTime)
                .getResultStream().findFirst();
    }

    public void save(Event event) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(event);
    }

    public void update(Event updatedEvent, long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        updatedEvent.setId(id);
        currentSession.merge(updatedEvent);
    }

    public void delete(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        try {
            if (findById(id).isEmpty()) {
                throw new EventNotExistException(EVENT_NOT_EXIST_EXCEPTION_MESSAGE.formatted(id));
            }
            currentSession.delete(currentSession.get(Event.class, id));
        } catch (EventNotExistException e) {
            log.info("Failed to delete event by id: {}", id);
            throw new EventNotExistException(EVENT_NOT_EXIST_EXCEPTION_MESSAGE.formatted(id));
        }
    }
}

