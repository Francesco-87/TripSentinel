async function getUsers() {
    try {
        const response = await fetch('http://localhost:8080/api/users');
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        
        const users = await response.json();
        return users;
    } catch (error) {
        console.error('Error fetching users:', error);
        throw error;
    }
}

async function getUserById(userId) {
    try {
        const response = await fetch(`http://localhost:8080/api/users/${userId}`);
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        const user = await response.json();
        return user;
    } catch (error) {
        console.error('Error fetching user:', error);
        throw error;
    }
}

async function adminCreateUser(userData) {
    try {
        const response = await fetch('http://localhost:8080/api/users/admin/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        const user = await response.json();
        return user;
    } catch (error) {
        console.error('Error creating user:', error);
        throw error;
    }
}

async function adminUpdateUser(userId, userData) {
    try {
        const response = await fetch(`http://localhost:8080/api/users/admin/${userId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
        }
        const user = await response.json();
        return user;
    } catch (error) {
        console.error('Error updating user:', error);
        throw error;
    }
}

export { getUsers, adminCreateUser, adminUpdateUser, getUserById };
