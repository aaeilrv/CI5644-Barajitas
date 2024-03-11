//Exchange request form for the user to select the card they want to receive from other user
//The one that will be sended to the owners of said card

"use client";
import Image from "next/image";
import localfont from "@next/font/local";
import Button from "@/app/components/Button";
import { barajitas_temporal } from "@/utils/barajitas_temporal";
import { Fragment, useState } from "react";
import { Listbox, Transition } from "@headlessui/react";
import { CheckIcon, ChevronUpDownIcon } from "@heroicons/react/20/solid";
import CreateExchangeRequest from "./createExchangeRequest";
import React from "react";

function classNames(...classes: string[]) {
  return classes.filter(Boolean).join(" ");
}

export default function ExchangePetition() {
  const [selected, setSelected] = useState(barajitas_temporal[0]);

  return (
    <div className="w-full h-full rounded-lg bg-[#d6dfea] p-2 drop-shadow-md">
      <div className="p-4">
        <h1 className="text-2xl font-bold space-y-4">
          {" "}
          ¡Intercambia tus Barajitas!{" "}
        </h1>
        <div>
          <Listbox  value={selected} onChange={setSelected}>
            {({ open }) => (
              <>
                <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900">
                  {" "}
                  Estoy buscando a{" "}
                </Listbox.Label>
                <div className="relative mt-2">
                  <Listbox.Button  data-testid="cardListSelect" className="w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-indigo-600 sm:text-sm sm:leading-6">
                    <span className="block truncate">{selected.name}</span>
                    <span className="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                      <ChevronUpDownIcon
                        className="h-5 w-5 text-gray-400"
                        aria-hidden="true"
                      />
                    </span>
                  </Listbox.Button>

                  <Transition
                    show={open}
                    as={Fragment}
                    leave="transition ease-in duration-100"
                    leaveFrom="opacity-100"
                    leaveTo="opacity-0"
                  >
                    <Listbox.Options className="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                      {barajitas_temporal.map((person) => (
                        <Listbox.Option
                          key={person.id}
                          className={({ active }) =>
                            classNames(
                              active
                                ? "bg-indigo-600 text-white"
                                : "text-gray-900",
                              "relative cursor-default select-none py-2 pl-3 pr-9"
                            )
                          }
                          value={person}
                        >
                          {({ selected, active }) => (
                            <>
                              <span
                              data-testid={`${person.name}-option`}
                                className={classNames(
                                  selected ? "font-semibold" : "font-normal",
                                  "block truncate"
                                )}
                              >
                                {person.name}{" "}
                                <Image
                                  src={person.photo}
                                  alt={person.name}
                                  className="w-10"
                                  width={1080}
                                  height={1080}
                                />
                              </span>

                              {selected ? (
                                <span
                                  className={classNames(
                                    active ? "text-white" : "text-indigo-600",
                                    "absolute inset-y-0 right-0 flex items-center pr-4"
                                  )}
                                >
                                  <CheckIcon
                                    className="h-5 w-5"
                                    aria-hidden="true"
                                  />
                                </span>
                              ) : null}
                            </>
                          )}
                        </Listbox.Option>
                      ))}
                    </Listbox.Options>
                  </Transition>
                </div>
              </>
            )}
          </Listbox>
        </div>
        <div style={{ display: "flex" }}>
          <Button
            testId="exchangePetitionButton"
            onClick={() => CreateExchangeRequest(1, selected.id)}
            style={{ margin: "auto", display: "block", marginTop: "20px" }}
            text="Solicitar intercambio"
          />
        </div>
      </div>
    </div>
  );
}
