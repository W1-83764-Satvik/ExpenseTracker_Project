import jwtDecode from 'jwt-decode';
import { ACCESS_TOKEN_KEY } from './constants';
import { getItem } from './storage';

// Decode JWT and extract payload
export const decodeToken = (token) => {
  try {
    return jwtDecode(token);
  } catch {
    return null;
  }
};

// Check if token is expired
export const isTokenExpired = (token) => {
  const decoded = decodeToken(token);
  if (!decoded || !decoded.exp) return true;

  const now = Date.now() / 1000;
  return decoded.exp < now;
};

// Get user details from token (fallback to stored one)
export const getUserFromToken = () => {
  const token = getItem(ACCESS_TOKEN_KEY);
  if (!token) return null;

  const decoded = decodeToken(token);
  return decoded ? decoded.sub || decoded.username || null : null;
};
