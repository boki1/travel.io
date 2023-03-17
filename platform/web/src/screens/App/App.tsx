import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from '../Home/index';



function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
      </Routes>
    </Router>
  );
}

export default App;
