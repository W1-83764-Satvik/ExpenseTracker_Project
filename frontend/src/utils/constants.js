// utils/constants.js

export const BASE_URL = "http://localhost:7777"; 

// --- API Endpoints ---
export const AUTH_ENDPOINTS = {
  SIGNUP: "/auth/v1/signup",
  SIGNIN: "/auth/v1/signin",
  LOGOUT: "/auth/v1/logout",
  REFRESH: "/jwt/v1/refresh",
  VALIDATE: "/jwt/v1/validate-token",
};

// --- Token Storage Keys ---
export const TOKEN_KEYS = {
  ACCESS: "access_token",
  REFRESH: "refresh_token",
  USER: "user_data",
};

// --- Token Expiry Buffer (in ms) ---
// To refresh 1 minute before actual expiry
export const TOKEN_REFRESH_BUFFER = 60 * 1000;

// --- Other Constants ---
export const REQUEST_TIMEOUT = 10000; // 10s axios timeout
