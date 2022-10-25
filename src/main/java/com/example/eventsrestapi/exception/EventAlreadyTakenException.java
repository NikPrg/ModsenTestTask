package com.example.eventsrestapi.exception;

public class EventAlreadyTakenException extends EventException {
    public EventAlreadyTakenException(String message) {
        super(message);
    }
}
