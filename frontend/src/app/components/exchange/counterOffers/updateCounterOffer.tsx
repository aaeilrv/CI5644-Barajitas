"use client";
import getJwt from "../../../helpers/getJwtClient";

export default function UpdateCounterOffer(
  counterOffer: number,
  offer: number,
  request: number,
  offeredCard: number,
  statustoUpdate: string
) {
  const API_EXCHANGE_REQUEST_PATCH_URL =
    process.env.NEXT_PUBLIC_EXCHANGE_COUNTEROFFER_URL + ``;

  const patchRequest = async () => {
    const { token } = await getJwt();
    const response = await fetch(API_EXCHANGE_REQUEST_PATCH_URL, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        id: counterOffer,
        offeredCardId: offeredCard,
        status: statustoUpdate,
        exchangeRequestId: request,
        exchangeOfferId: offer,
        createdAt: new Date().toISOString(),
      }),
    })
      .then((response) => response.json())
      .then((data) => console.log(data))
      .catch((error) => {
        console.error("Error:", error);
      });
  };
  patchRequest();
  if (statustoUpdate == "ACCEPTED") {
    alert("Oferta aceptada!");
  } else {
    alert("Oferta rechazada!");
  }
}
