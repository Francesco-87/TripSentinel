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

export {getResponderAvailability}