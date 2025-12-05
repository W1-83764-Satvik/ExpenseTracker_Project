import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import UseAuth from '../auth/hooks/UseAuth'


const Navbar = () => {

  const{logout} = UseAuth()

  const navigate = useNavigate();
  const handleLogout = async ()=>{
    try {
      await logout();
navigate("/")
    }catch(err){
      console.error(err)
    }
  }

  return (
    <div className='h-15 w-200 flex justify-between m-5 ml-140 rounded-3xl  bg-orange-600 text-white px-8 items-center shadow-2xl shadow-gray-400'>
       <Link className='text-2xl font-bold' to="/">Expense Tracker</Link>
      <ul className='flex'>
        <li className='hover:text-gray-200 hover:bg-orange-500 p-2 rounded-2xl cursor-pointer transition'>
            <Link to="/">Home</Link>
        </li>
        <li className='hover:text-gray-200 hover:bg-orange-500 p-2 rounded-2xl cursor-pointer transition'>
            <Link to="/dashboard">Dashboard</Link>
        </li>
        <li className='hover:text-gray-200 hover:bg-orange-500 p-2 rounded-2xl cursor-pointer transition'>
            <Link to="/about">About</Link>
        </li>
        <li className='hover:text-gray-200 hover:bg-orange-500 p-2 rounded-2xl cursor-pointer transition'>
            <Link to="/contact">Contact</Link>
        </li>
        <li className='hover:text-gray-200 hover:bg-orange-500 p-2 rounded-2xl cursor-pointer transition' onClick={handleLogout}>
            Logout
        </li>
      </ul>
    </div>
  )
}

export default Navbar
