"use client"
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { axiosHookWithoutToken } from '../../hooks/axiosHook';

type LoginValues = {
  email: string;
  password: string;
};

const loginSchema = z.object({
  email: z.string().email({
    message: 'El correo electrónico no es válido',
  }),
  password: z.string().min(4, {
    message: 'La contraseña debe tener al menos 4 caracteres',
  }),
});

type LoginSchemaType = z.infer<typeof loginSchema>;

export const Login = () => {
  const [passwordEye, setPasswordEye] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors, isValid, isDirty },
  } = useForm<LoginSchemaType>({
    resolver: zodResolver(loginSchema),
    mode: 'onChange',
  });

  const onSubmit = handleSubmit(async (data) => {
    const loginBody = {
      email: data.email,
      password: data.password,
    };

    try {
      const loginResponse = await axiosHookWithoutToken(
        'post',
        'signin/admins', // Cambiar URL para enviar solicitud de inicio de sesión
        loginBody
      );

      if (loginResponse!!.status === 'success') {
        // Si el inicio de sesión fue exitoso, realiza las acciones necesarias.
        console.log('Inicio de sesión exitoso');
      } else {
        console.log(loginResponse!!.message);
      }
    } catch (error) {
      console.error('Error al iniciar sesión', error);
    }
  });

  const handlePasswordEye = () => {
    setPasswordEye(!passwordEye);
  };

  return (
    <div className="flex  sm:w-full md:w-4/6 justify-center py-32 sm:py-24 md:py-24 lg:py-24">
      <div className="bg-[#4DBDEB] w-full  sm:w-2/5 md:w-6/12 flex flex-col rounded-2xl">
        {/* header or title */}
        <div>
          <h1 className="text-4xl text-white font-bold text-center mt-12">
            Iniciar Sesión
          </h1>
        </div>
        {/* Form */}
        <div className="flex justify-center">
          <form className="flex flex-col w-2/3" onSubmit={onSubmit}>
            <div className="flex flex-col mt-12">
              <label className="text-white font-semibold text-lg">
                Correo Electrónico
              </label>
              <input
                type="text"
                className="bg-transparent border-b-2  outline-none text-white"
                {...register('email')}
              />
              {errors.email && (
                <span className="text-red-500 text-sm">
                  {errors.email.message}
                </span>
              )}
            </div>
            <div className="flex flex-col mt-12">
              <label className="text-white font-semibold text-lg">
                Contraseña
              </label>
              {/* input tipo password con boton de ojo */}
              {passwordEye ? (
                <div className="flex">
                  <input
                    type="text"
                    className="bg-transparent border-b-2 border-white outline-none text-white w-full"
                    {...register('password')}
                  />
                  {/* ... */}
                </div>
              ) : (
                <div className="flex">
                  <input
                    type="password"
                    className="bg-transparent border-b-2 border-white outline-none text-white w-full"
                    {...register('password')}
                  />
                  {/* ... */}
                </div>
              )}
              {errors.password && (
                <span className="text-red-500 text-sm">
                  {errors.password.message}
                </span>
              )}
            </div>
            <div className="flex mt-16 justify-center mb-12">
              <button
                className="bg-[#FFE08C] disabled:opacity-50 text-white text-center rounded-lg py-3 md:p-3 w-1/2"
                disabled={!isDirty || !isValid}
              >
                Iniciar Sesión
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
