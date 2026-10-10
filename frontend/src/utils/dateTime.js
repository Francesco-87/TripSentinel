// Display backend UTC timestamps in the specified timezone, not the browser's timezone.
export function formatDateTime(timestamp, timeZone) {
    if (!timestamp) return "—";

    return new Intl.DateTimeFormat("en-GB", {
        timeZone,
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hourCycle: "h23",
    }).format(new Date(timestamp));
}

// Format a timestamp for a datetime-local input in the specified timezone.
export function toLocalDateTimeInput(timestamp, timeZone) {
    if (!timestamp) return "";

    const formatter = new Intl.DateTimeFormat("en-GB", {
        timeZone,
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hourCycle: "h23",
    });

    const parts = formatter.formatToParts(new Date(timestamp));

    const getPart = (type) =>
        parts.find((part) => part.type === type).value;

    return `${getPart("year")}-${getPart("month")}-${getPart("day")}T${getPart("hour")}:${getPart("minute")}`;
}
