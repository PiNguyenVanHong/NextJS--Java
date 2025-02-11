import NextAuth, { DefaultSession } from "next-auth";
import { JWT } from "next-auth/jwt";

interface IUser {
  name: string;
  access_token: string;
}

declare module "next-auth/jwt" {
  /** Returned by the `jwt` callback and `getToken`, when using JWT session **/
  interface JWT {
    access_token: string;
    refresh_token: string;
    user: IUser;
    access_expire: number;
    error: string;
  }
}

declare module "next-auth/jwt" {
  /** Returned by `useSession`, `getSession` and received as a prop on the callback **/
  interface Session {
    user: IUser;
    access_token: string;
    refresh_token: string;
    access_expire: number;
    error: string;
  }
}
