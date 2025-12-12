import apiClient from "./axios/axiosInstance";
import { AUTH_ENDPOINTS } from "../utils/constants";

const authApi = {
  // Login -> returns tokens (access + refresh)
  login: async (credentials) => {
    const res = await apiClient.post(AUTH_ENDPOINTS.SIGNIN, credentials);
    return res.data.data;
  },

  // Signup -> also returns tokens
  signup: async (payload) => {
    const res = await apiClient.post(AUTH_ENDPOINTS.SIGNUP, payload);
    return res.data.data;
  },

  // Get logged-in user details
  me: async () => {
    const res = await apiClient.get(AUTH_ENDPOINTS.ME);
    return res.data.data;
  },

  // Optional: logout only if you're doing refresh token invalidation server-side
  logout: async () => {
    const res = await apiClient.post(AUTH_ENDPOINTS.LOGOUT);
    return res.data.data;
  },
};

export default authApi;
