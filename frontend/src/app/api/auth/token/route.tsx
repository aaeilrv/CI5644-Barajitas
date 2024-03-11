import { getJwtToken } from "@/app/helpers/getJwtToken";
import { NextRequest, NextResponse } from "next/server";


export const GET = async (req: NextRequest, res: NextResponse) => {
    const token = await  getJwtToken()
    return NextResponse.json({ token })
}
