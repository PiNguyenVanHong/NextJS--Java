import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export const maskEmail = (email: string): string => {
  const [localPart, domain] = email.split("@");
  if (localPart.length <= 2) return email; // Nếu email quá ngắn, không cần ẩn
  return `${localPart[0]}${"*".repeat(localPart.length - 2)}${localPart.slice(
    -1
  )}@${domain}`;
};