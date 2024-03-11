"use client";

import { useUser } from "@auth0/nextjs-auth0/client";
import Image from "next/image";
import Link from "next/link";
import localfont from '@next/font/local';
import { ChevronLeftIcon, ChevronRightIcon } from "@heroicons/react/24/outline";
import Button from "@/app/components/Button";
import { useState, useEffect } from 'react';
import axios from 'axios';
import getJwt from "../../helpers/getJwtClient";

const ProtestRiot = localfont({ src: '../../../assets/fonts/ProtestRiot-Regular.ttf'});

const navbar = [
  {
    href: "/album",
    text: "Álbum",
  },
  {
    href: "/exchange",
    text: "Intercambio",
  },
  {
    href: "/buy",
    text: "Compra",
  },
  {
    href: "/profile",
    text: "Perfil",
  },
];

type Barajita = {
  id: number,
  playerName: string,
  playerPosition: string,
  playerNumber: number,
  numberOwned: number,
  country: string,
  photoURL: string,
}

export default function Barajitas() {
  const { user, isLoading } = useUser();
  let [loadingAlbum, setLoadingAlbum] = useState(true);
  let [pageNumber, setPageNumber] = useState(-1);
  let [pageContents, setPageContents] = useState<Barajita[]>([]);
  const API_ALBUM_DATA_URL = process.env.NEXT_PUBLIC_USER_API_URL + `/cardsOwned?page=${pageNumber}&size=10`;
  const EMPTY_CARD_IMG_LOC = '/static/images/emptycard.png'
  const CARD_PICTURE_LOC = '/static/images/cards/'
  const country_name = "Argentina";

  useEffect(() => {
    const getAlbumData = async () => {
      const { token } = await getJwt();
      const response = await fetch(
        API_ALBUM_DATA_URL,
        {
          method: 'GET',
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          }
        }
      )
      const data = await response.json();
      setPageContents(data.content);
      setLoadingAlbum(false);
    };
    getAlbumData();
  }, [pageNumber]);

  if (isLoading || loadingAlbum) return <div>Loading...</div>;

  return (
    <>
    <div>
      <div className="w-full mx-auto">
        <div className="mb-10 w-full flex justify-between space-x-4 items-center">
          <h1 className={`text-5xl text-white ${ProtestRiot.className}`}>{country_name}</h1>
         {/* <Button link="/buy" text="¡Compra barajitas!" />*/}
        </div>
        <div className="flex justify-between items-center space-x-4">
          <button>
            <ChevronLeftIcon
              className="h-6 w-6 text-white hover:text-gray-200"
              onClick={() => setPageNumber(pageNumber == 0 ? pageNumber : pageNumber - 1)} />
          </button>
          <div className="grid grid-cols-5 gap-10">
            {pageContents.map((barajita, index) => ( barajita ?
              <div className="rounded-lg bg-white p-2 drop-shadow-md hover:bg-slate-300" key={index}>
                <Image src={CARD_PICTURE_LOC + barajita.playerName + '.jpeg'} alt={barajita.playerName} className="w-full" width={1080} height={1080} />
              </div> : 
              <div className="rounded-lg bg-white p-2 drop-shadow-md hover:bg-slate-300" key={index}>
                <Image src={EMPTY_CARD_IMG_LOC} alt={'empty card'} className="w-full" width={1080} height={1080} />
              </div>
            ))}
          </div>
          <button>
            <ChevronRightIcon 
              className="h-6 w-6 text-white hover:text-gray-200"
              onClick = {() => {setPageNumber(pageNumber + 1)}}
            />
          </button>
        </div>
      </div>
    </div>
  </>
  );
}
