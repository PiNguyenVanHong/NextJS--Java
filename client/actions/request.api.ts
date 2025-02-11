const BASE_URL = process.env.NEXT_PUBLIC_API_PREFIX;

const AUTH_API = `${BASE_URL}/auth`;

export const LOGIN_API = `${AUTH_API}/login`;
export const REGISTER_API = `${AUTH_API}/register`;
export const VERIFY_API = `${AUTH_API}/verify`;
export const RESEND_API = `${AUTH_API}/resend`;