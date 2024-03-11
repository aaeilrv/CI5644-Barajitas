import "server-only"

import { redirect } from 'next/navigation';
import { checkUserExistance } from '../helpers/checkUserExistance';

export default async function SignUpRedirect() {

    const userExists = await checkUserExistance();

    if (userExists) {
        redirect('/album');
    } else {
        redirect('/register');
    }

    return null;
}