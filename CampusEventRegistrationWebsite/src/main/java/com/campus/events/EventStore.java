package com.campus.events;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EventStore {
    private final Map<Integer, Event> events = new LinkedHashMap<>();
    private final List<Registration> registrations = new ArrayList<>();
    private final AtomicInteger nextEventId = new AtomicInteger(1);
    private final AtomicInteger nextRegistrationId = new AtomicInteger(1);
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public EventStore() {
        addInitialEvent("AI & ML Workshop", "2027-01-15", "Computer Lab 1",
                "Explore practical applications of artificial intelligence and machine learning.", 60);
        addInitialEvent("Coding Competition", "2027-01-20", "Main Auditorium",
                "Solve programming challenges and test your coding skills.", 100);
        addInitialEvent("Cultural Fest", "2027-02-05", "College Ground",
                "Celebrate music, dance, art, and campus life.", 200);
    }

    private void addInitialEvent(String name, String date, String venue,
                                 String description, int capacity) {
        int id = nextEventId.getAndIncrement();
        events.put(id, new Event(id, name, date, venue, description, capacity));
    }

    public synchronized List<Event> getEvents() {
        return new ArrayList<>(events.values());
    }

    public synchronized Event addEvent(String name, String date, String venue,
                                       String description, int capacity) {
        if (name == null || name.isBlank() || date == null || date.isBlank()
                || venue == null || venue.isBlank()) {
            throw new IllegalArgumentException("Event name, date, and venue are required.");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1.");
        }
        int id = nextEventId.getAndIncrement();
        Event event = new Event(id, name.trim(), date, venue.trim(),
                description == null ? "" : description.trim(), capacity);
        events.put(id, event);
        return event;
    }

    public synchronized Registration register(int eventId, String studentName,
                                               String registerNo, String email) {
        if (studentName == null || studentName.isBlank()
                || registerNo == null || registerNo.isBlank()
                || email == null || email.isBlank()) {
            throw new IllegalArgumentException("Please fill in all student details.");
        }

        Event event = events.get(eventId);
        if (event == null) {
            throw new IllegalArgumentException("This event could not be found.");
        }

        String cleanRegisterNo = registerNo.trim();
        for (Registration r : registrations) {
            if (r.getEventId() == eventId
                    && r.getRegisterNo().equalsIgnoreCase(cleanRegisterNo)) {
                throw new IllegalArgumentException(
                        "This student is already registered for this event.");
            }
        }

        if (event.getRegisteredCount() >= event.getCapacity()) {
            throw new IllegalArgumentException("Sorry, this event is full.");
        }

        event.setRegisteredCount(event.getRegisteredCount() + 1);
        return new Registration(
                nextRegistrationId.getAndIncrement(),
                eventId,
                event.getName(),
                studentName.trim(),
                cleanRegisterNo,
                email.trim(),
                LocalDateTime.now().format(timeFormat)
        );
    }

    public synchronized Registration saveRegistration(int eventId, String studentName,
                                                       String registerNo, String email) {
        Registration registration = register(eventId, studentName, registerNo, email);
        registrations.add(registration);
        return registration;
    }

    public synchronized List<Registration> getRegistrations() {
        return new ArrayList<>(registrations);
    }
}
