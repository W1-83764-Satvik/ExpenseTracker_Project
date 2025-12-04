import React from 'react'
import UseAuth from '../auth/hooks/UseAuth';
import { Navigate } from 'react-router-dom';

const ProtectedRoutes = ({children}) => {

    const {isAuthenticated, loading} = UseAuth();

    if(loading) return <div>Loading...</div>


  return isAuthenticated ? children : <Navigate to="/login" replace />;
}

export default ProtectedRoutes
