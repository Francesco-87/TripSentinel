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

export { getCheckInSessions };
