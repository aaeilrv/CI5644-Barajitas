"use client";

import getJwt from "../../../helpers/getJwtClient";

export default function CreateExchangeRequest(
  user: number,
  requestedCard: number
) {
  const API_EXCHANGE_REQUEST_POST_URL =
    process.env.NEXT_PUBLIC_EXCHANGE_REQUEST_URL + ``;

  const postRequest = async () => {
    const { token } = await getJwt();
    const response = await fetch(API_EXCHANGE_REQUEST_POST_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        userId: user,
        requestedCardId: requestedCard,
        requeststatus: "PENDING",
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
  alert("Intercambio solicitado!");
}
