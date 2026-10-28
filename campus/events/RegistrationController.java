package com.campus.events;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final EventStore store;

    public RegistrationController(EventStore store) {
        this.store = store;
    }

    @GetMapping
    public List<Registration> getRegistrations() {
        return store.getRegistrations();
    }

    @PostMapping
    public Registration register(@RequestBody RegistrationRequest request) {
        try {
            return store.saveRegistration(request.eventId(), request.studentName(),
                    request.registerNo(), request.email());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public record RegistrationRequest(int eventId, String studentName,
                                      String registerNo, String email) {}
}
