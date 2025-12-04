// utils/authHelpers.js
import {jwtDecode} from "jwt-decode";
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

// --- Check if token is expired (returns true if expired or within buffer) ---
export const isTokenExpired = (token) => {
  if (!token) return true;
  const decoded = decodeToken(token);
  if (!decoded || !decoded.exp) return true;

  const expiryTime = decoded.exp * 1000; // exp in seconds -> ms
  const now = Date.now();

  // token is considered "expired" if expiryTime is already passed or within buffer
  return expiryTime - now < TOKEN_REFRESH_BUFFER;
};

// --- Extract user info from token (fallback to null) ---
export const getUserFromToken = () => {
  const token = getAccessToken();
  if (!token) return null;

  const decoded = decodeToken(token);
  // adjust to how you put subject in token; common fields: sub, email, username
  return decoded?.sub || decoded?.username || decoded?.email || null;
};
