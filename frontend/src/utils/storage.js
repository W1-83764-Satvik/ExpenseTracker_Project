// utils/storage.js

import { TOKEN_KEYS } from "./constants";

// --- Save Tokens & User Data ---
export const saveAuthData = (accessToken, refreshToken, user) => {
  if (accessToken) localStorage.setItem(TOKEN_KEYS.ACCESS, accessToken);
  if (refreshToken) localStorage.setItem(TOKEN_KEYS.REFRESH, refreshToken);
  if (user) localStorage.setItem(TOKEN_KEYS.USER, JSON.stringify(user));
};

// --- Getters ---
export const getAccessToken = () => localStorage.getItem(TOKEN_KEYS.ACCESS);
export const getRefreshToken = () => localStorage.getItem(TOKEN_KEYS.REFRESH);
export const getUserData = () => {
  const raw = localStorage.getItem(TOKEN_KEYS.USER);
  return raw ? JSON.parse(raw) : null;
};

// --- Clear Auth Data ---
export const clearAuthData = () => {
  Object.values(TOKEN_KEYS).forEach((key) => localStorage.removeItem(key));
};
