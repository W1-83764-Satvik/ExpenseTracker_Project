import apiClient from "./axios/axiosInstance";
import { AUTH_ENDPOINTS } from "../utils/constants";
import { getAccessToken } from "../utils/storage";

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
  const accessToken = getAccessToken();
  const res = await apiClient.get(AUTH_ENDPOINTS.VALIDATE, {
    params: {
      token: accessToken
    }
  });
  return res.data.data;
},


   me: async () => {
    const res = await apiClient.get(AUTH_ENDPOINTS.ME);
    return res.data.data;
  },
};

export default authApi;
