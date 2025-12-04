import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import authApi from '../api/authApi'


const Navbar = () => {

  const navigate = useNavigate();
  const handleLogout = async ()=>{
    try {
      await authApi.logout();
navigate("/")
    }catch(err){
      console.error(err)
    }
  }

  return (
    <div className='h-15 flex justify-between m-5 rounded-2xl  bg-orange-600 text-gray-100 px-10 items-center shadow-2xl shadow-gray-400'>
       <Link className='text-2xl font-bold' to="/">Expense Tracker</Link>
      <ul className='flex gap-3'>
        <li className='hover:text-gray-400 cursor-pointer transition'>
            <Link to="/">Home</Link>
        </li>
        <li className='hover:text-gray-400 cursor-pointer transition'>
            <Link to="/about">About</Link>
        </li>
        <li className='hover:text-gray-400 cursor-pointer transition'>
            <Link to="/contact">Contact</Link>
        </li>
        <li className='hover:text-gray-400 cursor-pointer transition' onClick={handleLogout}>
            Logout
        </li>
      </ul>
    </div>
  )
}

export default Navbar
