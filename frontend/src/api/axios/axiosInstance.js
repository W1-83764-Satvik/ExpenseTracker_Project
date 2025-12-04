// api/axios/axiosInstance.js
import apiClient from "./apiClient";
import { getAccessToken, getRefreshToken, saveAuthData, clearAuthData } from "../../utils/storage";
import { AUTH_ENDPOINTS } from "../../utils/constants";

let isRefreshing = false;
let refreshSubscribers = [];

/** Push a callback to be called once a new token is available */
const subscribeTokenRefresh = (callback) => {
  refreshSubscribers.push(callback);
};

/** Call all queued callbacks with the new token */
const onTokenRefreshed = (newToken) => {
  refreshSubscribers.forEach((cb) => cb(newToken));
  refreshSubscribers = [];
};

// Attach access token to every request
apiClient.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers = config.headers || {};
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor that refreshes token on 401
apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // If no response or no status, forward error
    if (!error.response) return Promise.reject(error);

    // Prevent trying to refresh if the failing request is the refresh endpoint itself
    const isRefreshEndpoint = originalRequest?.url?.includes(AUTH_ENDPOINTS.REFRESH);

    // Only attempt refresh on 401, skip if we already retried or if it's the refresh request
    if (error.response.status === 401 && !originalRequest._retry && !isRefreshEndpoint) {
      const refreshToken = getRefreshToken();
      if (!refreshToken) {
        clearAuthData();
        window.location.href = "/login";
        return Promise.reject(error);
      }

      // If a refresh is already in progress, queue the original request and wait
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          subscribeTokenRefresh((newToken) => {
            if (!newToken) return reject(error);
            originalRequest.headers = originalRequest.headers || {};
            originalRequest.headers.Authorization = `Bearer ${newToken}`;
            resolve(apiClient(originalRequest));
          });
        });
      }

      // Start refresh flow
      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // Call refresh endpoint (note: this call will use the request interceptor but
        // it is safe because refresh endpoint should accept refresh token in body)
        const refreshResp = await apiClient.post(AUTH_ENDPOINTS.REFRESH, { refreshToken });

        // defensive read of nested data
        const data = refreshResp?.data?.data || {};
        const newAccessToken = data.accessToken || data.access_token || data.access;
        const newRefreshToken = data.refreshToken || data.refresh_token || data.refresh;

        if (!newAccessToken || !newRefreshToken) {
          // If refresh did not return tokens, clear auth and redirect
          clearAuthData();
          window.location.href = "/login";
          return Promise.reject(new Error("Refresh endpoint did not return tokens"));
        }

        // Persist tokens (storage.saveAuthData should accept accessToken, refreshToken)
        saveAuthData(newAccessToken, newRefreshToken);

        // Set default header for future requests
        apiClient.defaults.headers = apiClient.defaults.headers || {};
        apiClient.defaults.headers.common = apiClient.defaults.headers.common || {};
        apiClient.defaults.headers.common.Authorization = `Bearer ${newAccessToken}`;

        // Notify queued requests
        onTokenRefreshed(newAccessToken);

        // Retry the original request with new token
        originalRequest.headers = originalRequest.headers || {};
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

        return apiClient(originalRequest);
      } catch (refreshError) {
        // Refresh failed: clear everything and redirect to login
        clearAuthData();
        window.location.href = "/login";
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;
