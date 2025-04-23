import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import GymsPage from './pages/HomePage.jsx';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<GymsPage />} />
      </Routes>
    </Router>
  );
}

export default App;
