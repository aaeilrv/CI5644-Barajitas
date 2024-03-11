"use client"
import axios from "axios";

const GET_JWT_URL = '/api/auth/token/'

export default async function getJwt() {
    const { token } = (await axios.get(
        GET_JWT_URL
    )).data;
    return { token }
}