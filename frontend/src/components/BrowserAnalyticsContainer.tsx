import { getClicksByBrowser } from '../services/api.ts';
import { useFetch } from '../hooks/useFetch.ts';
import { ChartDisplay } from './ChartDisplay.tsx';
import type { BrowserResponse } from '../types/browserResponse.ts';
import { useEffect, useState } from 'react';
import { SkeletonCard } from './SkeletonCard.tsx';

export const BrowserAnalyticsContainer = () => {
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
    BrowserResponse[],
    [string | undefined]
  >(getClicksByBrowser, args);

  return (
    <div className="w-full max-w-6xl mx-auto mt-20 p-6 bg-white/[0.025] border border-white/10 rounded-xl backdrop-blur-md">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-orange-500 text-xl font-bold">Clicks by Browser</h2>
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
          labels={data.map((b) => b.browser)}
          data={data.map((b) => b.clicks)}
          defaultType="pie"
          allowToggle
        />
      ) : (
        <p className="text-center mt-6 text-white/50">No data found.</p>
      )}
    </div>
  );
};
