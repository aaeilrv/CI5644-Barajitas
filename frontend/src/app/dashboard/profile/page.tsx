"use client"
import { getJwtToken } from "@/app/helpers/getJwtToken";
import { getAccessToken } from "@auth0/nextjs-auth0";
import { useUser } from "@auth0/nextjs-auth0/client";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Profile() {
  const {user, isLoading,} = useUser()
  const [token, setToken] = useState('')

  useEffect(() => {
    getToken()
  },[])


  const getToken = async () => {
    await fetch('/api/auth/token').then(res => res.json()).then(data => {
      console.log(data)
      data.token
      setToken(data.token)
      sessionStorage.setItem('token', data.token)
    }).catch(err => console.log(err))


  }

  if (isLoading) return <div>Loading...</div>

  return (
    <div>
      Probando el sistema de routing nuevo de nextjs. Esto es el profile (?)
      
    </div>
  )
}