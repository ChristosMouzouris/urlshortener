import React, { useMemo } from 'react';
import { useFetch } from '../hooks/useFetch.ts';
import { getClicksTrend } from '../services/api.ts';
import type { ClicksTrendResponse } from '../types/clicksTrendResponse.ts';
import { SkeletonCard } from './SkeletonCard.tsx';
import { LineGraph } from './LineGraph.tsx';

interface ClicksTrendContainerProps {
  days?: number;
}

export const ClicksTrendContainer: React.FC<ClicksTrendContainerProps> = ({
  days,
}) => {
  const args = useMemo<[number | undefined]>(() => [days], [days]);

  const { data, loading, error } = useFetch<
    ClicksTrendResponse[],
    [number | undefined]
  >(getClicksTrend, args);

  const labels = useMemo<string[]>(() => {
    if (!data) return [];
    return data.map((d) =>
      new Date(d.date).toLocaleDateString('en-GB', {
        day: '2-digit',
        month: 'short',
      })
    );
  }, [data]);

  const values = useMemo<number[]>(() => {
    if (!data) return [];
    return data.map((d) => Number(d.clicks));
  }, [data]);

  return (
    <div className="w-full max-w-6xl mx-auto mt-20 p-6 bg-white/[0.025] border border-white/10 rounded-xl backdrop-blur-md">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-orange-500 text-xl font-bold">Clicks Trend</h2>
      </div>

      {loading || error ? (
        <SkeletonCard lines={4} height="h-40" />
      ) : data && data.length > 0 ? (
        <LineGraph labels={labels} data={values} title="Clicks Trend" />
      ) : (
        <p className="text-center mt-6 text-white/50">No data found.</p>
      )}
    </div>
  );
};
