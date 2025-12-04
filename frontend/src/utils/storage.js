// utils/storage.js

import { TOKEN_KEYS } from "./constants";

// Save tokens only
export const saveAuthData = (accessToken, refreshToken) => {
  if (accessToken) localStorage.setItem(TOKEN_KEYS.ACCESS, accessToken);
  if (refreshToken) localStorage.setItem(TOKEN_KEYS.REFRESH, refreshToken);
};

// Getters
export const getAccessToken = () => localStorage.getItem(TOKEN_KEYS.ACCESS);
export const getRefreshToken = () => localStorage.getItem(TOKEN_KEYS.REFRESH);

// Clear everything
export const clearAuthData = () => {
  Object.values(TOKEN_KEYS).forEach((key) => localStorage.removeItem(key));
};
