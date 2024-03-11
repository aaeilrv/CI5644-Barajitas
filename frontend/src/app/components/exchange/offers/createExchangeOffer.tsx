"use client";

import getJwt from "../../../helpers/getJwtClient";

export default function CreateExchangeOffer(
  user: number,
  requestedCard: number,
  request: number
) {
  const API_EXCHANGE_OFFER_POST_URL =
    process.env.NEXT_PUBLIC_EXCHANGE_OFFER_URL + ``;

  const postRequest = async () => {
    const { token } = await getJwt();
    const response = await fetch(API_EXCHANGE_OFFER_POST_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        bidderId: user,
        exchangeRequestId: request,
        offeredCardId: requestedCard,
        status: "PENDING",
        createdAt: new Date().toISOString(),
      }),
    })
      .then((response) => response.json())
      .then((data) => console.log(data))
      .catch((error) => {
        console.error("Error:", error);
      });
  };
  postRequest();
  alert("Oferta solicitada!");
}
