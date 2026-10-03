import {useState, useCallback} from 'react';
import {useEffect} from 'react';
import {
    getUsers,
    adminCreateUser, 
    adminUpdateUser,
} from '../../services/userService.js';
import {getCheckInSessions} from '../../services/checkInSessionsService.js';

import UserForm from '../users/UserForm.jsx';
import Modal from '../layout/Modal.jsx';
import '../../styles/AdminDashboard.css';


function AdminDashboard() {

    const [stats, setStats] = useState({
        activeUsers: 0,
        totalCheckInSessions: 0,
        plannedCheckInSessions: 0,
        activeCheckInSessions: 0,
        missedCheckInSessions: 0

    });
    const [users, setUsers] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


    const fetchUsers = useCallback(async () => {
           try {
                const users = await getUsers();
                setUsers(users);
                const activeUsers = users.filter(user => user.status === 'ACTIVE').length;
                setStats(prev => ({ ...prev, activeUsers }));
           } catch (error) {
                console.error('Error fetching users:', error);
                setError(error);
           }
    }, []);


    const fetchSessionStats = useCallback(async () => {
            try {               
                
                const checkInSessions = await getCheckInSessions();
                const plannedCheckInSessions = checkInSessions.filter(session => session.status === 'PLANNED').length;
                const activeCheckInSessions = checkInSessions.filter(session => session.status === 'ACTIVE').length;
                const missedCheckInSessions = checkInSessions.filter(session => session.status === 'MISSED').length;


                setStats(prev => ({ ...prev, totalCheckInSessions: checkInSessions.length, plannedCheckInSessions, activeCheckInSessions, missedCheckInSessions }));
            } catch (error) {
                console.error('Error fetching stats:', error);
                setError(error);
            } 
        }, []);
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

    const loadDashboard = useCallback(async () => {
        try{
            setLoading(true);
            setError(null);
            await Promise.all([fetchUsers(), fetchSessionStats()]);
        } finally {
            setLoading(false);
        }
    }, [fetchUsers, fetchSessionStats]);

    useEffect(() => {        
        loadDashboard();
     
    }, [loadDashboard]);

  return (
    <main className="admin-dashboard">
    <div className="admin-dashboard__heading">
      <h1>Admin Dashboard</h1>
      <p>Welcome to the admin dashboard!</p>
    </div>

    <section className="admin-dashboard__section">
        <h2>Statistics</h2>
        {loading && <p className="admin-dashboard__message" role="status">Loading...</p>}
        {error && <p className="admin-dashboard__error" role="alert">Error: {error.message}</p>}
        {!loading && !error && (
            <div className="admin-dashboard__stats">
                <div className="admin-dashboard__stat"><span>Active users</span><strong>{stats.activeUsers}</strong></div>
                <div className="admin-dashboard__stat"><span>Total sessions</span><strong>{stats.totalCheckInSessions}</strong></div>
                <div className="admin-dashboard__stat"><span>Planned sessions</span><strong>{stats.plannedCheckInSessions}</strong></div>
                <div className="admin-dashboard__stat"><span>Active sessions</span><strong>{stats.activeCheckInSessions}</strong></div>
                <div className="admin-dashboard__stat"><span>Missed sessions</span><strong>{stats.missedCheckInSessions}</strong></div>
            </div>
        )}
    </section>

    <section className="admin-dashboard__section">
        <h2>Users</h2>
        <div>
            <UserForm onSubmit={handleUserCreate} title="Create new user" />
        </div>
        {loading && <p className="admin-dashboard__message" role="status">Loading...</p>}
        {error && <p className="admin-dashboard__error" role="alert">Error: {error.message}</p>}
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
                    {users.map(user => (
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

    </main>
  );
}

export default AdminDashboard;
