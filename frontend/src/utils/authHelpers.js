// utils/authHelpers.js

import jwtDecode from "jwt-decode";
import { getAccessToken } from "./storage";
import { TOKEN_REFRESH_BUFFER } from "./constants";

// --- Decode JWT payload safely ---
export const decodeToken = (token) => {
  try {
    return jwtDecode(token);
  } catch {
    return null;
  }
};

// --- Check if token is expired (with buffer) ---
export const isTokenExpired = (token) => {
  const decoded = decodeToken(token);
  if (!decoded?.exp) return true;

  const expiryTime = decoded.exp * 1000; // exp is in seconds → convert to ms
  const now = Date.now();

  // consider token "expired" if within buffer range
  return expiryTime - now < TOKEN_REFRESH_BUFFER;
};

// --- Extract user info from token (fallback to stored one) ---
export const getUserFromToken = () => {
  const token = getAccessToken();
  if (!token) return null;

  const decoded = decodeToken(token);
  return decoded?.sub || decoded?.username || decoded?.email || null;
};
