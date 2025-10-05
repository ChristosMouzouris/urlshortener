import { useEffect, useState } from 'react';
import { getTopUrls } from '../services/api.ts';
import type { TopUrlsResponse } from '../types/topUrlsResponse.ts';
import DataTable from './DataTable.tsx';
import { NotificationEnum } from '../types/notificationEnum.ts';
import { useNotification } from './NotificationContext.tsx';
import type { ApiError } from '../services/fetchWrapper.ts';

const TopUrls = () => {
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [topUrls, setTopUrls] = useState<TopUrlsResponse[] | null>(null);
  const { addNotification } = useNotification();

  useEffect(() => {
    const handleGetTopUrls = async () => {
      setLoading(true);
      try {
        const data: TopUrlsResponse[] = await getTopUrls();
        setTopUrls(data);
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

    void handleGetTopUrls();
  }, []);

  useEffect(() => {
    if (error) {
      addNotification(error, NotificationEnum.fail);
    }
    setError('');
  }, [error, addNotification]);

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

export default TopUrls;
