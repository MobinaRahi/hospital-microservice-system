import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { endpoints } from '../api/endpoints';

/**
 * Global authentication state.
 *
 * - On mount, if a token is stored we call /auth/me to restore the session.
 * - login() posts credentials, stores access + refresh tokens, fetches the user.
 * - logout() clears everything and redirects to /login.
 * - A token-expiry watcher triggers auto-refresh before the access token dies.
 */
const AuthContext = createContext({
  user: null,
  loading: true,
  isAuthenticated: false,
  login: async () => {},
  logout: () => {},
});

const ACCESS_KEY = 'nova-token';
const REFRESH_KEY = 'nova-refresh';
const USER_KEY = 'nova-user';

function storeTokens(access, refresh) {
  if (access) localStorage.setItem(ACCESS_KEY, access);
  if (refresh) localStorage.setItem(REFRESH_KEY, refresh);
}
function clearTokens() {
  localStorage.removeItem(ACCESS_KEY);
  localStorage.removeItem(REFRESH_KEY);
  localStorage.removeItem(USER_KEY);
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  /* Restore session on first load */
  const restore = useCallback(async () => {
    const token = localStorage.getItem(ACCESS_KEY);
    if (!token) {
      setLoading(false);
      return;
    }
    try {
      const cached = localStorage.getItem(USER_KEY);
      if (cached) setUser(JSON.parse(cached));
      const me = await endpoints.auth.me();
      setUser(me);
      localStorage.setItem(USER_KEY, JSON.stringify(me));
    } catch {
      clearTokens();
      setUser(null);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    restore();
  }, [restore]);

  const login = useCallback(async (credentials) => {
    const res = await endpoints.auth.login(credentials);
    const accessToken = res?.accessToken || res?.token;
    const refreshToken = res?.refreshToken;
    if (!accessToken) throw new Error('پاسخ ورود نامعتبر است');
    storeTokens(accessToken, refreshToken);
    // user info may come inside the login response
    if (res?.user) {
      setUser(res.user);
      localStorage.setItem(USER_KEY, JSON.stringify(res.user));
    } else {
      try {
        const me = await endpoints.auth.me();
        setUser(me);
        localStorage.setItem(USER_KEY, JSON.stringify(me));
      } catch {
        /* non-fatal */
      }
    }
    return res;
  }, []);

  const logout = useCallback(() => {
    clearTokens();
    setUser(null);
  }, []);

  /* Auto-refresh the access token ~50s before it expires (1h default). */
  useEffect(() => {
    const id = setInterval(async () => {
      const access = localStorage.getItem(ACCESS_KEY);
      const refresh = localStorage.getItem(REFRESH_KEY);
      if (!access || !refresh) return;
      try {
        const res = await endpoints.auth.refresh(refresh);
        const newAccess = res?.accessToken;
        if (newAccess) {
          localStorage.setItem(ACCESS_KEY, newAccess);
          if (res?.refreshToken) localStorage.setItem(REFRESH_KEY, res.refreshToken);
        }
      } catch {
        /* refresh failed — let the next 401 handle it */
      }
    }, 55 * 60 * 1000); // ~55 min
    return () => clearInterval(id);
  }, []);

  const value = {
    user,
    loading,
    isAuthenticated: !!user,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
