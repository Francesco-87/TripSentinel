import { useOutletContext } from "react-router-dom";
import {useState} from "react";
import { formatDateTime } from "../../utils/dateTime.js";
import { matchUserToId } from "../../utils/userUtils.js";
import {createResponderAvailability, 
    updateResponderAvailability
 } from "../../services/availabilityServices.js";
 import Modal from "../layout/Modal.jsx";
 import AvailabilityForm from "./AvailabilityForm.jsx";



function AdminAvailability(){
    const {users,responderAvailability,fetchAvailability,loading, error,} = useOutletContext();
    const [selectedAvailability, setSelectedAvailability] = useState(null);
    const [availabilitySearchTerm, setAvailabilitySearchTerm] = useState("");


    async function handleAvailabilityCreate(availabilityData){
        await createResponderAvailability(availabilityData);
        await fetchAvailability();
        
    }

    async function handleAvailabilityUpdate(availabilityData){
            await updateResponderAvailability(selectedAvailability.id, availabilityData)
            await fetchAvailability();
            setSelectedAvailability(null)
        }

    function searchAvailability(availabilitySearchTerm){
        const searchTerm = availabilitySearchTerm.trim().toLowerCase();

         if (!searchTerm) return responderAvailability;

       const filteredUsers = users.filter(user =>
          `${user.firstName} ${user.lastName}`.toLowerCase().includes(searchTerm)
      );
      const matchingUserIds = filteredUsers.map(user => user.id);
        return responderAvailability.filter(responderAvailability => {
            
            return responderAvailability.id.toString() === searchTerm ||
                responderAvailability.responderId.toString() === searchTerm ||
                matchingUserIds.includes(responderAvailability.responderId);
                              
        });
    }




    const filteredAvailability = searchAvailability(availabilitySearchTerm);
    return(
        <section>
            <h2>Availability</h2>
            <div>
                <AvailabilityForm onSubmit={handleAvailabilityCreate} title="Create new Availability"/>
            </div>
             <div >
            <input type="search"
            placeholder="Search availability..."
            aria-label="Search availability"
            value={availabilitySearchTerm}
            onChange={(e) => setAvailabilitySearchTerm(e.target.value)}
             />
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
                        {filteredAvailability.length === 0 &&(
                            <tr>
                                <td colSpan={6}>No Available Responder found</td>
                            </tr>
                        )}
                        {filteredAvailability.map(responderAvailability => {
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
                                    <button onClick={() => setSelectedAvailability(responderAvailability)}>
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
             {/*Edit availability modal*/}
             {selectedAvailability && (
            <Modal title="Edit Availability" onClose={() => setSelectedAvailability(null)}>
              <div onClick={(e) => e.stopPropagation()}>
                <AvailabilityForm 
                  key={selectedAvailability.id}
                  onSubmit={handleAvailabilityUpdate}
                  initialData={selectedAvailability}
                  submitLabel="Update Availability"
                  title={null}
                />            
                
              </div>                
            </Modal>
        )}
        </section>
    )
}

export default AdminAvailability;
