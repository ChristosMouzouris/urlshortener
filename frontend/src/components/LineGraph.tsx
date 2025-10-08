import { Line } from 'react-chartjs-2';
import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

interface LineGraphProps {
  labels: string[];
  data: number[];
  title?: string;
  lineColor?: string;
  height?: number;
}

export const LineGraph: React.FC<LineGraphProps> = ({
  labels,
  data,
  title,
  lineColor = '#FF8904',
  height = 300,
}) => {
  const chartData = {
    labels,
    datasets: [
      {
        label: title,
        data,
        borderColor: lineColor,
        backgroundColor: 'rgba(255, 137, 4, 0.2)',
        fill: true,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      y: { beginAtZero: true },
    },
  };

  return (
    <div style={{ height }}>
      <Line key={data.length} data={chartData} options={options} />
    </div>
  );
};
