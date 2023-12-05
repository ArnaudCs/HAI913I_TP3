import React from 'react';
import {
    Badge,
    Card,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeaderCell,
    TableRow,
    Text,
    Title,
  } from "@tremor/react";

class MetricChartCard extends React.Component {
    renderUserInformation(users) {
        return (
            <ul>
              {users.map((user, index) => (
                <li key={index}>
                  <Text>{`User ID: ${user.userId}, Reads: ${user.readOperations}, Writes: ${user.writeOperations}`}</Text>
                </li>
              ))}
            </ul>
        );
    }
  
    render() {

      const { data } = this.props;

  
      return (
        <div className={`bg-dark-third p-5 rounded-lg text-white shadow-2xl`}>
            <div className='mb-4 p-2 bg-dark-secondary rounded-lg'>
            <p className='text-xl font-sans font-semibold'>Title</p>
            </div>
    
            <Card>
                <Title>Ranked logs</Title>
                <Table className="mt-5">
                <TableHead>
                    <TableRow>
                    <TableHeaderCell className='text-center'>N°</TableHeaderCell>
                    <TableHeaderCell className='text-center'>User Id</TableHeaderCell>
                    <TableHeaderCell className='text-center'>Read Operations</TableHeaderCell>
                    <TableHeaderCell className='text-center'>Write Operations</TableHeaderCell>
                    </TableRow>
                </TableHead>
                <TableBody >
                    {data.map((item) => (
                        <TableRow key={item.userId}>
                        <TableCell className='text-center'>{item.index}</TableCell>
                        <TableCell className='text-center'>{item.userId}</TableCell>
                        <TableCell className='text-center'>
                            <Badge color='green'>
                                {item.readOperations}
                            </Badge>
                        </TableCell>
                        <TableCell className='text-center'>
                            <Badge color='blue'>
                                {item.writeOperations}
                            </Badge>
                        </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
                </Table>
            </Card>
        </div>
      );
    }
  }
  
  export default MetricChartCard;
  