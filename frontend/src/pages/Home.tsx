import About from './About.tsx';
import InputBar from '../components/InputBar.tsx';
import { useState } from 'react';
import type { UrlResponse } from '../types/urlResponse.ts';
import FrostedCard from '../components/FrostedCard.tsx';
import { getErrorMessage } from '../utilities/utilities.ts';
import { getShortCode } from '../services/api.ts';

function Home() {
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleInput = async (url: string) => {
    if (loading) return;
    setLoading(true);

    try {
      const shortCode: UrlResponse = await getShortCode(url);
      console.log(shortCode);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mt-10">
      <div className="flex items-center justify-center">
        <InputBar onInput={handleInput} placeholder="Enter a long url..." />
      </div>
      {error ? <FrostedCard text={error} /> : ''}
      {loading ? (
        <FrostedCard text="Loading" /> // Display a spinner actually
      ) : (
        '' // This should be displaying nothing
      )}
      <div id="about">
        <About />
      </div>
    </div>
  );
}

export default Home;
