import { useNavigate } from "react-router-dom"


function LandingPage() {

    const navigate = useNavigate()

  return (
    <div className="landing-page">
      <h1>Welcome to TripSentinel</h1>
      <p>Your ultimate travel companion.</p>

      <button
            className="btn btn--primary"
            onClick={() => navigate("/login")}
          >
            Go to Login
          </button>
    </div>
  );
}

export default LandingPage