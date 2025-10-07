import type { UrlResponse } from '../types/urlResponse.ts';
import type { TopUrlsResponse } from '../types/topUrlsResponse.ts';
import type { StatsResponse } from '../types/statsResponse.ts';
import type { BrowserResponse } from '../types/browserResponse.ts';
import { fetchWrapper } from './fetchWrapper.ts';
import type { CountryResponse } from '../types/countryResponse.ts';

export const getShortCode = async (longUrl: string) =>
  fetchWrapper<UrlResponse>(`/api/url`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ longUrl }),
  });

export const getTopUrls = async () =>
  fetchWrapper<TopUrlsResponse[]>(`/api/analytics/top-urls`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
  });

export const getStats = async () =>
  fetchWrapper<StatsResponse>('/api/analytics/stats', {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
  });

export const getClicksByBrowser = async (shortUrl?: string) => {
  const endpoint = shortUrl
    ? `/api/analytics/${shortUrl}/by-browser`
    : '/api/analytics/by-browser';

  return fetchWrapper<BrowserResponse[]>(endpoint, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
  });
};

export const getClicksByCountry = async (shortUrl?: string) => {
  const endpoint = shortUrl
    ? `/api/analytics/${shortUrl}/by-location`
    : '/api/analytics/by-location';

  return fetchWrapper<CountryResponse[]>(endpoint, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
  });
};
