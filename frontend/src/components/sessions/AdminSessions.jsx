import {useState} from "react";
import { useOutletContext } from 'react-router-dom';
import SessionForm from "./SessionForm.jsx";
import {adminCreateCheckInSession, adminUpdateSession} from "../../services/checkInSessionsService.js";
import Modal from "../layout/Modal.jsx";
import "../../styles/AdminSessions.css";





// Display backend UTC timestamps in the session's timezone, not the browser's timezone.
function formatSessionTime(timestamp, timeZone) {
    if (!timestamp) return "—";

    return new Intl.DateTimeFormat("en-GB", {
        timeZone,
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hourCycle: "h23",
    }).format(new Date(timestamp));
}

function AdminSessions() {

    // The parent owns sessions so list updates also refresh the overview counts.
    const { sessions, fetchSessions, loading, error } = useOutletContext();
    const [selectedSession, setSelectedSession] = useState(null);
    const [sessionSearchTerm, setSessionSearchTerm] = useState("");

    // Create a new session and refresh the list
    async function handleSessionCreate(sessionData) {
        await adminCreateCheckInSession(sessionData)
        await fetchSessions()
    }

    // Update an existing session and close the edit modal
    async function handleSessionUpdate(sessionData) {
        await adminUpdateSession(selectedSession.id, sessionData)
        await fetchSessions()
        setSelectedSession(null)
    }

    function searchSessions(sessionSearchTerm) {
        const searchTerm = sessionSearchTerm.trim().toLowerCase();

        if (!searchTerm) return sessions;

        return sessions.filter(session => {
              return session.id.toString() === searchTerm || 
              session.customerId.toString() === searchTerm || 
              session.responderId.toString() === searchTerm;
        });
      }

      const filteredSessions = searchSessions(sessionSearchTerm)

  return (
    <section className="admin-dashboard__section admin-sessions">
      <h2>Sessions</h2>
      <div>
        <SessionForm onSubmit={handleSessionCreate} title="Create new Session" />
      </div>
      <div className="admin-sessions__search">
            <input type="search"
            placeholder="Search sessions..."
            aria-label="Search sessions"
            value={sessionSearchTerm}
            onChange={(e) => setSessionSearchTerm(e.target.value)}
             />
        </div>

        {loading && <p className="admin-dashboard__message" role="status">Loading...</p>}
        {error && <p className="admin-dashboard__error" role="alert">Error: {error.message}</p>}
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
              {filteredSessions.length === 0 && (
                <tr>
                  <td colSpan={10}>No sessions found.</td>
                </tr>
              )}
              {filteredSessions.map(session => (
                <tr key={session.id}>
                  <td>{session.id}</td>
                  <td>{session.customerId}</td>
                  <td>{session.responderId}</td>
                  <td>{session.locationDescription}</td>
                  <td>{formatSessionTime(session.startAt, session.timeZone)}</td>
                  <td>{formatSessionTime(session.expectedReturnAt, session.timeZone)}</td>
                  <td>{formatSessionTime(session.latestCheckInAt, session.timeZone)}</td>
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
        {/*Edit session modal*/}
        {selectedSession && (
            <Modal title="Edit Session" onClose={() => setSelectedSession(null)}>
              <div onClick={(e) => e.stopPropagation()}>
                <SessionForm
                  key={selectedSession.id}
                  onSubmit={handleSessionUpdate}
                  initialData={selectedSession}
                  submitLabel="Update Session"
                  title={null}
                />            
                
              </div>
                
            </Modal>
        )}

    </section>
  );
}

export default AdminSessions;
