import type { UrlResponse } from '../types/urlResponse.ts';
import type { ErrorResponse } from '../types/errorResponse.ts';

export const getShortCode = async (longUrl: string) => {
  const result = await fetch(`/api/url`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ longUrl }),
  });

  if (!result.ok) {
    const errorData: ErrorResponse = await result.json();
    console.log(errorData);
    throw new Error(errorData.message || 'Unable to get short code.');
  }

  const data: UrlResponse = await result.json();

  return data;
};

export const getTopUrls = async () => {
  const result = await fetch(`/api/analytics/top-urls`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
  });

  if (!result.ok) {
    const errorData: ErrorResponse = await result.json();
    console.log(errorData);
    throw new Error(errorData.message || 'Unable to get top urls.');
  }
};
