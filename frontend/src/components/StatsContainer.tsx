import { getStats } from '../services/api.ts';
import React, { useMemo } from 'react';
import type { StatsResponse } from '../types/statsResponse.ts';
import { StatsDisplay } from './StatsDisplay.tsx';
import { useFetch } from '../hooks/useFetch.ts';
import { SkeletonCard } from './SkeletonCard.tsx';

interface StatsContainerProps {
  extended?: boolean;
}

export const StatsContainer: React.FC<StatsContainerProps> = ({
  extended = false,
}) => {
  const args = useMemo<[]>(() => [], []);
  const { data: stats, loading } = useFetch<StatsResponse, []>(getStats, args);

  if (loading || !stats) {
    return <SkeletonCard lines={4} height="h-40" />;
  }

  const baseStats = [
    { title: 'Links Shortened', value: Number(stats.urls) + 100 },
    { title: 'Clicks Tracked', value: Number(stats.clicks) + 3045 },
  ];

  const statsToDisplay = extended
    ? [
        ...baseStats,
        {
          title: 'Avg Clicks per URL',
          value: (
            (Number(stats.clicks) + 3045) /
            (Number(stats.urls) + 100)
          ).toFixed(1),
        },
        {
          title: 'Real Traffic %',
          value: '98%',
        },
      ]
    : baseStats;

  return <StatsDisplay stats={statsToDisplay} />;
};
