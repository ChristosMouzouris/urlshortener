import { getStats } from '../services/api.ts';
import { useState, useEffect } from 'react';
import type { StatsResponse } from '../types/statsResponse.ts';
import FrostedCard from './FrostedCard.tsx';
import { NotificationEnum } from '../types/notificationEnum.ts';
import { useNotification } from './NotificationContext.tsx';
import type { ApiError } from '../services/fetchWrapper.ts';

const Stats = () => {
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [stats, setStats] = useState<StatsResponse | null>(null);
  const { addNotification } = useNotification();

  useEffect(() => {
    const handleGetTotalClicks = async () => {
      setLoading(true);
      try {
        const data: StatsResponse = await getStats();
        setStats(data);
      } catch (err) {
        const apiError = err as ApiError;
        if (apiError.kind === 'network') {
          setError('Network error. Please check your connection.');
        } else if (apiError.kind === 'server') {
          setError(apiError.message);
        } else {
          setError('Unexpected error occurred.');
        }
        console.log(err);
      } finally {
        setLoading(false);
      }
    };

    void handleGetTotalClicks();
  }, []);

  useEffect(() => {
    if (error) {
      addNotification(error, NotificationEnum.fail);
    }
    setError('');
  }, [error, addNotification]);

  if (!stats) return null;

  return loading ? (
    <div className="flex items-center justify-center">
      <svg
        className="animate-spin"
        height="50"
        viewBox="0 0 24 24"
        width="50"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M2 12C2 6.47715 6.47715 2 12 2V5C8.13401 5 5 8.13401 5 12H2Z"
          fill="#FF8904"
        />
      </svg>
    </div>
  ) : (
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
