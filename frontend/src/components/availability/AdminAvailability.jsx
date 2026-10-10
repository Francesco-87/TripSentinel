import { useOutletContext } from "react-router-dom";
import { formatDateTime } from "../../utils/dateTime.js";
import { matchUserToId } from "../../utils/userUtils.js";



function AdminAvailability(){
    const {users,responderAvailability,loading, error,} = useOutletContext();

    return(
        <section>
            <h2>Availability</h2>
            <div>
                <p>sessionform</p>
            </div>
            <div>
                <p>sessionserch</p>
            </div>

             {loading && <p className="admin-dashboard__message" role="status">Loading...</p>}
            {error && <p className="admin-dashboard__error" role="alert">Error: {error.message}</p>}
            {!loading && !error && (

            <div>
                <table>
                    <thead>
                        <tr>
                            <th scope="col">Availability ID</th>
                            <th scope="col">Responder</th>
                            <th scope="col">Available From</th>
                            <th scope="col">Available Until</th>
                            <th scope="col">Status</th>           
                            <th scope="col">Actions</th>                 
                        </tr>
                    </thead>
                    <tbody>
                        {responderAvailability.length === 0 &&(
                            <tr>
                                <td colSpan={6}>No Available Responder found</td>
                            </tr>
                        )}
                        {responderAvailability.map(responderAvailability => {
                            const responder = matchUserToId(users, responderAvailability.responderId);

                            return(
                            <tr key={responderAvailability.id}>
                                <td>{responderAvailability.id}</td>


                                <td>{responder && (
                                    <div>{responder.firstName} {responder.lastName}</div>
                                    )}
                                    <small>ID: {responderAvailability.responderId}</small>
                                    </td>
                                <td title={responderAvailability.timeZone}>{formatDateTime(responderAvailability.availableFrom, responderAvailability.timeZone)}</td>
                                <td title={responderAvailability.timeZone}>{formatDateTime(responderAvailability.availableUntil, responderAvailability.timeZone)}</td>
                                <td>{responderAvailability.status}</td>
                                <td>
                                    <button type="button">
                                        Edit
                                    </button>
                                </td>
                            </tr>
                            );
                            })}

                    </tbody>
                </table>
            </div>
            )}
        </section>
    )
}

export default AdminAvailability;
