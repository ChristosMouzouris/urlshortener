import React, { useState } from 'react';
import {
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from 'recharts';

interface ChartDisplayProps {
  labels: string[];
  data: bigint[];
  title?: string;
  defaultType?: 'bar' | 'pie';
  allowToggle?: boolean;
}

const COLOURS = ['#FF8904', '#3B82F6', '#10B981', '#FBBF24', '#6366F1'];

export const ChartDisplay: React.FC<ChartDisplayProps> = ({
  labels,
  data,
  title,
  defaultType = 'bar',
  allowToggle = false,
}) => {
  const [chartType, setChartType] = useState<'bar' | 'pie'>(defaultType);

  const chartData = labels.map((label, index) => ({
    name: label,
    value: data[index],
  }));

  return (
    <div className="p-4 rounded-2xl shadow-sm bg-gray-900">
      <div className="flex justify-between items-center mb-4">
        {title && <h3 className="text-lg font-semibold">{title}</h3>}
        {allowToggle && (
          <button
            onClick={() =>
              setChartType((prev) => (prev === 'bar' ? 'pie' : 'bar'))
            }
            className="px-3 py-1 text-sm bg-gray-800 hover:bg-gray-700 rounded-md transition"
          >
            Switch to {chartType === 'bar' ? 'Pie' : 'Bar'}
          </button>
        )}
      </div>

      <ResponsiveContainer width="100%" height={250}>
        {chartType === 'pie' ? (
          <PieChart>
            <Pie
              data={chartData}
              dataKey="value"
              nameKey="name"
              outerRadius={100}
              label
            >
              {chartData.map((_, i) => (
                <Cell key={i} fill={COLOURS[i % COLOURS.length]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        ) : (
          <BarChart data={chartData}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="value" fill="#FF8904" radius={[6, 6, 0, 0]} />
          </BarChart>
        )}
      </ResponsiveContainer>
    </div>
  );
};
