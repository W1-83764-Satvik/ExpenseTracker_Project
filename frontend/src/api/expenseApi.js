import apiClient from "./axios/axiosInstance";
import { EXPENSE_ENDPOINTS } from "../utils/constants";

const expenseApi = {
  // --------------------------
  // CATEGORIES
  // --------------------------

  addCategory: async (data) => {
    const res = await apiClient.post(EXPENSE_ENDPOINTS.ADD_CATEGORY, data);
    return res.data.data;
  },

  getAllCategories: async () => {
    const res = await apiClient.get(EXPENSE_ENDPOINTS.GET_ALL_CATEGORIES);
    return res.data.data;
  },

  // --------------------------
  // EXPENSES
  // --------------------------

  addExpense: async (data) => {
    const res = await apiClient.post(EXPENSE_ENDPOINTS.ADD_EXPENSE, data);
    return res.data.data;
  },

  getAllExpenses: async () => {
    const res = await apiClient.get(EXPENSE_ENDPOINTS.GET_ALL_EXPENSES);
    return res.data.data;
  },
};

export default expenseApi;
