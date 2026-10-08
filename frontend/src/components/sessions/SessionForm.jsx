import {useState, useId, useEffect, useCallback} from "react";
import "../../styles/SessionForm.css";
import {getCheckInMethods} from "../../services/checkInSessionsService.js";


function SessionForm({ onSubmit, initialData = null, submitLabel = "Create Session", title = "Create Session" }) {

    const emptyForm = {
    customerId: "",
    responderId: "",
    checkInMethodIds: [],
    locationDescription: "",
    startAt: "",
    expectedReturnAt: "",
    latestCheckInAt: "",
    timeZone: "Europe/Oslo",
    importantNotes: "",
};

    const [formData, setFormData] = useState(buildInitialFormData(initialData) || emptyForm);
    const [isSaving, setIsSaving] = useState(false);
    const [submitError, setSubmitError] = useState(null);
    const [checkInMethods, setCheckInMethods] = useState([]);
    const [methodsLoading, setMethodsLoading] = useState(true);
    const [methodsError, setMethodsError] = useState(null);
    const formId = useId();


    //HELPER METHODS 
    // For the formdata
    function buildInitialFormData(initialData) {
        if (!initialData) return emptyForm;
        return {
            customerId: initialData.customerId,
            responderId: initialData.responderId,
            checkInMethodIds: initialData.checkInMethods.map(method => method.id),
            locationDescription: initialData.locationDescription,
            startAt: toLocalDateTimeInput(initialData.startAt, initialData.timeZone),
            expectedReturnAt: toLocalDateTimeInput(initialData.expectedReturnAt, initialData.timeZone),
            latestCheckInAt: toLocalDateTimeInput(initialData.latestCheckInAt, initialData.timeZone),
            timeZone: initialData.timeZone,
            importantNotes: initialData.importantNotes ?? "",
        }
    }
    //For timezone, convert to local time
   function toLocalDateTimeInput(timestamp, timeZone) {
    if (!timestamp) return "";

    const formatter = new Intl.DateTimeFormat("en-GB", {
        timeZone,
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hourCycle: "h23",
    });

    const parts = formatter.formatToParts(new Date(timestamp));

    const getPart = (type) =>
        parts.find((part) => part.type === type).value;

    return `${getPart("year")}-${getPart("month")}-${getPart("day")}T${getPart("hour")}:${getPart("minute")}`;
}

    // Keep this reusable for retries without changing the effect dependency each render.
    const fetchCheckInMethods = useCallback(() => {
        return getCheckInMethods().then(
            methods => {
                setCheckInMethods(methods);
                setMethodsLoading(false);
            },
            error => {
                console.error('Error fetching check-in methods:', error);
                setMethodsError(error.message || "Unable to load check-in methods.");
                setMethodsLoading(false);
            }
        );
    }, [setCheckInMethods, setMethodsLoading, setMethodsError]);

    function retryCheckInMethods() {
        setMethodsLoading(true);
        setMethodsError(null);
        fetchCheckInMethods();
    }

    useEffect(() => {
        fetchCheckInMethods();
    }, [fetchCheckInMethods]);
        
            
    

    function handleChange(e) {
        const { name, value, type, checked } = e.target;
        if (name === "checkInMethodIds") {
            const methodId = Number(value);
            setFormData((prev) => ({
                ...prev,
                [name]: type === "checkbox" ? (checked ? [...prev[name], methodId] : prev[name].filter((id) => id !== methodId)) : value
            }));
        } else {
            setFormData((prevData) => ({
                ...prevData,
                [name]: type === "number" ? (value === "" ? "" : Number(value)) : type === "checkbox" ? checked : value
            }));
        }
    }

    // Handle form submission; calls parent callback and resets form if creating new session
    async function handleSubmit(e) {
        e.preventDefault()
        if (isSaving || methodsLoading || methodsError || checkInMethods.length === 0) return;
        setSubmitError(null);
         if (formData.checkInMethodIds.length === 0) {
            alert("Select at least one check-in method")
            return
        }
        setIsSaving(true);

        const formDataToSubmit = {
            ...(!initialData && { customerId: formData.customerId }),  // Only include customerId if creating a new session 
            responderId: formData.responderId,
            checkInMethodIds: formData.checkInMethodIds,
            locationDescription: formData.locationDescription, 
            ...((!initialData ||
                    formData.startAt !== toLocalDateTimeInput(initialData.startAt, initialData.timeZone)) 
                    && { startAt: formData.startAt }), 
            expectedReturnAt: formData.expectedReturnAt,
            latestCheckInAt: formData.latestCheckInAt,
            timeZone: formData.timeZone,
            importantNotes: formData.importantNotes,
        };
        try {
            await onSubmit(formDataToSubmit);
            if (!initialData) {
                setFormData(emptyForm);
            }
        } catch (error) {
            setSubmitError(error.message || "Unable to save session. Please try again.");
        } finally {
            setIsSaving(false);
        }
    }

    return (
        <div className="session-form">
            {title && <h3>{title}</h3>}
            <form onSubmit={handleSubmit} aria-busy={isSaving}>
                <fieldset className="session-form__fields" disabled={isSaving}>
                <legend className="session-form__legend">Session details</legend>
                
                <div>
                    <label htmlFor={`${formId}-customerId`}>Customer ID:</label>
                    <input 
                        readOnly={Boolean(initialData)}
                        type="number"
                        min="1"
                        step="1"
                        id={`${formId}-customerId`}
                        name="customerId"
                        value={formData.customerId}
                        onChange={handleChange}
                        placeholder="Customer ID"
                        required
                        
                    />
                </div>
                <div>
                    <label htmlFor={`${formId}-responderId`}>Responder ID:</label>
                    <input 
                        type="number"
                        min="1"
                        step="1"
                        id={`${formId}-responderId`}
                        name="responderId"
                        value={formData.responderId}
                        onChange={handleChange}
                        placeholder="Responder ID"
                        required
                    />
                </div>

                <div>
                    <label htmlFor={`${formId}-locationDescription`}>Location Description:</label>
                    <input
                        type="text"
                        id={`${formId}-locationDescription`}
                        name="locationDescription"
                        value={formData.locationDescription}
                        onChange={handleChange}
                        placeholder="Location Description"
                        required
                    />
                </div>
                <div>
                    <label htmlFor={`${formId}-startAt`}>Start Time:</label>
                    <input
                        type="datetime-local"
                        id={`${formId}-startAt`}
                        name="startAt"
                        value={formData.startAt}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                     <label htmlFor={`${formId}-expectedReturnAt`}>Expected Return:</label>
                    <input
                        type="datetime-local"
                        id={`${formId}-expectedReturnAt`}
                        name="expectedReturnAt"
                        value={formData.expectedReturnAt}
                        onChange={handleChange}
                        required
                    />
                </div>
                
                <div>
                    <label htmlFor={`${formId}-latestCheckInAt`}>Latest Check-In:</label>
                    <input
                        type="datetime-local"
                        id={`${formId}-latestCheckInAt`}
                        name="latestCheckInAt"
                        value={formData.latestCheckInAt}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <label htmlFor={`${formId}-timeZone`}>Time Zone:</label>
                    <input
                        type="text"
                        id={`${formId}-timeZone`}
                        name="timeZone"
                        value={formData.timeZone}
                        onChange={handleChange}
                        placeholder="Europe/Oslo"
                        required
                    />
                </div>
                <div>
                    <label htmlFor={`${formId}-importantNotes`}>Important Notes (optional):</label>
                    <textarea
                        id={`${formId}-importantNotes`}
                        name="importantNotes"
                        value={formData.importantNotes}
                        onChange={handleChange}
                        rows={4}
                    />
                </div>
                <fieldset className="session-form__methods">
                    <legend >Check-In Methods (choose at least one):</legend>
                    {methodsLoading && <p role="status">Loading check-in methods…</p>}
                    {methodsError && (
                        <div>
                            <p role="alert">Unable to load check-in methods: {methodsError}</p>
                            <button type="button" onClick={retryCheckInMethods}>Retry</button>
                        </div>
                    )}
                    {!methodsLoading && !methodsError && checkInMethods.length === 0 && (
                        <p role="status">No check-in methods are available.</p>
                    )}
                    {checkInMethods.map((method) => (
                        <div className="session-form__method" key={method.id}>
                            <input
                                type="checkbox"
                                id={`${formId}-checkInMethod-${method.id}`}
                                name="checkInMethodIds"
                                value={method.id}
                                checked={formData.checkInMethodIds.includes(method.id)}
                                onChange={handleChange}
                                
                            />
                            <label htmlFor={`${formId}-checkInMethod-${method.id}`}>{method.name}</label>
                        </div>
                    ))}
                </fieldset>

            
                <div className="session-form__actions">
                    {submitError && <p role="alert">{submitError}</p>}
                    <button type="submit" disabled={isSaving || methodsLoading || Boolean(methodsError) || checkInMethods.length === 0}>
                        {isSaving ? "Saving..." : submitLabel}
                    </button>
                </div>
                </fieldset>
            </form>
        </div>
         )
}


export default SessionForm;
