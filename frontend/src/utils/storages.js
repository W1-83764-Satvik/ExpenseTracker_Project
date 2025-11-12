// utils/storage.js

import { TOKEN_KEYS } from "./constants";

// --- Save Tokens & User Data ---
export const saveAuthData = (token, refreshToken, user) => {
  localStorage.setItem(TOKEN_KEYS.ACCESS, token);
  localStorage.setItem(TOKEN_KEYS.REFRESH, refreshToken);
  if (user) {
    localStorage.setItem(TOKEN_KEYS.USER, JSON.stringify(user));
  }
};

// --- Getters ---
export const getAccessToken = () => localStorage.getItem(TOKEN_KEYS.ACCESS);
export const getRefreshToken = () => localStorage.getItem(TOKEN_KEYS.REFRESH);
export const getUserData = () => {
  const data = localStorage.getItem(TOKEN_KEYS.USER);
  return data ? JSON.parse(data) : null;
};

// --- Clear Auth Data ---
export const clearAuthData = () => {
  localStorage.removeItem(TOKEN_KEYS.ACCESS);
  localStorage.removeItem(TOKEN_KEYS.REFRESH);
  localStorage.removeItem(TOKEN_KEYS.USER);
};
