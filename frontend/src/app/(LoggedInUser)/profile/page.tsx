'use client';
import { useUser } from "@auth0/nextjs-auth0/client";
import { useState, useEffect } from 'react';
import Image from "next/image";
import getJwt from "../../helpers/getJwtClient"
import Link from "next/link";


type UserData = {
  id: number,
  username: string,
  firstName: string,
  lastName: string,
  birthDay: string,
  emailAddress: string,
}

export default function Profile() {
  const { user, isLoading } = useUser();
  const [userData, setUserData] = useState<UserData | undefined>(undefined)
  const [userLoaded, setUserLoaded] = useState<boolean>(false)
  const [progress, setProgress] = useState<string>("")
  const [progressLoaded, setProgressLoaded] = useState<boolean>(false)
  const API_GET_USER_URL= `${process.env.NEXT_PUBLIC_USER_API_URL}`

  useEffect(() => {
    const getUserData = async () => {
      const { token } = await getJwt();
      const responseUser = await fetch(
        API_GET_USER_URL,
        {
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          }
        }
      );
      const userResponseData = await responseUser.json();
      console.log(userResponseData);
      setUserData(userResponseData);
      setUserLoaded(true);

      const responseProgress = await fetch(
        `${API_GET_USER_URL}/progress`,
        {
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          }
        }
      );
      console.log(responseProgress);
      const userProgressData = await responseProgress.text();
      setProgress(userProgressData);
      setProgressLoaded(true);
    }
    getUserData(); 
  }, [progressLoaded]);

  if (isLoading || !userLoaded) return <div>Loading...</div>;

  return (

    <div className="flex w-full h-full rounded-lg bg-[#d6dfea] p-4 drop-shadow-md">
        <div className="bg-white p-2 rounded-lg w-48">
          <Image src="https://i.pinimg.com/564x/4b/9b/14/4b9b1469f4246f41384258c827d1d445.jpg" alt="" width={1080} height={1080} loading="lazy" />
        </div>
        <div className="p-4 space-y-10">
          <div className="flex flex-wrap">
            <h1 className="flex-auto font-bold text-slate-900 text-5xl">
              {userData?.username}
            </h1>
            <div className="w-full flex-none mt-2 order-1 space-y-4 text-3xl font-bold text-violet-600">
              Progreso en el álbum: {progress}
            </div>
            <div className="text-sm font-medium text-slate-500"> 
              {userData?.birthDay}
            </div>
          </div>

          <div className="flex space-x-4 mb-5 text-sm font-medium">
            <div className="flex-auto flex space-x-4">
            <p className="text-lg font-semibold">Correo electrónico: </p>
            <p className="text-lg">{userData?.emailAddress}</p>
            </div>
          </div>

          <div className="flex ">
            <Link data-testid="editProfile" className="ml-2 py-2 px-4 bg-gray-900/80 rounded-lg text-white hover:bg-black" href={"/editProfile"}>Editar Perfil</Link>
          </div>
        </div>
      </div>
    
  );
}