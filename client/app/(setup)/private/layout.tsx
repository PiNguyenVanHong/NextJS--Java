import { auth } from "@/auth";

export default async function PrivateLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const session = await auth();

  return (
    <div className="w-full h-full">
      {session?.user?.name || "Hello"}
      {children}
    </div>
  );
}
