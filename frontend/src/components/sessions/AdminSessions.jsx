import {useState} from "react";
import { useOutletContext } from 'react-router-dom';
import SessionForm from "./SessionForm.jsx";
import {adminCreateCheckInSession, adminUpdateSession, cancelSession} from "../../services/checkInSessionsService.js";
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
    const { users, sessions, fetchSessions, loading, error } = useOutletContext();
    const [selectedSession, setSelectedSession] = useState(null);
    const [sessionSearchTerm, setSessionSearchTerm] = useState("");
    const [sessionCancel, setSessionCancel] = useState(null);
    const [cancelError, setCancelError] = useState("");

    // Create a new session and refresh the list
    async function handleSessionCreate(sessionData) {
        await adminCreateCheckInSession(sessionData);
        await fetchSessions();
    }

    // Update an existing session and close the edit modal
    async function handleSessionUpdate(sessionData) {
        await adminUpdateSession(selectedSession.id, sessionData);
        await fetchSessions();
        setSelectedSession(null);
    }

    function searchSessions(sessionSearchTerm) {
        const searchTerm = sessionSearchTerm.trim().toLowerCase();

        if (!searchTerm) return sessions;

       const filteredUsers = users.filter(user =>
          `${user.firstName} ${user.lastName}`.toLowerCase().includes(searchTerm)
      );
      const matchingUserIds = filteredUsers.map(user => user.id);
        return sessions.filter(session => {
            
            return session.id.toString() === searchTerm ||
                session.customerId.toString() === searchTerm ||
                session.responderId.toString() === searchTerm ||
                matchingUserIds.includes(session.customerId) ||
                matchingUserIds.includes(session.responderId) ||
                session.locationDescription.toLowerCase().includes(searchTerm);
                
               
        });
    }

    function matchUserToId(sessionUserId) {
        const userFound = users.find(user => user.id === sessionUserId);
        return userFound;
    }

    const filteredSessions = searchSessions(sessionSearchTerm);

  async  function cancelSessionHandler(){
    setCancelError("");
    try{
       await cancelSession(sessionCancel.id);
       await fetchSessions();
       setSessionCancel(null);
    }catch (error) {
            setCancelError(error.message || "Unable to cancel session. Please try again.");
    }
    
      
    }

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
                <th scope="col">Status</th>
                <th scope="col">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredSessions.length === 0 && (
                <tr>
                  <td colSpan={9}>No sessions found.</td>
                </tr>
              )}
              {filteredSessions.map(session => {
                // Find both participants once per row. IDs remain visible if a user is missing.
                const customer = matchUserToId(session.customerId);
                const responder = matchUserToId(session.responderId);

                return (
                <tr key={session.id}>
                  <td>{session.id}</td>
                  <td>
                    {customer && (
                      <div>{customer.firstName} {customer.lastName}</div>
                    )}
                    <small>ID: {session.customerId}</small>
                  </td>
                  <td>
                    {responder && (
                      <div>{responder.firstName} {responder.lastName}</div>
                    )}
                    <small>ID: {session.responderId}</small>
                  </td>
                  <td>{session.locationDescription}</td>
                  <td title={session.timeZone}>{formatSessionTime(session.startAt, session.timeZone)}</td>
                  <td title={session.timeZone}>{formatSessionTime(session.expectedReturnAt, session.timeZone)}</td>
                  <td title={session.timeZone}>{formatSessionTime(session.latestCheckInAt, session.timeZone)}</td>
                  <td>{session.status}</td>
                  <td>
                    <button onClick={() => setSelectedSession(session)}>Edit</button>
                    <button onClick={() => {
                      setCancelError("");
                      setSessionCancel(session);
                    }}>Cancel</button>
                  </td>
                </tr>
                );
              })}
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
         {/*Cancel session modal*/}
         {sessionCancel && (
          <Modal title="Cancel Session" onClose={() => setSessionCancel(null)}>
              <div onClick={(e) => e.stopPropagation()}>
                <p>Are you sure you want to cancel the Session corresponding to ID-number: {sessionCancel.id}</p>
                {cancelError && (
                  <p className="admin-dashboard__error" role="alert">{cancelError}</p>
                )}
                <button onClick={() => setSessionCancel(null)}>Keep Session</button>
                <button onClick={cancelSessionHandler}>Confirm</button>
              </div>

          </Modal>
         )}

    </section>
  );
}

export default AdminSessions;
