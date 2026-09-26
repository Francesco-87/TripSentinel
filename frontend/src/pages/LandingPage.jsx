import { Link } from 'react-router-dom'
import '../styles/LandingPage.css'

function LandingPage() {
  return (
    <main className="landing-page">
      <div className="landing-page__hero">
        <h1>Welcome to TripSentinel</h1>
        <p>Plan your check-in. Keep your responder informed.</p>
        <Link className="btn btn--primary" to="/login">
          Log in
        </Link>
      </div>

      <section className="landing-page__about" id="about">
        <h2>About TripSentinel</h2>
        <p>
          TripSentinel is a personal safety check-in system designed for situations
          where someone expects to return or check in by a certain time.
        </p>
        <p>
          It provides a simple safety net for solo travelers, hikers, isolated
          workers, and anyone spending time alone. If a scheduled check-in is
          missed, TripSentinel can begin an escalation process instead of relying
          on someone noticing that something is wrong.
        </p>
        <p>
          The goal is simple: make planned check-ins structured, reliable, and easy
          to manage while keeping personal information limited to what is actually
          needed.
        </p>
      </section>

      <section className="landing-page__how" id="how">
        <h2>How It Works</h2>
        <ol className="landing-page__steps">

          <li className="landing-page__step">
            <h3>Plan your check-in</h3>
            <p>Set when you expect to return or confirm you're safe.</p>
          </li>

          <li className="landing-page__step">
            <h3>Assign a responder</h3>
            <p>
              Choose the person responsible for following up if you miss your
              check-in. Your assigned responder can see the session details and
              important notes they need to help.
            </p>
          </li>

          <li className="landing-page__step">
            <h3>Keep your responder informed</h3>
            <p>
              TripSentinel tracks your scheduled return and latest check-in deadline.
              Confirm you're safe before that deadline, or cancel the session if your
              plans change. Passing the expected return time does not count as a
              check-in or automatically complete the session.
            </p>
          </li>

          <li className="landing-page__step">
            <h3>Missed check-in?</h3>
            <p>
              If the latest check-in deadline passes without confirmation, the session
              is treated as a missed check-in and TripSentinel starts the configured
              escalation process. Your assigned responder follows up using the
              information you provided. A missed deadline does not mean you have
              returned safely.
            </p>
          </li>
        </ol>
      </section>
    </main>
  )
}

export default LandingPage
