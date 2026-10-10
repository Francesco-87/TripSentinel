import {useState, useId} from "react";
import { toLocalDateTimeInput } from "../../utils/dateTime";
import "../../styles/AvailabilityForm.css";


function AvailabilityForm({onSubmit, initialData=null, submitLabel = "Create availability", title = "Create availability"}){
    
    const emptyForm = {
    responderId: "",
    availableFrom: "",
    availableUntil: "",
    timeZone: "Europe/Oslo",
    status: "AVAILABLE",
    };

    const [formData, setFormData] = useState(buildInitialFormData(initialData) || emptyForm);
    const [isSaving, setIsSaving] = useState(false);
    const [submitError, setSubmitError] = useState(null);
    const formId = useId();

    //HELPER METHODS 
    // For the formdata
    function buildInitialFormData(initialData) {
        if(!initialData) return emptyForm;
        return{
             responderId: initialData.responderId,
            availableFrom: toLocalDateTimeInput(initialData.availableFrom, initialData.timeZone),
            availableUntil: toLocalDateTimeInput(initialData.availableUntil, initialData.timeZone),
            timeZone: initialData.timeZone,
            status: initialData.status,
        }
    }


    function handleChange(e){
        const {name, value, type} = e.target
        setFormData((prev) => ({
                ...prev,
                [name]: type === "number" ? (value === "" ? "" : Number(value)) : value
            }));
    }

  async  function handleSubmit(e){
         e.preventDefault()
        if (isSaving) return;
        setSubmitError(null);

        setIsSaving(true);
        try {
            await onSubmit(formData);
            if (!initialData) {
                setFormData(emptyForm);
            }
        } catch (error) {
            setSubmitError(error.message || "Unable to save availability. Please try again.");
        } finally {
            setIsSaving(false);
        }
    }


    return(
        <div className="availability-form">
            {title && <h3>{title}</h3>}
            <form onSubmit={handleSubmit} aria-busy={isSaving}>
                <fieldset className="availability-form__fields" disabled={isSaving}>
                    <legend>Availability details</legend>
                    <div>
                        <label htmlFor={`${formId}-responderId`}>Responder Id</label>
                         
                        <input 
                            type="number"
                            min={1}
                            step={1}
                            id={`${formId}-responderId`}
                            name="responderId"
                            value={formData.responderId}
                            onChange={handleChange}
                            required
                            placeholder="Responder ID"
                            readOnly={Boolean(initialData)}
                            />                            
                    </div>
                    <div>
                        <label htmlFor={`${formId}-availableFrom`}>Available From</label>
                        <input
                            type="datetime-local"
                            id={`${formId}-availableFrom`}
                            name="availableFrom"
                            value={formData.availableFrom}
                            onChange={handleChange}
                            required
                            />
                    </div>
                    <div>
                        <label htmlFor={`${formId}-availableUntil`}>Available Until</label>
                        <input
                            type="datetime-local"
                            id={`${formId}-availableUntil`}
                            name="availableUntil"
                            value={formData.availableUntil}
                            onChange={handleChange}
                            required
                            />
                    </div>
                </fieldset>
                      <div className="availability-form__actions">
                    {submitError && <p role="alert">{submitError}</p>}
                    <button type="submit" disabled={isSaving}>
                        {isSaving ? "Saving..." : submitLabel}
                    </button>
                </div>
            </form>
        </div>
    )
}

export default AvailabilityForm;
