async function getResponderAvailability(){
    try{
        const response = await fetch('http://localhost:8080/api/responder-availability')
        if(!response.ok){
            const errorData = await response.json().catch(() => null);
                throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error){
        console.error('Error fetching Responder Availability:', error);
            throw error;
        }
}

// The ID here belongs to the availability record, not the responder.
async function getResponderAvailabilityById(availabilityId) {
    try {
        const response = await fetch(`http://localhost:8080/api/responder-availability/${availabilityId}`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching responder availability:', error);
        throw error;
    }
}

// Creation identifies the responder in the URL; dates and timezone go in the body.
async function createResponderAvailability(availabilityData) {
    try {
        const response = await fetch(`http://localhost:8080/api/responder-availability/responder/${availabilityData.responderId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                availableFrom: availabilityData.availableFrom,
                availableUntil: availabilityData.availableUntil,
                timeZone: availabilityData.timeZone,
            }),
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error creating responder availability:', error);
        throw error;
    }
}

// PUT requires both dates, timezone, and status in availabilityData.
async function updateResponderAvailability(availabilityId, availabilityData) {
    try {
        const response = await fetch(`http://localhost:8080/api/responder-availability/${availabilityId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                availableFrom: availabilityData.availableFrom,
                availableUntil: availabilityData.availableUntil,
                timeZone: availabilityData.timeZone,
                status: availabilityData.status,
            }),
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error updating responder availability:', error);
        throw error;
    }
}

export {
    getResponderAvailability,
    getResponderAvailabilityById,
    createResponderAvailability,
    updateResponderAvailability,
};
