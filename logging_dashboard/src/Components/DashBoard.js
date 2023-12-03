import React from 'react';
import TableStats from './TableStats';
import MetricCard from './MetricCard';
import MetricChartCard from './MetricChartCard';
import CloudSyncIcon from '@mui/icons-material/CloudSync';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import axios from 'axios';


class DashBoard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      all: [],
      read: [],
      write: [],
      ranked: [],
      MReadThanWriteCount: 0, //Reads of Reads logs
      MWriteThanReadCount: 0, //Writes of Writes logs
      parsedRankedLogs: [],
      isSnackbarOpen: false,
    };
  }

  componentDidMount() {
    this.fetchLogs();
  }

  fetchLogs = () => {
    // Faites une requête GET pour récupérer les utilisateurs
    axios.get('http://localhost:8080/api/logs/all')
      .then(response => {
        const parsedAllLogs = this.parseLogData(response.data);
        this.setState({ all: parsedAllLogs });

        axios.get('http://localhost:8080/api/logs/read')
          .then(response => {
            const writeNumber = this.countUsersReadsWrites(response.data, 'r');
            this.setState({ MReadThanWriteCount: writeNumber });
            this.setState({ read: response.data });

            axios.get('http://localhost:8080/api/logs/write')
              .then(response => {
                const writeNumber = this.countUsersReadsWrites(response.data, 'w');
                this.setState({ MWriteThanReadCount: writeNumber });
                this.setState({ write: response.data });

                axios.get('http://localhost:8080/api/logs/ranked')
                  .then(response => {
                    const parsedRankedLogs = this.countOperations(response.data);
                    this.setState({ parsedRankedLogs: parsedRankedLogs });
                    this.setState({ ranked: response.data });
                  })
                  .catch(error => {console.error('Erreur lors de la récupération des utilisateurs :', error);});
              })
              .catch(error => {console.error('Erreur lors de la récupération des utilisateurs :', error);});
          })
          .catch(error => {console.error('Erreur lors de la récupération des utilisateurs :', error);});
      })
      .catch(error => {console.error('Erreur lors de la récupération des utilisateurs :', error);});
  };

  countOperations(input) {
    const lines = input.split('\n');
    const profiles = [];
    let currentProfile = null;
  
    for (const line of lines) {
      if (line.startsWith('UserProfile')) {
        const userIdMatch = line.match(/userId=(\w+)/);
        if (userIdMatch) {
          const userId = userIdMatch[1];
          currentProfile = {
            index: profiles.length + 1,
            userId,
            readOperations: 0,
            writeOperations: 0,
          };
          profiles.push(currentProfile);
        }
      } else if (line.includes('READ')) {
        currentProfile.readOperations += 1;
      } else if (line.includes('WRITE')) {
        currentProfile.writeOperations += 1;
      }
    }
  
    return profiles;
  }

  parseLogData = (logData) => {
    const logs = logData.split('\n').map((log) => {
      const [timestamp, sessionId, operation, ...details] = log.split('|').map((item) => item.trim());
      const [entityType, entityId] = details.join(' ').split('|').map((item) => item.trim());
  
      return {
        timestamp,
        sessionId,
        operation,
        entityType,
        entityId,
      };
    });
  
    return logs;
  };

  countUsersReadsWrites(input, option) {
    const lines = input.split('\n');
    const userCounts = {};
  
    let currentUserId = null;
  
    for (const line of lines) {
      if (line.startsWith('UserProfile')) {
        // Extract userId from the UserProfile line
        const userIdMatch = line.match(/userId=(\w+)/);
        if (userIdMatch) {
          currentUserId = userIdMatch[1];
          userCounts[currentUserId] = userCounts[currentUserId] || { reads: 0, writes: 0 };
        }
      } else if (line.includes('READ')) {
        userCounts[currentUserId].reads += 1;
      } else if (line.includes('WRITE')) {
        userCounts[currentUserId].writes += 1;
      }
    }
  
    if(option === 'w'){ //If we need to know writes
      const usersWithMoreWritesThanReads = Object.values(userCounts).reduce(
        (count, { reads, writes }) => (writes > reads ? count + 1 : count),
        0
      );
      return usersWithMoreWritesThanReads;
    } else { //need to know reads
      const usersWithMoreReadsThanWrites = Object.values(userCounts).reduce(
        (count, { reads, writes }) => (reads > writes ? count + 1 : count),
        0
      );
      return usersWithMoreReadsThanWrites;
    }
  }

  closeDialog = () => {
    this.setState({ isDialogOpen: false });
  };

  openDialog = () => {
    this.setState({ isDialogOpen: true });
  };

  handleUserSelection = (userId) => {
    this.setState({ selectedUserId: userId }, () => {
      this.closeDialog();
    });
  };

  showSnackbar = () => {
    this.fetchLogs();
    this.setState({ isSnackbarOpen: true });
  };

  closeSnackbar = () => {
    this.setState({ isSnackbarOpen: false });
  };

  render() {
    const {
      MReadThanWriteCount,
      MWriteThanReadCount,
    } = this.state;

    return (
      <div className="bg-dark-secondary h-100vh justify-start w-full p-4 rounded-xl mt-3 flex flex-col">
        <div className='flex flex-row justify-between items-center'>
          <p className='text-3xl font-bold text-white'>Your DashBoard</p>
          <div className='bg-violet-500 p-2 rounded-xl flex flex-row items-center gap-2 hover:bg-violet-600 hover:cursor-pointer hover:scale-105 ease-in duration-300'
          onClick={this.openDialog}>
            <p className='text-md font-light text-white' onClick={this.showSnackbar}>Refresh</p>
            <CloudSyncIcon className='text-white'></CloudSyncIcon>
          </div>
        </div>
        <p className='text-md font-light text-white'>Here you can monitor your logs</p>

        <div className="grid grid-flow-rows md:grid-cols-2 xs:grid-cols-2 gap-4 mt-10 ">
            <div className='overflow-auto'>
              <MetricChartCard
                  title="User By Requests"
                  metricLabel="Reads"
                  data={this.state.parsedRankedLogs}
                  decorationColor="green"
              />
            </div>
            
            <MetricCard
                title="Users with more read than writes"
                metricLabel="Number of users"
                metricValue={MReadThanWriteCount}
                decorationColor="red"
                title2="Users with more writes than reads"
                metricLabel2="Number of users"
                metricValue2={MWriteThanReadCount}
                decorationColor2="orange"
            />
        </div>
        <div className="grid grid-flow-rows md:grid-cols-1 xs:grid-cols-1 gap-4 mt-10 text-white">
            <div className='bg-dark-third p-5 rounded-lg overflow-auto'>
                <div className='mb-4 p-2 bg-dark-secondary rounded-lg'>
                    <p className='text-xl font-sans font-semibold'>All logs</p>
                </div>
                <TableStats data={this.state.all}/>
            </div>
        </div>
        <Snackbar open={this.state.isSnackbarOpen} autoHideDuration={1000} onClose={this.closeSnackbar}>
          <MuiAlert elevation={6} variant="filled" onClose={this.closeSnackbar} severity="success">
            Logs refreshed successfully!
          </MuiAlert>
        </Snackbar>
      </div>
    );
  }
}

export default DashBoard;