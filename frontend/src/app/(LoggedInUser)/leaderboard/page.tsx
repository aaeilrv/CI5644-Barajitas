'use client';
import Badge from "@/app/components/Badge";
import { useUser } from "@auth0/nextjs-auth0/client";
import { useState, useEffect } from "react";
import axios from 'axios';
import getJwt from "@/app/helpers/getJwtClient";

const total_cards = 100;

export default function Leaderboard() {
  const USER_API_URL = process.env.NEXT_PUBLIC_USER_API_URL + '/leaders';
  const [leaders, setLeaders] = useState<any[]>([]);
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    const fetchLeaders = async () => {
      setLoading(true)
      const { token } = await getJwt();
      try {
        const response = await fetch(USER_API_URL, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });
        const leaders = await response.json();
        setLeaders(leaders);
      } catch (error) {
        console.error('Error fetching leaders:', error);
      }
      setLoading(false)
    }
    fetchLeaders();
  }, [USER_API_URL]);

  const { user, isLoading } = useUser();
  if (isLoading) return <div>Loading...</div>;

  return (
  <div className="w-full h-full rounded-lg bg-[#d6dfea] p-2 drop-shadow-md">
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">Leaderboard</h1>
      <div className="flex justify-center">
        <div className="space-y-4 w-1/2">
          {!loading && leaders?.map((user, index) => (
            <div key={index} className="p-4 rounded-lg bg-white drop-shadow-md flex justify-between px-4">
              <div className="flex space-x-4">
                <p className="text-lg font-bold">{index + 1}</p>
                <p className="text-lg">@{user.username}</p>
              </div>
              <div>
                { user.cardsOwned == total_cards ? (<Badge text="¡Album lleno!"/>) : null }
                <p className="text-lg font-bold">{user.cardsOwned} {user.cardsOwned == 1 ? 'barajita' : 'barajitas'}</p>
              </div>
            </div>
            ))}
        </div>
      </div>
    </div>
  </div>
  );
}
