import { useNavigate } from "react-router-dom"



function LandingPage() {

    const navigate = useNavigate()

  return (
    <div className="landing-page">
      <div className="landing-page__hero">
      <h1>Welcome to TripSentinel</h1>
      <p>Your ultimate travel companion.</p>
      </div>

      <button
            className="btn btn--primary"
            onClick={() => navigate("/login")}
          >
            Go to Login
          </button>
      <div className="HowItWorks" id="how">
        <h2>How It Works</h2>
        <p>TripSentinel helps you plan your trips efficiently by providing personalized recommendations, travel itineraries, and real-time updates.</p>
      </div>

      <div className="About" id="about">
        <h2>About TripSentinel</h2>
        <p>TripSentinel is a travel planning application designed to make your travel experience seamless and enjoyable. Our platform offers a range of features to help you organize your trips, discover new destinations, and stay informed throughout your journey.</p>
      </div>
    </div>
  );
}

export default LandingPage