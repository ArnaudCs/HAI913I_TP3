import './App.css';

import React from 'react';
import Navbar from './Components/NavBar';
import DashBoard from './Components/DashBoard';

class App extends React.Component {
  render() {
    return (
      <div className='p-3 bg-dark-primary'>
        <Navbar />
        <DashBoard />
      </div>
    );
  }
}

export default App;
