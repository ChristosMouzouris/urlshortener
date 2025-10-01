import { getStats } from '../services/api.ts';
import { getErrorMessage } from '../utilities/utilities.ts';
import { useState, useEffect } from 'react';
import type { StatsResponse } from '../types/statsResponse.ts';
import FrostedCard from './FrostedCard.tsx';

const Stats = () => {
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [stats, setStats] = useState<StatsResponse | null>(null);

  useEffect(() => {
    const handleGetTotalClicks = async () => {
      setLoading(true);
      try {
        const data: StatsResponse = await getStats();
        setStats(data);
      } catch (err) {
        const message: string = getErrorMessage(err);
        setError(message);
        console.log(message);
      } finally {
        setLoading(false);
      }
    };

    void handleGetTotalClicks();
  }, []);

  if (loading) return <p>This component is loading...</p>;
  if (error) return <p>Error...</p>;
  if (!stats) return null;

  return (
    <div className="max-w-3xl mx-auto mt-20 px-4 text-center relative">
      <h1 className="text-orange-500 text-3xl font-bold mb-8 inline-flex items-center justify-center">
        Live Stats
        <span className="relative ml-2 group cursor-pointer">
          <span className="text-sm text-orange-500 font-bold">ⓘ</span>
          <span className="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 w-48 bg-black text-white text-sm rounded-md p-2 opacity-0 group-hover:opacity-100 transition-opacity pointer-events-none z-10">
            These stats have been padded for demo purposes.
          </span>
        </span>
      </h1>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 justify-items-center">
        <FrostedCard
          heroText={(Number(stats.urls) + 100).toString()}
          heroTitle="Links Shortened"
          description=""
        />
        <FrostedCard
          heroText={(Number(stats.clicks) + 3045).toString()}
          heroTitle="Clicks Tracked"
          description=""
        />
      </div>
    </div>
  );
};

export default Stats;
