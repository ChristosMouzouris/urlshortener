import React, { useMemo } from 'react';
import { getTopUrls } from '../services/api.ts';
import type { TopUrlsResponse } from '../types/topUrlsResponse.ts';
import DataTable from './DataTable.tsx';
import { useFetch } from '../hooks/useFetch.ts';

interface TopUrlsProps {
  limit?: number;
}

export const TopUrlsContainer: React.FC<TopUrlsProps> = ({ limit = 10 }) => {
  const args = useMemo<[number]>(() => [limit], [limit]);

  const { data: topUrls, loading } = useFetch<TopUrlsResponse[], [number]>(
    getTopUrls,
    args
  );

  if (!topUrls) return null;

  const headers = ['Short Code', 'Clicks', 'Domain'];
  const rows = topUrls.map((row) => [row.shortUrl, row.clicks, row.domain]);

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
    <DataTable headers={headers} rows={rows} />
  );
};
