import apiClient from "./axios/axiosInstance";
import { AUTH_ENDPOINTS } from "../utils/constants";

const authApi = {
  login: async (credentials) => {
    const res = await apiClient.post(AUTH_ENDPOINTS.SIGNIN, credentials);
    return res.data.data;
  },

  signup: async (payload) => {
    const res = await apiClient.post(AUTH_ENDPOINTS.SIGNUP, payload);
    return res.data.data;
  },

  logout: async () => {
    const res = await apiClient.post(AUTH_ENDPOINTS.LOGOUT);
    return res.data.data;
  },

  getProfile: async () => {
    const res = await apiClient.get(AUTH_ENDPOINTS.VALIDATE);
    return res.data.data;
  },
};

export default authApi;
