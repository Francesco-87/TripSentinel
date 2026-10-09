import { useState, useCallback, useEffect } from 'react';
import { getUsers } from '../../services/userService.js';
import {getCheckInSessions} from '../../services/checkInSessionsService.js';

import '../../styles/AdminDashboard.css';
import { Outlet, NavLink } from 'react-router-dom';


function AdminDashboard() {

    const [stats, setStats] = useState({
        activeUsers: 0,
        totalCheckInSessions: 0,
        plannedCheckInSessions: 0,
        activeCheckInSessions: 0,
        missedCheckInSessions: 0

    });
    const [users, setUsers] = useState([]);
    const [sessions, setSessions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);



    // Stable callbacks keep the loading effect from repeating on every render.
    const fetchUsers = useCallback(async () => {
           try {
                const users = await getUsers();
                setUsers(users);
                const activeUsers = users.filter(user => user.status === 'ACTIVE').length;
                // Preserve session counts when refreshing only the users.
                setStats(prev => ({ ...prev, activeUsers }));
           } catch (error) {
                console.error('Error fetching users:', error);
                setError(error);
           }
    }, []);


    const fetchSessions = useCallback(async () => {
            try {
                const sessionStats = await getCheckInSessions();
                setSessions(sessionStats);
                const plannedCheckInSessions = sessionStats.filter(session => session.status === 'PLANNED').length;
                const activeCheckInSessions = sessionStats.filter(session => session.status === 'ACTIVE').length;
                const missedCheckInSessions = sessionStats.filter(session => session.status === 'MISSED').length;

               
                setStats(prev => ({ ...prev, totalCheckInSessions: sessionStats.length, plannedCheckInSessions, activeCheckInSessions, missedCheckInSessions }));
            } catch (error) {
                console.error('Error fetching stats:', error);
                setError(error);
            }
        }, []);
    // Wait for both independent requests before showing the dashboard.
    const loadDashboard = useCallback(async () => {
        try{
            setLoading(true);
            setError(null);
            await Promise.all([fetchUsers(), fetchSessions()]);
        } finally {
            setLoading(false);
        }
    }, [fetchUsers, fetchSessions]);

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
        <nav className="admin-dashboard__nav" aria-label="Admin dashboard navigation">
            <NavLink to="/admin/users" className="admin-dashboard__nav-link">Users</NavLink>
            <NavLink to="/admin/sessions" className="admin-dashboard__nav-link">Sessions</NavLink>
            <button type="button" className="admin-dashboard__nav-link" disabled>Availability</button>
        </nav>
        {/* Share one user list between the overview and nested management views. */}
        <Outlet context={{ users, fetchUsers, fetchSessions,sessions, loading, error }} />
    </section>


    </main>
  );
}

export default AdminDashboard;
