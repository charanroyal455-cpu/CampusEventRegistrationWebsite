const eventList = document.getElementById("event-list");
const eventSelect = document.getElementById("eventId");
const registrationMessage = document.getElementById("registration-message");
const eventMessage = document.getElementById("event-message");

const eventEmojis = ["💡", "💻", "🎉", "🎤", "🤖", "🎨"];
const bannerClasses = ["purple", "mint", "peach"];

function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>"']/g, character => ({
        "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
    })[character]);
}

function showMessage(element, message, type) {
    element.textContent = message;
    element.className = "form-message " + type;
}

async function loadEvents() {
    eventList.innerHTML = '<p class="loading">Loading events…</p>';
    try {
        const response = await fetch("/api/events");
        if (!response.ok) throw new Error("Could not load events.");
        const events = await response.json();

        eventList.innerHTML = "";
        eventSelect.innerHTML = '<option value="">Select an event</option>';

        if (events.length === 0) {
            eventList.innerHTML = '<p class="loading">No events yet. Organizers can add one below.</p>';
            return;
        }

        events.forEach((event, index) => {
            const banner = bannerClasses[index % bannerClasses.length];
            const emoji = eventEmojis[index % eventEmojis.length];
            const available = event.availableSeats;
            const card = document.createElement("article");
            card.className = "event-card";
            card.innerHTML = `
                <div class="event-banner ${banner}">${emoji}</div>
                <div class="event-content">
                    <div class="event-date">${escapeHtml(event.date)}</div>
                    <h3>${escapeHtml(event.name)}</h3>
                    <p>${escapeHtml(event.description || "Join us for this campus event.")}</p>
                    <div class="event-meta"><span>📍 ${escapeHtml(event.venue)}</span></div>
                    <div class="event-bottom">
                        <span class="seats">${available} of ${event.capacity} seats left</span>
                        <button class="small-button" data-event-id="${event.id}" ${available <= 0 ? "disabled" : ""}>
                            ${available <= 0 ? "Full" : "Register"}
                        </button>
                    </div>
                </div>`;
            eventList.appendChild(card);

            const option = document.createElement("option");
            option.value = event.id;
            option.textContent = `${event.name} — ${available} seats left`;
            option.disabled = available <= 0;
            eventSelect.appendChild(option);
        });

        eventList.querySelectorAll("[data-event-id]").forEach(button => {
            button.addEventListener("click", () => {
                eventSelect.value = button.dataset.eventId;
                document.getElementById("register").scrollIntoView({ behavior: "smooth" });
            });
        });
    } catch (error) {
        eventList.innerHTML = '<p class="loading">Unable to load events. Make sure the Java server is running, then refresh.</p>';
    }
}

async function loadRegistrations() {
    const tbody = document.getElementById("registration-list");
    try {
        const response = await fetch("/api/registrations");
        if (!response.ok) throw new Error("Could not load registrations.");
        const registrations = await response.json();

        if (registrations.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="empty-cell">No registrations yet. Be the first!</td></tr>';
            return;
        }
        tbody.innerHTML = registrations.slice().reverse().map(reg => `
            <tr>
                <td>${escapeHtml(reg.studentName)}</td>
                <td>${escapeHtml(reg.registerNo)}</td>
                <td>${escapeHtml(reg.eventName)}</td>
                <td>${escapeHtml(reg.email)}</td>
                <td>${escapeHtml(reg.registeredAt)}</td>
            </tr>`).join("");
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-cell">Unable to load registrations.</td></tr>';
    }
}

document.getElementById("registration-form").addEventListener("submit", async event => {
    event.preventDefault();
    showMessage(registrationMessage, "Submitting registration…", "");

    const payload = {
        eventId: Number(eventSelect.value),
        studentName: document.getElementById("studentName").value.trim(),
        registerNo: document.getElementById("registerNo").value.trim(),
        email: document.getElementById("email").value.trim()
    };

    try {
        const response = await fetch("/api/registrations", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || result.detail || "Registration failed.");
        }

        showMessage(registrationMessage,
            `Success! ${result.studentName}, you are registered for ${result.eventName}.`,
            "success");
        event.target.reset();
        await loadEvents();
        await loadRegistrations();
    } catch (error) {
        showMessage(registrationMessage, error.message || "Could not submit registration.", "error");
    }
});

document.getElementById("event-form").addEventListener("submit", async event => {
    event.preventDefault();
    showMessage(eventMessage, "Adding event…", "");

    const payload = {
        name: document.getElementById("eventName").value.trim(),
        date: document.getElementById("eventDate").value,
        venue: document.getElementById("venue").value.trim(),
        capacity: Number(document.getElementById("capacity").value),
        description: document.getElementById("description").value.trim()
    };

    try {
        const response = await fetch("/api/events", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || result.detail || "Could not add event.");
        }

        showMessage(eventMessage, `Event "${result.name}" added successfully.`, "success");
        event.target.reset();
        document.getElementById("capacity").value = 50;
        await loadEvents();
    } catch (error) {
        showMessage(eventMessage, error.message || "Could not add event.", "error");
    }
});

document.getElementById("refresh-registrations").addEventListener("click", loadRegistrations);

loadEvents();
loadRegistrations();
