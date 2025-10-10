import { useState, useEffect, useRef } from 'react';
import type { ApiError } from '../services/fetchWrapper';
import { NotificationEnum } from '../types/notificationEnum';
import { useNotification } from '../components/NotificationContext';

export function useFetch<T, A extends unknown[]>(
  fetchFn: (...args: A) => Promise<T>,
  args: A,
  notifyError: boolean = true,
  enabled: boolean = true
) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>('');

  const { addNotification } = useNotification();

  const argsKey = JSON.stringify(args);
  const argsKeyRef = useRef<string>('');

  const hasFailedRef = useRef(false);
  const fetchIdRef = useRef(0);

  useEffect(() => {
    if (!enabled) return;

    if (
      hasFailedRef.current &&
      fetchIdRef.current &&
      argsKey === argsKeyRef.current
    )
      return;

    hasFailedRef.current = false;
    argsKeyRef.current = argsKey;

    const fetchId = ++fetchIdRef.current;
    let cancelled = false;

    const fetchData = async () => {
      setLoading(true);
      setError('');

      try {
        const result: T = await fetchFn(...args);
        if (!cancelled && fetchId === fetchIdRef.current) setData(result);
      } catch (err) {
        if (cancelled) return;
        hasFailedRef.current = true;
        const apiError = err as ApiError;
        let msg: string;
        switch (apiError.kind) {
          case 'network':
            msg = 'Network error. Please check your connection.';
            break;
          case 'server':
            msg = `Server error: ${apiError.message}`;
            break;
          default:
            msg = 'Unexpected error occurred.';
        }
        setError(msg);
        if (notifyError) addNotification(msg, NotificationEnum.fail);
        console.error(err);
      } finally {
        if (!cancelled && fetchId === fetchIdRef.current) setLoading(false);
      }
    };
    void fetchData();

    return () => {
      cancelled = true;
    };
  }, [fetchFn, argsKey, addNotification, notifyError]);

  return { data, loading, error };
}
