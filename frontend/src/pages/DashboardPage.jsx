import {useNavigate} from 'react-router-dom'

function DashboardPage() {

const navigate = useNavigate();

  return (
    <div className="dashboard">
      <h1>Dashboard</h1>
      <p>Welcome to your dashboard!</p>
      <nav>
        <ul>
          <button onClick={() => navigate('/admin')}>Admin</button>
          <button onClick={() => navigate('/responder')}>Responder</button>
          <button onClick={() => navigate('/customer')}>Customer</button>
        </ul>
      </nav>
    </div>
  );
}

export default DashboardPage;