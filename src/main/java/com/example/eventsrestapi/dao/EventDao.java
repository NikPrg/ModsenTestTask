package com.example.eventsrestapi.dao;

import com.example.eventsrestapi.model.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
public class EventDao {
    private static final String SQL_FIND_BY_PLACE_AND_EVENT_TIME = "SELECT e FROM Event e WHERE e.place = :place AND e.eventTime = :eventTime";
    private static final String SQL_SORT_BY_SUBJECT = "SELECT e FROM Event e ORDER BY e.subject";
    private static final String SQL_SORT_BY_ORGANIZER = "SELECT e FROM Event e ORDER BY e.organizer";
    private static final String SQL_SORT_BY_TIME = "SELECT e FROM Event e ORDER BY e.eventTime";

    private final SessionFactory sessionFactory;


    public EventDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Event> findAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("SELECT e FROM Event e", Event.class).getResultList();
    }

    public List<Event> findAll(List<String> list){
        List<Event> eventList;
        Session currentSession = sessionFactory.getCurrentSession();
        if("subject".equals(list.get(0))) eventList = currentSession.createQuery(SQL_SORT_BY_SUBJECT, Event.class).getResultList();
        else if ("organizer".equals(list.get(0))) eventList = currentSession.createQuery(SQL_SORT_BY_ORGANIZER, Event.class).getResultList();
        else eventList = currentSession.createQuery(SQL_SORT_BY_TIME, Event.class).getResultList();

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
        updatedEvent.setId(id);   //т.к merge использует id нашего объекта для поиска сначала в Persistence Context, если там нету, то идёт искать в бд.
        currentSession.merge(updatedEvent);
//        Event eventToBeUpdated = currentSession.get(Event.class, id);

//        eventToBeUpdated.setSubject(updatedEvent.getSubject());
//        eventToBeUpdated.setDescription(updatedEvent.getDescription());
//        eventToBeUpdated.setOrganizer(updatedEvent.getOrganizer());
//        eventToBeUpdated.setPlace(updatedEvent.getPlace());
//        eventToBeUpdated.setEventTime(updatedEvent.getEventTime());

    }


    public void delete(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(currentSession.get(Event.class, id));
    }


}
