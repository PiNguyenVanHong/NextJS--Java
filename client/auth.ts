import NextAuth from "next-auth";
import Credentials from "next-auth/providers/credentials";
import {
  InactiveAccountError,
  InvalidEmailPassordError,
  NotFoundAccountError,
} from "@/utils/errors";
import { login } from "@/actions/auth.api";
import { IUser } from "@/types/next-auth";

export const { handlers, signIn, signOut, auth } = NextAuth({
  providers: [
    Credentials({
      credentials: {
        email: {},
        password: {},
      },
      authorize: async (credentials) => {
        const res = await login({
          email: credentials.email,
          password: credentials.password,
        });

        if (res.status === 200) {
          const { data } = await res.json();

          return {
            name: data.name,
            access_token: data.token,
          };
        }

        if (res.status === 404) {
          throw new NotFoundAccountError();
        } else if (res.status === 400) {
          throw new InactiveAccountError();
        } else if (res.status === 401) {
          throw new InvalidEmailPassordError();
        } else {
          throw new Error("Internal Server Error");
        }
      },
    }),
  ],
  pages: {
    signIn: "/login",
  },
  callbacks: {
    jwt({ token, user }) {
      if (user) {
        token.user = (user as IUser);
      }
      return token;
    },
    session({ session, token }) {
      (session.user as unknown as IUser) = token.user;
      return session;
    },
    authorized: async ({ auth }) => {
      return !!auth;
    },
  },
});
