import {useState} from "react";
import { useOutletContext } from 'react-router-dom';





function AdminSessions() {

// The parent owns users so table updates also refresh the overview count.
    const { users, fetchUsers, sessions, fetchSessions, loading, error } = useOutletContext();
    const [selectedSession, setSelectedSession] = useState(null);
    const [statusError, setStatusError] = useState(null);
    const [userSearchTerm, setUserSearchTerm] = useState("");


  return (
    <section className="admin-dashboard__section">
      <h2>Admin Sessions</h2>
      <div>
        <p>This is for the creation and management of sessions.</p>
      </div>
      <div>
            <input type="search"
            placeholder="Search sessions..."
            aria-label="Search sessions"
             />
        </div>

        {loading && <p className="admin-dashboard__message" role="status">Loading...</p>}
        {error && <p className="admin-dashboard__error" role="alert">Error: {error.message}</p>}
         {statusError && (
                <p className="admin-dashboard__error" role="alert">
                    {statusError}
                </p>
            )}
        {!loading && !error && (

        <div className="admin-dashboard__table-scroll" role="region" aria-label="Sessions table" tabIndex={0}>
          <table>
            <thead>
              <tr>
                <th scope="col">Session ID</th>
                <th scope="col">Customer</th>
                <th scope="col">Responder</th>
                <th scope="col">Location</th>
                <th scope="col">Start Time</th>
                <th scope="col">Expected Return</th>
                <th scope="col">Latest Check-In</th>
                <th scope="col">Time Zone</th>
                <th scope="col">Status</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
              {sessions.length === 0 && (
                <tr>
                  <td colSpan={10}>No sessions found.</td>
                </tr>
              )}
              {sessions.map(session => (
                <tr key={session.id}>
                  <td>{session.id}</td>
                  <td>{session.customerId}</td>
                  <td>{session.responderId}</td>
                  <td>{session.locationDescription}</td>
                  <td>{session.startAt}</td>
                  <td>{session.expectedReturnAt}</td>
                  <td>{session.latestCheckInAt}</td>
                  <td>{session.timeZone}</td>
                  <td>{session.status}</td>
                  <td>
                    <button onClick={() => setSelectedSession(session)}>Edit</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        )}

    </section>
  );
}

export default AdminSessions;
