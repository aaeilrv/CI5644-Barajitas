"use client";
import React, { useState } from "react";
import { useUser } from "@auth0/nextjs-auth0/client";
import { useRouter } from "next/navigation";
import axios from "axios";
import getJwt from "@/app/helpers/getJwtClient";
import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

interface EditUser {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
}

// Prondodo react-hook-form con zod
const editProfileSchema = z.object({
  firstName: z
    .string()
    .min(4, {
      message: "El nombre de usuario debe tener al menos 4 caracteres",
    })
    .max(30, {
      message: "El nombre de usuario debe tener menos de 30 caracteres",
    }),
  userName: z
    .string()
    .min(4, {
      message: "El nombre de usuario debe tener al menos 4 caracteres",
    })
    .max(30, {
      message: "El nombre de usuario debe tener menos de 30 caracteres",
    }),
  lastName: z
    .string()
    .min(3, {
      message: "El campo no puede ser vacío",
    })
    .max(30, {
      message: "Longitud máxima de 30 caracteres.",
    }),
  email: z
    .string()
    .email({ message: "El correo electrónico no es valido" })
    .min(4, {
      message: "El correo electrónico debe tener al menos 4 caracteres",
    })
    .max(250, {
      message: "El correo electronico debe tener menos de 250 caracteres",
    }),
});

type editProfileSchemaType = z.infer<typeof editProfileSchema>;

export default function Profile() {
  const { user, isLoading } = useUser();
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, isValid, isDirty },
  } = useForm<editProfileSchemaType>({
    resolver: zodResolver(editProfileSchema),
    mode: "onChange",
  });
  const [editUser, setEditUser] = useState<EditUser>({
    firstName: "",
    lastName: "",
    username: "",
    email: "",
  });
  const router = useRouter();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEditUser({ ...editUser, [e.target.name]: e.target.value });
  };

  const API_EDIT_USER_URL = process.env.NEXT_PUBLIC_USER_API_URL + '/edit'

  const onSubmit = handleSubmit(async (data) => {
    const { token } = await getJwt();

    const editProfileBody = {
        firstName: data.firstName,
        lastName: data.lastName,
        username: data.userName,
        email: data.email,
    }
    const response = await fetch(API_EDIT_USER_URL, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(editProfileBody),
    });

    if (response.ok) {
      const data = await response.json();
      console.log("User updated successfully:", data);
      router.push("/profile");
    } else {
      console.error("Error updating user:", response.status);
      alert(
        "Ocurrió un error en la actualización de sus datos. Por favor intente de nuevo"
      );
    }

    // Reset form or update UI as needed
  });

  if (isLoading) return <div>Loading...</div>;

  return (
    <div className="flex w-full h-full rounded-lg bg-[#d6dfea] p-4 drop-shadow-md">
      <div className="p-4">
        <h1 data-testid="headerEditProfile" className="text-2xl font-bold mb-4">Editar Perfil</h1>
        {/* Aquí iría el contenido de tu perfil */}
        <div style={{ display: "flex", justifyContent: "center" }}>
          <form onSubmit={onSubmit} className="flex flex-col max-w-md">
            <label className="flex flex-col mb-4 w-full">
              <span className="mb-2">Nombre:</span>
              <input
                data-testid="name-input"
                id="name-input"
                type="text"
                className="bg-transparent border-b-2  outline-none text-white"
                {...register("firstName")}
              ></input>
              {errors.firstName && (
                <span className="text-red-500 text-sm">
                  {errors.firstName.message}
                </span>
              )}
            </label>
            <label className="flex flex-col mb-4 w-full">
              <span className="mb-2">Apellido:</span>
              <input
                data-testid="lastName-input"
                id="lastName-input"
                type="text"
                className="bg-transparent border-b-2  outline-none text-white"
                {...register("lastName")}
              ></input>
              {errors.lastName && (
                <span className="text-red-500 text-sm">
                  {errors.lastName.message}
                </span>
              )}
            </label>
            <label className="flex flex-col mb-4 w-full">
              <span className="mb-2">Nombre de usuario:</span>
              <input
              data-testid="username-input"
                id="username-input"
                type="text"
                className="bg-transparent border-b-2  outline-none text-white"
                {...register("userName")}
              ></input>
              {errors.userName && (
                <span className="text-red-500 text-sm">
                  {errors.userName.message}
                </span>
              )}
            </label>
            <label className="flex flex-col mb-4 w-full">
              <span className="mb-2">Correo electrónico:</span>
              <input
              data-testid="email-input"
                id="email-input"
                type="text"
                className="bg-transparent border-b-2  outline-none text-white"
                {...register("email")}
              ></input>
              {errors.email && (
                <span className="text-red-500 text-sm">
                  {errors.email.message}
                </span>
              )}
            </label>
            <button
            data-testid="submitButtonEditProfile"
              type="submit"
              className="p-2 bg-blue-500 text-white rounded-md"
              disabled={!isDirty || !isValid}
            >
              Guardar cambios
            </button>
            {(isDirty && !isValid) && (
                <span className="text-red-500 text-sm mt-2">
                    Por favor, complete el formulario
                </span>
            )}
          </form>
        </div>
      </div>
    </div>
  );
}
