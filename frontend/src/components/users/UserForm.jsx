import { useState, useEffect } from "react";
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

    useEffect(() => {
        if (initialData) {
            setFormData({
                id: initialData.id,
                firstName: initialData.firstName,
                lastName: initialData.lastName,
                email: initialData.email,
                phoneNumber: initialData.phoneNumber,
                roles: initialData.roles,
                status: initialData.status,
            })
        }else {
            setFormData(emptyForm);
        }
    }, [initialData]);

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
        // Call parent's onSubmit callback with form data
        await onSubmit(formDataToSubmit)
        // Reset form to empty state only when creating new user (not during updates)
        if (!initialData) {
        setFormData(emptyForm)
        }
    }

    return (
        <div className="user-form">
            <h3>{title}</h3>
            <form className="user-form__fields" onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="firstName">First Name:</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        required
                        maxLength={50}
                        placeholder="Enter first name"  
                    />
                </div>
                <div>
                    <label htmlFor="lastName">Last Name:</label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        required
                        maxLength={50}
                        placeholder="Enter last name"
                    />
                </div>
                <div>
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                        placeholder="Enter email"
                    />
                </div>
                <div>
                    <label htmlFor="phoneNumber">Phone Number:</label>
                    <input 
                        type="text"
                        id="phoneNumber"
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
                            <label htmlFor="password">Password:</label>
                            <input
                                type="password"
                                id="password"
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
                            <label htmlFor="confirmPassword">Confirm Password:</label>
                            <input 
                                type="password"
                                id="confirmPassword"
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
                    <button type="submit">{submitLabel}</button>
                </div>
            </form>
        </div>
    );
}

export default UserForm;
