import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage.jsx';
import WorkoutPage from './pages/WorkoutPage.jsx';
import LoginPage from './pages/LoginPage.jsx'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/workout" element={<WorkoutPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
      </Routes>
    </Router>
  );
}

export default App;
