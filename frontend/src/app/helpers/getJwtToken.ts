import "server-only";


import { getAccessToken } from "@auth0/nextjs-auth0";


export const getJwtToken = async () => {
  const { accessToken } = await getAccessToken();

  if (!accessToken) {
    throw new Error(`Did not receive an access token.`);
  }

  return accessToken;
};

export const getPublicMessage = async () => {
  return {
    text: "This is a public message.",
  };
};
