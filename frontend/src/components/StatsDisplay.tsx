import FrostedCard from './FrostedCard.tsx';
import React from 'react';

interface Stat {
  title: string;
  value: string | number;
}

interface StatDisplayProps {
  stats: Stat[];
}

export const StatsDisplay: React.FC<StatDisplayProps> = ({ stats }) => {
  return (
    <div className="max-w-3xl mx-auto mt-20 px-4 text-center relative">
      <h1 className="text-orange-500 text-3xl font-bold mb-8">Live Stats</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 justify-items-center">
        {stats.map((s, i) => (
          <FrostedCard
            key={i}
            heroText={s.value.toString()}
            heroTitle={s.title}
            description=""
          />
        ))}
      </div>
    </div>
  );
};
