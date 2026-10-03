import { useState, useId } from "react";
import "../../styles/UserForm.css";


function UserForm({ onSubmit, initialData = null, submitLabel = "Create User", title = "Create User" }) {

    const emptyForm ={
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        password: "",
        confirmPassword: "",
        roles: [],
        status: "ACTIVE",
    }

    const [formData, setFormData] = useState(initialData || emptyForm);
    const [isSaving, setIsSaving] = useState(false);
    const [submitError, setSubmitError] = useState(null);
    const formId = useId();


    function handleChange(e) { 
        const { name, value, type, checked } = e.target
        if(name === "roles") {
            setFormData((prev) => ({
                ...prev,
                roles: checked
                    ? [...prev.roles, value]
                    : prev.roles.filter(role => role !== value)
            }));
        } else {
            setFormData((prev) => ({
                ...prev,
                [name]: type === "number" ? (value === "" ? "" : Number(value)) : value
            }));
        }
    }

    // Handle form submission; calls parent callback and resets form if creating new user
    async function handleSubmit(e) {
        e.preventDefault()
        if (isSaving) return;
        setSubmitError(null);
        if (formData.roles.length === 0) {
            alert("Select at least one role")
            return
        }
        if (!initialData && formData.password !== formData.confirmPassword) {
            alert("Passwords do not match")
            return
        }
        const formDataToSubmit = { 
            firstName: formData.firstName,
            lastName: formData.lastName,
            email: formData.email,
            phoneNumber: formData.phoneNumber,
            roles: formData.roles,
          
         }
       if (!initialData) {
            formDataToSubmit.password = formData.password
            formDataToSubmit.status = "ACTIVE"  // Set default status for new users
        }
        setIsSaving(true);
        try {
            await onSubmit(formDataToSubmit);
            if (!initialData) {
                setFormData(emptyForm);
            }
        } catch (error) {
            setSubmitError(error.message || "Unable to save user. Please try again.");
        } finally {
            setIsSaving(false);
        }
    }

    return (
        <div className="user-form">
            {title && <h3>{title}</h3>}
            <form onSubmit={handleSubmit} aria-busy={isSaving}>
                <fieldset className="user-form__fields user-form__controls" disabled={isSaving}>
                <legend className="user-form__sr-only">User details</legend>
                <div>
                    <label htmlFor={`${formId}-firstName`}>First Name:</label>
                    <input
                        type="text"
                        id={`${formId}-firstName`}
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        required
                        maxLength={50}
                        placeholder="Enter first name"  
                    />
                </div>
                <div>
                    <label htmlFor={`${formId}-lastName`}>Last Name:</label>
                    <input
                        type="text"
                        id={`${formId}-lastName`}
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        required
                        maxLength={50}
                        placeholder="Enter last name"
                    />
                </div>
                <div>
                    <label htmlFor={`${formId}-email`}>Email:</label>
                    <input
                        type="email"
                        id={`${formId}-email`}
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                        placeholder="Enter email"
                    />
                </div>
                <div>
                    <label htmlFor={`${formId}-phoneNumber`}>Phone Number:</label>
                    <input 
                        type="text"
                        id={`${formId}-phoneNumber`}
                        name="phoneNumber"
                        value={formData.phoneNumber}
                        onChange={handleChange}
                        required
                        placeholder="Enter phone number"
                    />
                </div>
                {!initialData && (
                    
                <div className="user-form__passwords"> 
                     <div>
                            <label htmlFor={`${formId}-password`}>Password:</label>
                            <input
                                type="password"
                                id={`${formId}-password`}
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                                minLength={12}
                                maxLength={128}
                                placeholder="Enter password"
                            />
                        </div>
                        <div>
                            <label htmlFor={`${formId}-confirmPassword`}>Confirm Password:</label>
                            <input 
                                type="password"
                                id={`${formId}-confirmPassword`}
                                name="confirmPassword"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                                required
                                minLength={12}
                                maxLength={128}
                                placeholder="Confirm password"
                            />
                        </div>  
                </div>)}
               
                <fieldset className="user-form__roles">
                    <legend>Roles (choose at least one)</legend>
                    {['ADMIN', 'RESPONDER', 'CUSTOMER'].map(role => (
                        <label className="user-form__role" key={role}>
                            <input
                                type="checkbox"
                                name="roles"
                                value={role}
                                checked={formData.roles.includes(role)}
                                onChange={handleChange}
                            />
                            {role.charAt(0) + role.slice(1).toLowerCase()}
                        </label>
                    ))}
                </fieldset>
                <div className="user-form__actions"> 
                    {submitError && <p className="user-form__error" role="alert">{submitError}</p>}
                    <button type="submit" disabled={isSaving}>{isSaving ? "Saving…" : submitLabel}</button>
                </div>
                </fieldset>
            </form>
        </div>
    );
}

export default UserForm;
