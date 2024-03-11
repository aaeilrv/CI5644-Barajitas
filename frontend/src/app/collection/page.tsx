"use client";
import Image from "next/image";
import { useEffect, useState } from "react";

export default function Collection() {
  const API_CARD_DATA_URL = `${process.env.NEXT_PUBLIC_CARD_API_URL}`;
  const [cards, setCards] = useState<any[]>([]);
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    const fetchCards = async () => {
      setLoading(true)
      try {
        const response = await fetch(
          API_CARD_DATA_URL,
          {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        });
        const cards = await response.json();
        setCards(cards);
      } catch (error) {
        console.error('Error fetching cards:', error);
      }
      setLoading(false)
    }
    fetchCards();
  }, [API_CARD_DATA_URL]);
  
  return (
    <div className="flex justify-center">
      <div className="space-y-4 w-9/12 justify-center mb-10">
        <h1 className="text-2xl font-bold mb-10 mt-10">Barajitas disponibles:</h1>
        <div className="grid grid-cols-5 gap-10">
          {!loading && cards.map((barajita, index) => (
            <div className="rounded-lg bg-white p-2 drop-shadow-md hover:bg-slate-300" key={index}>
              <Image src={barajita.photoURL} alt={barajita.name} className="w-full" width={1080} height={1080} />
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}