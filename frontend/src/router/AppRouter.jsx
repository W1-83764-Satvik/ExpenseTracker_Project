import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "../auth/context/AuthContext";
import Dashboard from '../modules/expense/pages/Dashboard.jsx';
import LoginForm from "../modules/auth/components/LoginForm.jsx";
import SignupForm from "../modules/auth/components/SignupForm.jsx";
import Navbar from "../modules/Navbar.jsx";
import ProtectedRoutes from "./ProtectedRoutes.jsx";

const AppRouter = () => {
  return (
    <Router>
      
      <AuthProvider>
        <Navbar />
        <Routes>
            <Route path="/login" element={<LoginForm />} />
            <Route path="/" element={<SignupForm />} />
            <Route path="/dashboard" element={
              <ProtectedRoutes>
                <Dashboard />
              </ProtectedRoutes>           
              } />
        </Routes>
      </AuthProvider>
    </Router>
  );
};

export default AppRouter;
