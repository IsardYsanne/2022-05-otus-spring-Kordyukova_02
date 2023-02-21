import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter} from 'react-router-dom';
import Root from "./components/Root.jsx";

function App() {

  return (
      <div>
        <BrowserRouter>
          <Root/>
        </BrowserRouter>
      </div>
  );
}

export default App;
