import React, { useEffect, useState } from "react";
import { useAuthContext } from "../../../auth/context/AuthContext";
import { AddExpense } from "../components/AddExpense";
import expenseApi from "../../../api/expenseApi";

const Dashboard = () => {
  const { user, loading } = useAuthContext();

  const [expenses, setExpenses] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loadingExpenses, setLoadingExpenses] = useState(true);

  useEffect(() => {
    fetchExpenses();
    fetchCategories();
  }, []);

  const fetchExpenses = async () => {
    try {
      const data = await expenseApi.getAllExpenses();
      setExpenses(data);
    } catch (err) {
      console.error("Failed to load expenses:", err);
    } finally {
      setLoadingExpenses(false);
    }
  };

  const fetchCategories = async () => {
    try {
      const data = await expenseApi.getAllCategories();
      setCategories(data);
    } catch (err) {
      console.error("Failed to load categories:", err);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="flex flex-col mx-20 mt-10 bg-gray-100 shadow-2xl p-10">

      <h2 className="text-3xl font-semibold mb-5">
        Welcome back, <span className="font-bold">{user?.email}</span>
      </h2>

      {/* PASS CATEGORIES */}
      <AddExpense 
  categories={categories} 
  onExpenseAdded={fetchExpenses} 
/>


      {/* EXPENSE LIST */}
      <h3 className="text-2xl font-semibold mt-8 mb-3">Your Expenses</h3>

      {loadingExpenses ? (
        <p>Loading expenses...</p>
      ) : expenses.length === 0 ? (
        <p>No expenses yet.</p>
      ) : (
        <ul className="space-y-3">
          {expenses.map((exp) => (
            <li key={exp.id} className="p-4 bg-white shadow rounded">
              <div className="font-bold">{exp.title}</div>
              <div>₹{exp.amount}</div>
              <div className="text-sm text-gray-600">{exp.categoryName}</div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default Dashboard;
