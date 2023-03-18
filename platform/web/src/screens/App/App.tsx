import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from '../Home/index';
import Destinations from "../Destinations/index";
import "./styles.css"



function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/destinations" element={<Destinations />} />
      </Routes>
    </Router>
  );
}

export default App;
