import { useMemo, useState } from 'react';
import { getShortCode } from '../services/api.ts';
import { useFetch } from '../hooks/useFetch.ts';
import type { UrlResponse } from '../types/urlResponse.ts';
import InputBar from './InputBar.tsx';
import { useNotification } from './NotificationContext.tsx';
import { NotificationEnum } from '../types/notificationEnum.ts';

export const UrlShortener = () => {
  const [submittedUrl, setSubmittedUrl] = useState<string>('');
  const { addNotification } = useNotification();
  const BASE_URL = 'http://localhost:8080';

  const args = useMemo<[string]>(() => [submittedUrl], [submittedUrl]);

  const {
    data: urlResponse,
    loading,
    error,
  } = useFetch<UrlResponse, [string]>(getShortCode, args, true, !!submittedUrl);

  const handleInput = async (url: string) => {
    if (!url) {
      addNotification('Please enter a valid URL.', NotificationEnum.fail);
      return;
    }
    setSubmittedUrl(url);
  };

  return (
    <div>
      <div className="flex items-center justify-center mt-20">
        <InputBar onInput={handleInput} placeholder="Enter a long URL..." />
      </div>
      {loading || error ? (
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
      ) : urlResponse ? (
        <div className="flex items-center justify-center">
          <div className="flex flex-col items-center w-[300px] rounded-2xl bg-white/[0.025] backdrop-blur-md border border-white/10 shadow-lg shadow-black/50 mt-4 p-6">
            <p className="font-bold text-xl">Your short link:</p>
            <a
              href={`${BASE_URL}/${urlResponse?.shortUrl}`}
              target="_blank"
              rel="noopener noreferrer"
              className="text-orange-500 font-semibold hover:underline"
            >
              {BASE_URL}/{urlResponse?.shortUrl}
            </a>
          </div>
        </div>
      ) : null}
    </div>
  );
};
