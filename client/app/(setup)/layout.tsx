import Header from "@/components/header";

export default function SetupLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="w-full h-full">
      <Header />
      {children}
    </div>
  );
}
