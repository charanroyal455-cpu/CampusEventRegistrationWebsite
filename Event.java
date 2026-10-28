package com.campus.events;

public class Event {
    private int id;
    private String name;
    private String date;
    private String venue;
    private String description;
    private int capacity;
    private int registeredCount;

    public Event() {}

    public Event(int id, String name, String date, String venue,
                 String description, int capacity) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.description = description;
        this.capacity = capacity;
        this.registeredCount = 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getVenue() { return venue; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public int getRegisteredCount() { return registeredCount; }
    public int getAvailableSeats() { return capacity - registeredCount; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDate(String date) { this.date = date; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setDescription(String description) { this.description = description; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setRegisteredCount(int registeredCount) {
        this.registeredCount = registeredCount;
    }
}
