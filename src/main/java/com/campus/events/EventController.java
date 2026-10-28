package com.campus.events;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventStore store;

    public EventController(EventStore store) {
        this.store = store;
    }

    @GetMapping
    public List<Event> getEvents() {
        return store.getEvents();
    }

    @PostMapping
    public Event addEvent(@RequestBody EventRequest request) {
        try {
            return store.addEvent(request.name(), request.date(), request.venue(),
                    request.description(), request.capacity());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public record EventRequest(String name, String date, String venue,
                               String description, int capacity) {}
}
