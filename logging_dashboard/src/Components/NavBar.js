import React from 'react';
import './NavBar.css';
import FaceIcon from '@mui/icons-material/Face';

class Navbar extends React.Component {
  render() {
    return (
      <div className="bg-dark-secondary h-5vh flex justify-start items-center w-full p-4 justify-between rounded-xl top-0">
          <div className="text-xl font-bold text-white">
            <p>Logging Dashboard</p>
          </div>

          <div className='p-1 bg-dark-third rounded-lg'>
            <FaceIcon className='text-white'></FaceIcon>
          </div>
      </div>
    );
  }
}

export default Navbar;