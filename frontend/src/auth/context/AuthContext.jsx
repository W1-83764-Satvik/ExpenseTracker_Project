import { createContext, useContext, useEffect, useState } from "react";
import authApi from "../../api/authApi";
import {
  saveAuthData,
  getAccessToken,
  getUserData,
  clearAuthData,
} from "../../utils/storage";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {  
  const [user, setUser] = useState(getUserData());
  const [token, setToken] = useState(getAccessToken());
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadUser = async () => {
      if (!token) {
        setLoading(false);
        return;
      }
      try {
        const data = await authApi.getProfile();
        setUser(data);
      } catch (err) {
        console.error("Error loading user:", err);
        clearAuthData();
        setUser(null);
        setToken(null);
      } finally {
        setLoading(false);
      }
    };
    loadUser();
  }, [token]);

  const login = async (credentials) => {
    const data = await authApi.login(credentials);
    saveAuthData(data.accessToken, data.refreshToken, data.user);
    setToken(data.accessToken);
    setUser(data.user);
  };

  const signup = async (userData) => {
    const data = await authApi.signup(userData);
    saveAuthData(data.accessToken, data.refreshToken, data.user);
    setToken(data.accessToken);
    setUser(data.user);
  };

  const logout = () => {
    clearAuthData();
    setUser(null);
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, login, signup, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuthContext = () => useContext(AuthContext);
