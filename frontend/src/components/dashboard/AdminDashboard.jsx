import {useState} from 'react';
import {useEffect} from 'react';
import {getUsers} from '../../services/userService.js';


function AdminDashboard() {

    const [stats, setStats] = useState({
        activeUsers: 0,
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchStats() {
            try {
                const users = await getUsers();
                const activeUsers = users.filter(user => user.status === 'ACTIVE').length;

                setStats({ activeUsers });
            } catch (error) {
                console.error('Error fetching stats:', error);
                setError(error);
            } finally {
                setLoading(false);
            }
        }

        fetchStats();
    }, []);

  return (
    <main>
    <div className="hero ">
      <h1>Admin Dashboard</h1>
      <p>Welcome to the admin dashboard!</p>
    </div>

    <section>
        <h2>Statistics</h2>
        {loading && <p>Loading...</p>}
        {error && <p>Error: {error.message}</p>}
        {!loading && !error && <p>Active Users: {stats.activeUsers}</p>}
    </section>

    </main>
  );
}

export default AdminDashboard;
