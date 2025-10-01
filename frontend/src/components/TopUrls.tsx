import { useEffect, useState } from 'react';
import { getTopUrls } from '../services/api.ts';
import { getErrorMessage } from '../utilities/utilities.ts';
import type { TopUrlsResponse } from '../types/topUrlsResponse.ts';
import DataTable from './DataTable.tsx';

const TopUrls = () => {
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [topUrls, setTopUrls] = useState<TopUrlsResponse[] | null>(null);

  useEffect(() => {
    const handleGetTopUrls = async () => {
      setLoading(true);
      try {
        const data: TopUrlsResponse[] = await getTopUrls();
        setTopUrls(data);
        console.log(data);
      } catch (err) {
        const message: string = getErrorMessage(err);
        setError(message);
        console.log(message);
      } finally {
        setLoading(false);
      }
    };

    void handleGetTopUrls();
  }, []);

  if (error) return <p>Error...</p>;
  if (!topUrls) return null;

  const headers = ['Short Code', 'Clicks'];
  const rows = topUrls.map((row) => [row.shortUrl, row.clicks]);

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

export default TopUrls;
