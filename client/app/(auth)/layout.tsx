import WrapperImage from "@/public/1.jpg";
import { Boxes } from "lucide-react";
import Image from "next/image";

export default function AuthLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div className="p-1 w-full h-screen">
      <div className="h-full grid grid-cols-1 md:grid-cols-2">
        {/* Left Column - Sign Up Form */}
        <div className="flex flex-col px-8 py-12 md:px-12 col-span-1">
          <div className="mb-8">
            <div className="flex items-center gap-2">
              <Boxes />
              <h5 className="font-semibold">CORALS</h5>
            </div>
          </div>
          <div className="w-full h-full">{children}</div>
        </div>
        {/* Right Column - Image */}
        <div className="hidden md:block overflow-hidden relative">
          <div className="w-full h-full rounded-2xl border-4 border-white overflow-hidden">
            <Image
              className="object-cover"
              src={WrapperImage}
              alt="Wrapper Image"
            />
          </div>
          <div className="w-1/6 h-16 absolute left-0 top-0 bg-white border-r-4 border-b-4 border-white rounded-br-2xl flex items-center justify-center after:w-4 after:h-4 after:absolute after:bg-transparent after:-bottom-4 after:left-0 after:rounded-tl-xl after:shadow-[-6px_-6px_0px_white] before:w-4 before:h-4 before:absolute before:bg-transparent before:left-1 before:-bottom-5 before:rounded-tl-xl before:shadow-[-6px_-4px_0px_white]">
            <p className="bg-white text-black w-[90%] text-center py-4 mb-1 text-xs rounded-xl border-4 border-black"> 2M Download</p>
          </div>
          <div className="w-1/6 h-16 absolute right-0 bottom-0 bg-white border-t-4 border-l-4 border-white rounded-tl-2xl flex items-center justify-center after:w-4 after:h-4 after:absolute after:bg-transparent after:-top-4 after:right-0 after:rounded-br-xl after:shadow-[6px_6px_0px_white] before:w-4 before:h-4 before:absolute before:bg-transparent before:right-1 before:-top-5 before:rounded-br-xl before:shadow-[6px_4px_0px_white]">
            <p className="bg-black text-white w-[90%] text-center py-4 text-xs rounded-xl"> 2M Download</p>
          </div>
          <div className="w-4 h-4 absolute right-[calc(16.666667%_-_0px)] bottom-1 bg-transparent rounded-br-lg shadow-[4px_10px_0px_white]"></div>
          <div className="w-4 h-4 absolute right-[calc(16.666667%_-_4px)] bottom-0 bg-transparent rounded-br-xl shadow-[6px_6px_0px_white]"></div>
          <div className="w-4 h-4 absolute left-[calc(16.666667%_-_0px)] top-1 bg-transparent rounded-tl-lg shadow-[-4px_-10px_0px_white]"></div>
          <div className="w-4 h-4 absolute left-[calc(16.666667%_-_4px)] top-0 bg-transparent rounded-tl-xl shadow-[-6px_-6px_0px_white]"></div>
        </div>
      </div>
    </div>
  );
}
