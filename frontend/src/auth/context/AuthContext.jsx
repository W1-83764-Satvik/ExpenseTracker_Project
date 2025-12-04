import { createContext, useContext, useEffect, useState } from "react"; 
import authApi from "../../api/authApi";
import { saveAuthData, getAccessToken, clearAuthData } from "../../utils/storage";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(getAccessToken());
  const [loading, setLoading] = useState(!!token);

  useEffect(() => {
    const loadUser = async () => {
      if (!token) {
        setLoading(false);
        return;
      }

      try {
        const me = await authApi.me();   // calls /auth/v1/me which expects Authorization header
        console.log("me:", me);
        setUser(me);
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
    const res = await authApi.login(credentials);
    saveAuthData(res.accessToken, res.refreshToken);
    setToken(res.accessToken);   // triggers user load
  };

  const signup = async (data) => {
    const res = await authApi.signup(data);
    saveAuthData(res.accessToken, res.refreshToken);
    setToken(res.accessToken);   // triggers user load
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
