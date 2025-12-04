// utils/constants.js

// --- Base URL ---
export const BASE_URL = "http://localhost:7777";

// --- API Endpoints ---
export const AUTH_ENDPOINTS = {
  SIGNUP: "/auth/v1/signup",
  SIGNIN: "/auth/v1/signin",
  LOGOUT: "/auth/v1/logout",
  ME: "/auth/v1/me",
  REFRESH: "/jwt/v1/refresh",
  VALIDATE: "/jwt/v1/validate-token",
};

// --- Token Storage Keys ---
export const TOKEN_KEYS = {
  ACCESS: "access_token",
  REFRESH: "refresh_token",
  USER: "user"
};

// --- Token Expiry Buffer (in ms) ---
// Used to refresh token slightly before real expiry
export const TOKEN_REFRESH_BUFFER = 60 * 1000; // 1 min before expiry

// --- Other Constants ---
export const REQUEST_TIMEOUT = 10000; // 10s axios timeout
