import About from './About.tsx';
import InputBar from '../components/InputBar.tsx';
import { useState, useEffect } from 'react';
import type { UrlResponse } from '../types/urlResponse.ts';
import FrostedCard from '../components/FrostedCard.tsx';
import { getShortCode } from '../services/api.ts';
import Stats from '../components/Stats.tsx';
import TopUrls from '../components/TopUrls.tsx';
import { NotificationEnum } from '../types/notificationEnum.ts';
import { useNotification } from '../components/NotificationContext.tsx';
import type { ApiError } from '../services/fetchWrapper.ts';

function Home() {
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { addNotification } = useNotification();

  const handleInput = async (url: string) => {
    if (loading) return;
    setLoading(true);

    try {
      const shortCode: UrlResponse = await getShortCode(url);
      console.log(shortCode);
    } catch (err) {
      const apiError = err as ApiError;
      if (apiError.kind === 'network') {
        setError('Network error. Please check your connection.');
      } else if (apiError.kind === 'server') {
        setError(apiError.message);
      } else {
        setError('Unexpected error occurred.');
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (error) {
      addNotification(error, NotificationEnum.fail);
      setError('');
    }
  }, [error, addNotification]);

  return (
    <div className="mt-10">
      <div className="flex flex-col items-center justify-center text-center space-y-3">
        <p className="font-bold text-5xl text-orange-500">
          Shorten your links! Track your impact!
        </p>
        <p>
          Quick, easy and lightweight service to create short codes for long
          URLs.
        </p>
        <p>Track analytics for each short code.</p>
      </div>
      <div className="flex items-center justify-center mt-20">
        <InputBar onInput={handleInput} placeholder="Enter a long URL..." />
      </div>
      {loading ? (
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
      ) : null}
      <div className="max-w-6xl mx-auto mt-20 px-4 flex flex-wrap justify-center gap-6">
        <FrostedCard
          heroText="⚡"
          heroTitle="Fast Shortening"
          description="Shorten your URLs in seconds."
        />

        <FrostedCard
          heroText="📈"
          heroTitle="Analytics"
          description="Track clicks for each short code."
        />

        <FrostedCard
          heroText="🤖"
          heroTitle="Filter BOTS"
          description="Shorten your URLs in seconds."
        />

        <FrostedCard
          heroText="⚡"
          heroTitle="Redirect"
          description="Lightining fast redirects."
        />
      </div>
      <div>
        <Stats />
      </div>
      <div>
        <TopUrls />
      </div>
      <div id="about">
        <About />
      </div>
    </div>
  );
}

export default Home;
