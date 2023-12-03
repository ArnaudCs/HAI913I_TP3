import React from 'react';
import { Card, Metric, Text } from "@tremor/react";

class MetricCard extends React.Component {
  render() {
    const { title, metricLabel, metricValue, decorationColor,
      title2, metricLabel2, metricValue2, decorationColor2} = this.props;

    return (
      <div className={`bg-dark-third p-5 rounded-lg text-white shadow-2xl flex justify-evenly flex-col`}>
        <div className='mb-4 p-2 bg-dark-secondary rounded-lg'>
          <p className='text-xl font-sans font-semibold'>{title}</p>
        </div>

        <Card className="w-full" decoration="bottom" borderRadius='30' decorationColor={decorationColor}>
          <Text>{metricLabel}</Text>
          <Metric>{metricValue}</Metric>
        </Card>

        <div className='mb-4 p-2 bg-dark-secondary rounded-lg mt-3'>
          <p className='text-xl font-sans font-semibold'>{title2}</p>
        </div>

        <Card className="w-full" decoration="bottom" borderRadius='30' decorationColor={decorationColor2}>
          <Text>{metricLabel2}</Text>
          <Metric>{metricValue2}</Metric>
        </Card>
      </div>
    );
  }
}

export default MetricCard;
