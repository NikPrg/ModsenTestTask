package com.example.eventsrestapi.exception;

public class EventNotExistException extends EventException{

    public EventNotExistException(String message) {
        super(message);
    }
}
