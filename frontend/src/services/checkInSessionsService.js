async function getCheckInSessions() {
    try {
        const response = await fetch('http://localhost:8080/api/check-in-sessions');
            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
            }
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Error fetching check-in sessions:', error);
            throw error;
        }
    }

async function adminCreateCheckInSession(sessionData) {
    try {
        const response = await fetch('http://localhost:8080/api/check-in-sessions/create-admin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sessionData)
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error creating check-in session:', error);
        throw error;
    }
}

async function adminUpdateSession(sessionId, sessionData) {
    try {
        const response = await fetch(`http://localhost:8080/api/check-in-sessions/${sessionId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sessionData)
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error updating check-in session:', error);
        throw error;
    }
}

async function getCheckInMethods() {
    try {
        const response = await fetch('http://localhost:8080/api/check-in-methods');
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching check-in methods:', error);
        throw error;
    }
}

async function cancelSession(sessionId){
    try {
        const response = await fetch(`http://localhost:8080/api/check-in-sessions/${sessionId}/cancel`, {
            method: 'POST',
        });
        
     if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
       
    } catch (error) {
        console.error('Error Canceling session:', error);
        throw error;
    }
}
export { getCheckInSessions, adminCreateCheckInSession, adminUpdateSession, getCheckInMethods, cancelSession };
