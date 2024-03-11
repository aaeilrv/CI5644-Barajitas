import { getJwtToken } from "./getJwtToken";

export async function checkUserExistance() {
    const token = await getJwtToken();
    const API_USER_DATA_URL = `${process.env.NEXT_PUBLIC_USER_API_URL}`;
    
    if (token === undefined) {
        return false;
    }

    let userRes = await fetch(
        API_USER_DATA_URL,
        {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token!}`
            },
        }
    )

    return userRes.ok;
}