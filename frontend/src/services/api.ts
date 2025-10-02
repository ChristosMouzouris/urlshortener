import type { UrlResponse } from '../types/urlResponse.ts';
import type { TopUrlsResponse } from '../types/topUrlsResponse.ts';
import type { StatsResponse } from '../types/statsResponse.ts';
import { fetchWrapper } from './fetchWrapper.ts';

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
