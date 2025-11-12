import { useAuthContext } from '../context/AuthContext';

const UseAuth = () => {
  const { user, token, login, signup, logout, loading } = useAuthContext();

  return {
    isAuthenticated: !!token,
    user,
    token,
    login,
    signup,
    logout,
    loading,
  };
};

export default UseAuth;
