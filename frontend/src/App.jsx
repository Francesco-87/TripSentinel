import LandingPage from './pages/LandingPage'
import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'

import './styles/App.css'

import { BrowserRouter, Routes, Route } from "react-router-dom"
import PublicLayout from './components/layout/PublicLayout'
import DashboardLayout from './components/layout/DashboardLayout'
import AdminDashboard from './components/dashboard/AdminDashboard'
import ResponderDashboard from './components/dashboard/ResponderDashboard'
import CustomerDashboard from './components/dashboard/CustomerDashboard'


function App() {
  return (
  
    

  <BrowserRouter>
  <Routes>
    <Route element={<PublicLayout />}>
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={<LoginPage />} />
    </Route>
    <Route element={<DashboardLayout />}>
      <Route path="/dashboard" element={<DashboardPage />} />
      <Route path="/admin" element={<AdminDashboard />} />
      <Route path="/responder" element={<ResponderDashboard />} />
      <Route path="/customer" element={<CustomerDashboard />} />
    </Route>
  </Routes>
  

  
    
    
  </BrowserRouter>
  

  )
}

export default App