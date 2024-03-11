"use client";
import getJwt from "../../../helpers/getJwtClient";

export default function UpdateExchangeRequest(
  request: number,
  user: number,
  requestedCard: number,
  statustoUpdate: string
) {
  const API_EXCHANGE_REQUEST_PATCH_URL =
    process.env.NEXT_PUBLIC_EXCHANGE_REQUEST_URL + ``;

  const patchRequest = async () => {
    const { token } = await getJwt();
    const response = await fetch(API_EXCHANGE_REQUEST_PATCH_URL, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        id: request,
        userId: user,
        requestedCardId: requestedCard,
        status: statustoUpdate,
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
  alert("Request deleted!");
}
