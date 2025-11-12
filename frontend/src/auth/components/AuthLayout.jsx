// auth/components/AuthLayout.jsx

import { Outlet, Navigate } from "react-router-dom";
import UseAuth from '../hooks/UseAuth'

const AuthLayout = () => {
  const { isAuthenticated } = UseAuth();

  // Prevent access to login/signup if already logged in
  if (isAuthenticated) return <Navigate to="/dashboard" replace />;

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100">
      <div className="bg-white shadow-lg rounded-2xl p-8 w-full max-w-md">
        <Outlet />
      </div>
    </div>
  );
};

export default AuthLayout;
