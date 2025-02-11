"use server";

import { signIn } from "@/auth";

export async function authenticate(email: string, password: string) {
  try {
    const res = await signIn("credentials", {
      email: email,
      password: password,
      redirect: false,
    });

    return res;
  } catch (error) {
    if ((error as any).name === "InvalidEmailPassordError") {
      return {  
        error: (error as any).type,
        code: 401,
      };
    } else if ((error as any).name === "InactiveAccountError") {
      return {
        error: (error as any).type,
        code: 401,
      };
    } else if ((error as any).name === "NotFoundAccountError") {
      return {
        error: (error as any).type,
        code: 404,
      };
    } else {
      return {
        error: "Internal server error",
        code: 500,
      };
    }
  }
}
