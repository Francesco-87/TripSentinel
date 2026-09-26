import LandingPage from './pages/LandingPage'
import LoginPage from './pages/LoginPage'
import Header from './components//layout/Header'

import './styles/App.css'

import { BrowserRouter, Routes, Route } from "react-router-dom"

function App() {
  return (
  
    

  <BrowserRouter>
  <Header />
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={<LoginPage />} />
      
    </Routes>
  
    
    
  </BrowserRouter>
  

  )
}

export default App