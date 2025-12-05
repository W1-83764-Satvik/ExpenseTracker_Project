import React from "react";
import { useAuthContext } from "../../../auth/context/AuthContext";

const Dashboard = () => {
  const { user, loading } = useAuthContext();

  if (loading) return <div>Loading...</div>;

  return (
    <div className="flex justify-center items-center mt-10 min-h-210 mx-20 bg-gray-100 shadow-2xl">
      <div>
        <span className="text-3xl font-semibold px-5">Welcome back,</span> <span className="text-3xl">{user?.username ?? "user"}</span>
        </div>

        
    </div>
  );
};

export default Dashboard;
