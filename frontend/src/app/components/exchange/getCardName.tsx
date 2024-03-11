//Get the name of a card by id
//(does not work)
"use client";
import { useUser } from "@auth0/nextjs-auth0/client";
import Image from "next/image";
import Button from "@/app/components/Button";
import { Fragment, useState, useEffect } from "react";
import { barajitas_temporal } from "@/utils/barajitas_temporal";
import getJwt from "../../helpers/getJwtClient";

type exchangeProps = {
  requiredCard: number;
};

type barajita = {
  id: number;
  name: string;
  playerPosition: string;
  playerNumber: number;
  numberOwned: number;
  country: string;
  photoURL: string;
};

export default async function getCardName(requiredCard: number) {
  const API_CARD_URL =
    process.env.NEXT_PUBLIC_CARD_API_URL + `/${requiredCard}`;

  const getCardData = async () => {
    const { token } = await getJwt();
    const response = await fetch(API_CARD_URL, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    const data = await response.json();
    return data;
  };
  getCardData();
  const cardRequested = await getCardData();

  return cardRequested;
}
