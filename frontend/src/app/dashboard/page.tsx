
import { getJwtToken } from "../helpers/getJwtToken";

export default async  function Dashboard() {

  const token = await getJwtToken()
  

  return (
    <>
      <div className="py-16 px-8">
        Probando el sistema de routing nuevo de nextjs. Esto es el dashboard (?)
        {token && <p>{token}</p>}
      </div>
    </>
  );
}