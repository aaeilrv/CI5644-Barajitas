//Exchange offers the user made
//(Responses to another user exchange requests)

"use client";
import Image from "next/image";
import Button from "@/app/components/Button";
import { Fragment, useState, useEffect } from "react";
import getJwt from "../../../helpers/getJwtClient";
import UpdateExchangeOffer from "./updateExchangeOffer";

type exchangeOffer = {
  id: number;
  bidderId: number;
  bidderUsername: string;
  exchangeRequestId: number;
  exchangeRequestCreator: String,
  exchangeRequestCardName: string;
  offeredCardId: number;
  offeredCardName: string;
  status: string;
};

export default function ExchangeOffersMade() {
  const [exchangeOffer, setExchangeOffer] = useState(true);
  const [offerContent, setOfferContent] = useState<exchangeOffer[]>([]);
  const API_EXCHANGE_REQUEST_OFFER_URL =
    process.env.NEXT_PUBLIC_EXCHANGE_OFFER_URL + `/bidder/me`;

  useEffect(() => {
    const getExchangeOfferData = async () => {
      const { token } = await getJwt();
      console.log("EO creadas")
      const response = await fetch(API_EXCHANGE_REQUEST_OFFER_URL, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
      });
      const data = await response.json();
      setOfferContent(data);
      setExchangeOffer(false);
    };
    getExchangeOfferData();
  }, []);

  return (
    <div>
      {offerContent.length > 0 ? (
        offerContent.map((it) => (
          <div key={it.id} className="p-4">
            <div className="rounded-md bg-[#ab9ee6] px-2 py-2 text-xs font-medium text-white ring-1 ring-inset ring-blue-700/10 flex justify-between align-top">
              <div className='flex'>Mi Oferta: <div className="text-black ml-2">{it.offeredCardName}</div></div>
              <div className='flex'>Barajita a Obtener: <div className="text-black ml-2">{it.exchangeRequestCardName}</div></div>
              <div className='flex'>Transacción con: <div className="text-black ml-2">{it.exchangeRequestCreator}</div></div>
              <div className="flex">{it.status}</div>
              <Button
                onClick={() =>
                  UpdateExchangeOffer(
                    it.id,
                    it.bidderId,
                    it.exchangeRequestId,
                    it.offeredCardId,
                    "CANCELLED"
                  )
                }
                text="Cancelar"
                color="red"
              />
            </div>
          </div>
        ))
      ) : (
        <div className="py-4">
          <div className="rounded-md bg-[#ab9ee6] px-2 py-2 text-xs font-medium text-white ring-1 ring-inset ring-blue-700/10 flex justify-between">
            No existen ofertas.
          </div>
        </div>
      )}
    </div>
  );
}
