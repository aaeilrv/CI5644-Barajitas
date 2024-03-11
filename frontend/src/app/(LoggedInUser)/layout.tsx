'use client';
import '../../styles/globals.css'
import React, { ReactNode } from 'react';
import Link from 'next/link';
import { UserProvider, useUser } from '@auth0/nextjs-auth0/client';
import { Fragment, useState } from 'react'
import { Dialog, Transition } from '@headlessui/react'
import { usePathname } from 'next/navigation';
import {
  Bars3Icon,
  BookOpenIcon,
  HomeIcon,
  UserIcon,
  UsersIcon,
  XMarkIcon,
  ArrowPathIcon,
  CreditCardIcon,
  ArrowLeftStartOnRectangleIcon
} from '@heroicons/react/24/outline'

function classNames(...classes: string[]) {
  return classes.filter(Boolean).join(' ')
}

export default function LoggedInLayout({ children }: {
  children: React.ReactNode
}) {
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const pathname = usePathname()
  const { user, isLoading} = useUser();

  const navigation = [
    { name: 'Mi álbum', href: '/album', icon: BookOpenIcon, current: pathname === '/album' },
    { name: 'Intercambio', href: '/exchange', icon: ArrowPathIcon, current: pathname === '/exchange' },
    { name: 'Comprar barajitas', href: '#', icon: CreditCardIcon, current: pathname === '/buy' },
    { name: 'Leaderboard', href: '/leaderboard', icon: UsersIcon, current: pathname === '/leaderboard' },
  ]

  const personal_navigation = [
    { name: 'Perfil', href: '/profile', icon: UserIcon, current: pathname === '/profile' },
    { name: 'Cerrar Sesión', href: '/api/auth/logout', icon: ArrowLeftStartOnRectangleIcon, current: false },
  ]

  return (
    <>
      <div>
        <Transition.Root show={sidebarOpen} as={Fragment}>
          <Dialog as="div" className="relative z-50 lg:hidden" onClose={setSidebarOpen}>
            <Transition.Child
              as={Fragment}
              enter="transition-opacity ease-linear duration-300"
              enterFrom="opacity-0"
              enterTo="opacity-100"
              leave="transition-opacity ease-linear duration-300"
              leaveFrom="opacity-100"
              leaveTo="opacity-0"
            >
              <div className="fixed inset-0 bg-gray-900/80" />
            </Transition.Child>

            <div className="fixed inset-0 flex">
              <Transition.Child
                as={Fragment}
                enter="transition ease-in-out duration-300 transform"
                enterFrom="-translate-x-full"
                enterTo="translate-x-0"
                leave="transition ease-in-out duration-300 transform"
                leaveFrom="translate-x-0"
                leaveTo="-translate-x-full"
              >
                <Dialog.Panel className="relative mr-16 flex w-full max-w-xs flex-1">
                  <Transition.Child
                    as={Fragment}
                    enter="ease-in-out duration-300"
                    enterFrom="opacity-0"
                    enterTo="opacity-100"
                    leave="ease-in-out duration-300"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                  >
                    <div className="absolute left-full top-0 flex w-16 justify-center pt-5">
                      <button type="button" className="-m-2.5 p-2.5" onClick={() => setSidebarOpen(false)}>
                        <XMarkIcon className="h-6 w-6 text-white" aria-hidden="true" />
                      </button>
                    </div>
                  </Transition.Child>

                  {/* Sidebar mobile */}
                  <div className="flex grow flex-col gap-y-5 overflow-y-auto border-[#c7d3e1] bg-[#d6dfea] px-6 pb-2">
                    <div className="flex h-16 shrink-0 items-center">
                      <Link href="/" className="flex">
                        <BookOpenIcon className="w-8" />
                        <div className="font-bold text-2xl">Barajitas</div>
                      </Link>
                    </div>
                    <nav className="flex flex-1 flex-col">
                      <ul role="list" className="flex flex-1 flex-col gap-y-7">
                        <li>
                          <ul role="list" className="-mx-2 space-y-1">
                            {navigation.map((item) => (
                              <li key={item.name}>
                                <a
                                  href={item.href}
                                  className={classNames(
                                    item.current
                                      ? 'bg-gray-50 text-indigo-600'
                                      : 'text-gray-700 hover:text-indigo-600 hover:bg-gray-50',
                                    'group flex gap-x-3 rounded-md p-2 text-sm leading-6 font-semibold'
                                  )}
                                >
                                  <item.icon
                                    className={classNames(
                                      item.current ? 'text-indigo-600' : 'text-gray-700 group-hover:text-indigo-600',
                                      'h-6 w-6 shrink-0'
                                    )}
                                    aria-hidden="true"
                                  />
                                  {item.name}
                                </a>
                              </li>
                            ))}
                          </ul>
                        </li>
                      </ul>
                      <div className="relative">
                        <div className="absolute inset-0 flex items-center py-4" aria-hidden="true">
                          <div className="w-full border-t border-gray-300" />
                          </div>
                      </div>
                      <ul role="list" className="-mx-2 space-y-1 py-10">
                        {personal_navigation.map((item) => (
                          <li key={item.name}>
                            <a
                              href={item.href}
                              className={classNames(
                                item.current
                                  ? 'bg-gray-50 text-indigo-600'
                                  : 'text-gray-700 hover:text-indigo-600 hover:bg-gray-50',
                                'group flex gap-x-3 rounded-md p-2 text-sm leading-6 font-semibold'
                              )}
                            >
                              <item.icon
                                className={classNames(
                                  item.current ? 'text-indigo-600' : 'text-gray-700 group-hover:text-indigo-600',
                                  'h-6 w-6 shrink-0'
                                )}
                                aria-hidden="true"
                              />
                              {item.name}
                            </a>
                          </li>
                        ))}
                      </ul>
                    </nav>
                  </div>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </Dialog>
        </Transition.Root>

        {/* Sidebar desktop */}
        <div className="hidden lg:fixed lg:inset-y-0 lg:z-50 lg:flex lg:w-72 lg:flex-col">
          <div className="flex grow flex-col gap-y-5 overflow-y-auto border-r border-[#c7d3e1] bg-[#d6dfea] px-6">
            <div className="flex h-16 shrink-0 items-center">
              <Link href="/" className="flex">
                <BookOpenIcon className="w-8" />
                <div className="font-bold text-2xl">Barajitas</div>
              </Link>
            </div>
            
            <div className='w-full flex justify-center'>
              <h1 className='font-bold text-2xl'>¡Hola, {user?.nickname}!</h1>
            </div>

            <nav className="flex flex-1 flex-col">
              <ul role="list" className="flex flex-1 flex-col gap-y-7">
                <li>
                  <ul role="list" className="-mx-2 space-y-1">
                    {navigation.map((item) => (
                      <li key={item.name}>
                        <a
                        data-testid={item.name}
                          href={item.href}
                          className={classNames(
                            item.current
                              ? 'bg-gray-50 text-indigo-600'
                              : 'text-gray-700 hover:text-indigo-600 hover:bg-gray-50',
                            'group flex gap-x-3 rounded-md p-2 text-sm leading-6 font-semibold'
                          )}
                        >
                          <item.icon
                            className={classNames(
                              item.current ? 'text-indigo-600' : 'text-gray-700 group-hover:text-indigo-600',
                              'h-6 w-6 shrink-0'
                            )}
                            aria-hidden="true"
                          />
                          {item.name}
                        </a>
                      </li>
                    ))}
                  </ul>
                  <div className="relative">
                    <div className="absolute inset-0 flex items-center py-4" aria-hidden="true">
                      <div className="w-full border-t border-gray-300" />
                    </div>
                  </div>
                  <ul role="list" className="-mx-2 space-y-1 py-10">
                    {personal_navigation.map((item) => (
                      <li key={item.name}>
                        <a
                          data-testid={item.name}
                          href={item.href}
                          className={classNames(
                            item.current
                              ? 'bg-gray-50 text-indigo-600'
                              : 'text-gray-700 hover:text-indigo-600 hover:bg-gray-50',
                            'group flex gap-x-3 rounded-md p-2 text-sm leading-6 font-semibold'
                          )}
                        >
                          <item.icon
                            className={classNames(
                              item.current ? 'text-indigo-600' : 'text-gray-700 group-hover:text-indigo-600',
                              'h-6 w-6 shrink-0'
                            )}
                            aria-hidden="true"
                          />
                          {item.name}
                        </a>
                      </li>
                    ))}
                  </ul>
                </li>
              </ul>
            </nav>
          </div>
        </div>

        <div className="sticky top-0 z-40 flex items-center gap-x-6 border-[#c7d3e1] bg-[#d6dfea] px-4 py-4 shadow-sm sm:px-6 lg:hidden">
          <button type="button" className="-m-2.5 p-2.5 text-gray-700 lg:hidden" onClick={() => setSidebarOpen(true)}>
            <Bars3Icon className="h-6 w-6" aria-hidden="true" />
          </button>
          <div className="flex-1 text-sm font-semibold leading-6 text-gray-900">Dashboard</div>
        </div>

        <main className="py-10 lg:pl-72">
          <div className="px-4 sm:px-6 lg:px-8">{children}</div>
        </main>
      </div>
    </>
  )
}