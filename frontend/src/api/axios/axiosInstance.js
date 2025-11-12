// api/axios/axiosInstance.js
import apiClient from "./apiClient";
import { getAccessToken, getRefreshToken, saveAuthData, clearAuthData } from "../../utils/storages";
import { AUTH_ENDPOINTS } from "../../utils/constants";

let isRefreshing = false;
let refreshSubscribers = [];

const subscribeTokenRefresh = (callback) => {
  refreshSubscribers.push(callback);
};

const onTokenRefreshed = (newToken) => {
  refreshSubscribers.forEach((callback) => callback(newToken));
  refreshSubscribers = [];
};

apiClient.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      const refreshToken = getRefreshToken();
      if (!refreshToken) {
        clearAuthData();
        window.location.href = "/login";
        return Promise.reject(error);
      }

      if (isRefreshing) {
        // Queue the request until new token is ready
        return new Promise((resolve) => {
          subscribeTokenRefresh((newToken) => {
            originalRequest.headers.Authorization = `Bearer ${newToken}`;
            resolve(apiClient(originalRequest));
          });
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const response = await apiClient.post(AUTH_ENDPOINTS.REFRESH, { refreshToken });
        const { accessToken: newToken, refreshToken: newRefresh, user } = response.data.data;

        saveAuthData(newToken, newRefresh, user);
        apiClient.defaults.headers.common.Authorization = `Bearer ${newToken}`;
        onTokenRefreshed(newToken);

        return apiClient(originalRequest);
      } catch (refreshError) {
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
