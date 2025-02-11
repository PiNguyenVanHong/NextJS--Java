import { LOGIN_API, REGISTER_API, RESEND_API, VERIFY_API } from "@/actions/request.api";

export const register = async ({
  name,
  email,
  password
}: {
  name: string;
  email: string;
  password: string;
}) => {
  const res = await fetch(REGISTER_API, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer ",
    },
    body: JSON.stringify({
      name: name,
      email: email,
      password: password,
    }),
    credentials: "include"
  });

  return await res.json();
};

export const login = async ({
  email,
  password,
}: {
  email: any;
  password: any;
}) => {
  const res = await fetch(LOGIN_API, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer ",
    },
    body: JSON.stringify({
      email: email,
      password: password,
    }),
    credentials: "include"
  });

  return res;
};

export const verify = async ({ email, verificationCode }: { email: string; verificationCode: string }) => {
  const res = await fetch(VERIFY_API, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer ",
    },
    body: JSON.stringify({
      email,
      verificationCode
    }),
    credentials: "include"
  });

  return await res.json();
};

export const resendVerificationCode = async ({ email }: { email: string }) => {
  const res = await fetch(`${RESEND_API}?email=${email}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer ",
    },
    credentials: "include"
  });

  return await res.json();
};