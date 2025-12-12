import React, { useState } from "react";
import expenseApi from "../../../api/expenseApi";

export const AddExpense = ({ categories, onExpenseAdded }) => {

  const [form, setForm] = useState({
    title: "",
    amount: "",
    categoryId: "",
    description: ""    // FIXED field name
  });

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await expenseApi.addExpense(form);
      setForm({ title: "", amount: "", categoryId: "", description: "" });
      onExpenseAdded?.();
    } catch (err) {
      console.error("Error adding expense:", err);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-5 rounded shadow mb-5">
      <h3 className="text-xl font-semibold mb-3">Add Expense</h3>

      <input
        name="title"
        value={form.title}
        onChange={handleChange}
        placeholder="Title"
        className="border p-2 w-full rounded mb-3"
      />

      <input
        name="amount"
        type="number"
        value={form.amount}
        onChange={handleChange}
        placeholder="Amount"
        className="border p-2 w-full rounded mb-3"
      />

      <select
  name="categoryId"
  value={form.categoryId}
  onChange={handleChange}
  className="border p-2 w-full rounded mb-3"
  required
>
  <option value="">Select Category</option>
  {categories.map((cat) => (
    <option key={cat.id} value={cat.id}>
      {cat.name}
    </option>
  ))}
</select>


      <textarea
        name="description"
        value={form.description}
        onChange={handleChange}
        placeholder="Description"
        className="border p-2 w-full rounded mb-3"
      />

      <button className="bg-blue-600 text-white px-4 py-2 rounded">
        Add
      </button>
    </form>
  );
};
