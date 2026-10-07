import { useState } from 'react';
import { useOutletContext } from 'react-router-dom';
import { adminCreateUser, adminUpdateUser } from '../../services/userService.js';
import UserForm from './UserForm.jsx';
import Modal from '../layout/Modal.jsx';

function AdminUsers() {
    // The parent owns users so table updates also refresh the overview count.
    const { users, fetchUsers, loading, error } = useOutletContext();
    const [selectedUser, setSelectedUser] = useState(null);
    const [statusError, setStatusError] = useState(null);
    const [userSearchTerm, setUserSearchTerm] = useState("");

     // Create a new user and refresh the list
    async function handleUserCreate(userData) {
        await adminCreateUser(userData)
        await fetchUsers()

    }

     // Update an existing user and close the edit modal
    async function handleUserUpdate(userData) {
        await adminUpdateUser(selectedUser.id, userData)
        await fetchUsers()
        setSelectedUser(null)
    }

   async function handleStatusChange(userData) {
        setStatusError(null);
        const userStatus = userData.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";

        try {
            await adminUpdateUser(userData.id, { status: userStatus });
            await fetchUsers();
        } catch (error) {
            setStatusError(error.message || "Unable to change user status.");
        }
    }
    // Filter the complete list locally; searching never replaces users state.
    function searchUser(userSearchTerm) {
        const searchTerm = userSearchTerm.trim().toLowerCase();

        if (!searchTerm) return users;

        return users.filter(user => {
            const fullName = `${user.firstName} ${user.lastName}`.toLowerCase();

            return fullName.includes(searchTerm) ||
                user.email.toLowerCase().includes(searchTerm) ||
                user.id.toString() === searchTerm;
        });
    }

    // Recalculated on each render when the search text or fetched users change.
    const filteredUsers = searchUser(userSearchTerm);

    return (
    <section className="admin-dashboard__section">
        <h2>Users</h2>
        <div>
            <UserForm onSubmit={handleUserCreate} title="Create new user" />
        </div>
        <div>
            <input type="search"
            placeholder="Search users..."
            aria-label="Search users"
            value={userSearchTerm}
            onChange={(e) => setUserSearchTerm(e.target.value)} />
        </div>
        {loading && <p className="admin-dashboard__message" role="status">Loading...</p>}
        {error && <p className="admin-dashboard__error" role="alert">Error: {error.message}</p>}
         {statusError && (
                <p className="admin-dashboard__error" role="alert">
                    {statusError}
                </p>
            )}
        {!loading && !error && (

            <div className="admin-dashboard__table-scroll" role="region" aria-label="Users table" tabIndex={0}>
            <table>
                <thead>
                    <tr>
                        <th scope="col">User ID</th>
                        <th scope="col">First Name</th>
                        <th scope="col">Last Name</th>
                        <th scope="col">Email</th>
                        <th scope="col">Phone</th>
                        <th scope="col">Roles</th>
                        <th scope="col">Status</th>
                        <th scope="col">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredUsers.length === 0 && (
                        <tr>
                            <td colSpan={8}>No users found.</td>
                        </tr>
                    )}
                    {filteredUsers.map(user => (
                        <tr key={user.id}>
                            <td>{user.id}</td>
                            <td>{user.firstName}</td>
                            <td>{user.lastName}</td>
                            <td>{user.email}</td>
                            <td>{user.phoneNumber}</td>
                            <td>{user.roles.join(', ')}</td>
                            <td>{user.status}</td>
                            <td>
                                <button
                                type="button"

                                onClick={() => setSelectedUser(user)}
                                >Edit</button>


                                <button
                                type="button"
                                onClick={() => handleStatusChange(user)}
                                >
                                    {user.status === "ACTIVE" ? "Deactivate" : "Activate"}
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            </div>
        )}
           {/* Edit user modal */}
            {selectedUser && (
                <Modal title="Edit User" onClose={() => setSelectedUser(null)}>
                    <div

                    onClick={(e) => e.stopPropagation()}
                >
                  {/* Reuse the same form component for user updates */}
                    <UserForm
                    key={selectedUser.id}  // Ensure the form resets when a different user is selected
                    onSubmit={handleUserUpdate}
                    initialData={selectedUser}
                    submitLabel="Update User"
                    title={null}
                    />
                </div>
                </Modal>
            )}
    </section>

    );
}

export default AdminUsers;
