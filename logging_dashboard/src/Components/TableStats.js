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

class TableStats extends React.Component {


  render() {
    const { data } = this.props;
    return (
      <Card >
        <Title>All registered logs</Title>
        <Table className="mt-5">
          <TableHead>
            <TableRow>
              <TableHeaderCell>Date</TableHeaderCell>
              <TableHeaderCell>User ID</TableHeaderCell>
              <TableHeaderCell>Operation</TableHeaderCell>
              <TableHeaderCell>Entity Requested</TableHeaderCell>
            </TableRow>
          </TableHead>
          <TableBody className='text-white'>
            {data.map((item) => (
              item.timestamp ? ( // Check if timestamp is not empty
                <TableRow key={item.timestamp}>
                  <TableCell>{item.timestamp}</TableCell>
                  <TableCell>
                    <Text>{item.sessionId}</Text>
                  </TableCell>
                  <TableCell>
                    <Badge color={item.operation === 'READ' ? 'red' : 'yellow'}>
                      {item.operation}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Badge color={item.entityType === 'ALL PRODUCTS' ? 'orange' : 'emerald'}>
                      {item.entityType}
                    </Badge>
                  </TableCell>
                </TableRow>
              ) : null
            ))}
          </TableBody>
        </Table>
      </Card>
    );
  }
}

export default TableStats;