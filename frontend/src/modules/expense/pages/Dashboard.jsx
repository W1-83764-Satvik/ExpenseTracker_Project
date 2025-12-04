import React from "react";
import { useAuthContext } from "../../../auth/context/AuthContext";

const Dashboard = () => {
  const { user, loading } = useAuthContext();

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      Welcome to Dashboard, {user?.username ?? "user"}
    </div>
  );
};

export default Dashboard;
