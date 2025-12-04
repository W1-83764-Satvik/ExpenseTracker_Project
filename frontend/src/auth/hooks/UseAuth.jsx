// auth/hooks/UseAuth.js
import { isTokenExpired } from '../../utils/authHelpers';
import { useAuthContext } from '../context/AuthContext';

const UseAuth = () => {
  const { user, token, login, signup, logout, loading } = useAuthContext();

  // user is authenticated if token exists and is NOT expired (or near expiry)
  const isAuthenticated = !!token && !isTokenExpired(token);

  return {
    isAuthenticated,
    user,
    token,
    login,
    signup,
    logout,
    loading,
  };
};

export default UseAuth;
