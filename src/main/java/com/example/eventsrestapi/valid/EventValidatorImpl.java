package com.example.eventsrestapi.valid;

import com.example.eventsrestapi.dao.EventDao;
import com.example.eventsrestapi.dto.EventDto;
import com.example.eventsrestapi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EventValidatorImpl implements Validator {
    private final EventDao eventDao;

    public EventValidatorImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return EventDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventDto eventDto = (EventDto) target;
        if(eventDao.findByPlaceAndTime(eventDto.getPlace(), eventDto.getEventTime()).isPresent())
            errors.rejectValue("place", "", "This time and place is taken by other organization");
    }
}
