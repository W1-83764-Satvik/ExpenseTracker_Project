import React from "react";
import AppRouter from "./router/AppRouter.jsx";
import { AuthProvider } from "./auth/context/AuthContext.jsx";

const App = () => (
  <AuthProvider>
    <AppRouter />
  </AuthProvider>
);

export default App;
