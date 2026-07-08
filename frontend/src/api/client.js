/**
 * Thin fetch wrapper around the Spring Boot REST API.
 * Responses are expected to be the backend ApiResponse envelope:
 *   { success, message, data, status, timestamp }
 * We return `.data` directly and throw a typed error on failure.
 */

const BASE = '/api/v1';

export class ApiError extends Error {
  constructor(message, status, payload) {
    super(message);
    this.status = status;
    this.payload = payload;
  }
}

function token() {
  return localStorage.getItem('nova-token');
}

async function request(path, { method = 'GET', body, query, isAuth = true } = {}) {
  let url = `${BASE}${path}`;
  if (query) {
    const params = new URLSearchParams(
      Object.fromEntries(Object.entries(query).filter(([, v]) => v !== undefined && v !== null && v !== ''))
    );
    if (params.toString()) url += `?${params.toString()}`;
  }

  const headers = { Accept: 'application/json' };
  if (body) headers['Content-Type'] = 'application/json';
  if (isAuth && token()) headers['Authorization'] = `Bearer ${token()}`;

  let res;
  try {
    res = await fetch(url, {
      method,
      headers,
      body: body ? JSON.stringify(body) : undefined,
    });
  } catch (e) {
    throw new ApiError('ارتباط با سرور برقرار نشد. مطمئن شو سرویس‌ها در حال اجرا هستند.', 0);
  }

  const text = await res.text();
  const json = text ? JSON.parse(text) : null;

  // Login endpoints may return a token shape instead of ApiResponse.
  if (json && (json.accessToken || json.token)) return json;

  if (!res.ok) {
    const msg = (json && (json.message || json.error)) || `خطای سرور (${res.status})`;
    throw new ApiError(msg, res.status, json);
  }

  // ApiResponse envelope
  if (json && typeof json === 'object' && 'data' in json) return json.data;
  return json;
}

export const api = {
  get: (path, opts) => request(path, { ...opts, method: 'GET' }),
  post: (path, body, opts) => request(path, { ...opts, method: 'POST', body }),
  put: (path, body, opts) => request(path, { ...opts, method: 'PUT', body }),
  patch: (path, body, opts) => request(path, { ...opts, method: 'PATCH', body }),
  delete: (path, opts) => request(path, { ...opts, method: 'DELETE' }),
};
