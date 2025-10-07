import { useEffect, useState } from 'react';
import { useFetch } from '../hooks/useFetch.ts';
import { getClicksByCountry } from '../services/api.ts';
import type { CountryResponse } from '../types/countryResponse.ts';
import { SkeletonCard } from './SkeletonCard.tsx';
import { ChartDisplay } from './ChartDisplay.tsx';

export const CountryAnalytics = () => {
  const [shortUrl, setShortUrl] = useState('');
  const [debouncedShortUrl, setDebouncedShortUrl] = useState<
    string | undefined
  >(undefined);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedShortUrl(shortUrl.trim() || undefined);
    }, 500);

    return () => clearTimeout(handler);
  }, [shortUrl]);

  const args: [string | undefined] = [debouncedShortUrl];

  const { data, loading, error } = useFetch<
    CountryResponse[],
    [string | undefined]
  >(getClicksByCountry, args);

  return (
    <div className="w-full max-w-6xl mx-auto mt-20 p-6 bg-white/[0.025] border border-white/10 rounded-xl backdrop-blur-md">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-orange-500 text-xl font-bold">Clicks by Country</h2>
        <input
          type="text"
          placeholder="Filter by short code..."
          value={shortUrl}
          onChange={(e) => setShortUrl(e.target.value)}
          className="px-3 py-2 text-sm rounded-lg bg-white/5 border border-white/10 focus:outline-none focus:ring-1 focus:ring-orange-500 text-white placeholder-white/50"
        />
      </div>

      {loading || error ? (
        <SkeletonCard lines={4} height="h-40" />
      ) : data && data.length > 0 ? (
        <ChartDisplay
          title=""
          labels={data.map((b) => b.country)}
          data={data.map((b) => b.clicks)}
          defaultType="bar"
          allowToggle
        />
      ) : (
        <p className="text-center mt-6 text-white/50">No data found.</p>
      )}
    </div>
  );
};
