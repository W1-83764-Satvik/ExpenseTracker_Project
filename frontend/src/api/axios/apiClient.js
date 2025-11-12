// api/axios/apiClient.js
import axios from "axios";
import { BASE_URL, REQUEST_TIMEOUT } from "../../utils/constants";

const apiClient = axios.create({
  baseURL: BASE_URL,
  timeout: REQUEST_TIMEOUT,
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClient;
