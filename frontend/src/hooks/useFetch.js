import { useEffect, useState, useCallback } from 'react';
import { ApiError } from '../api/client';

/**
 * Simple data-fetching hook with loading/error/refetch.
 * Falls back gracefully when the backend is offline (demo mode).
 */
export function useFetch(fetcher, deps = []) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const run = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await fetcher();
      setData(result);
    } catch (e) {
      setError(e instanceof ApiError ? e.message : 'خطای ناشناخته');
      setData(null);
    } finally {
      setLoading(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);

  useEffect(() => { run(); }, [run]);

  return { data, loading, error, refetch: run, setData };
}
