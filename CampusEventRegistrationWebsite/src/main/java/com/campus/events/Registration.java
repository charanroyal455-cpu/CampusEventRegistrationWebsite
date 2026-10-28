package com.campus.events;

public class Registration {
    private int id;
    private int eventId;
    private String eventName;
    private String studentName;
    private String registerNo;
    private String email;
    private String registeredAt;

    public Registration() {}

    public Registration(int id, int eventId, String eventName, String studentName,
                        String registerNo, String email, String registeredAt) {
        this.id = id;
        this.eventId = eventId;
        this.eventName = eventName;
        this.studentName = studentName;
        this.registerNo = registerNo;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    public int getId() { return id; }
    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public String getStudentName() { return studentName; }
    public String getRegisterNo() { return registerNo; }
    public String getEmail() { return email; }
    public String getRegisteredAt() { return registeredAt; }
}
