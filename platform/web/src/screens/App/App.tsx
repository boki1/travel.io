import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import Home from '../Home/index';



function App() {
  return (
    <Router>
      <Route path="/" element={<Home />} />
    </Router>
  );
}

export default App;
