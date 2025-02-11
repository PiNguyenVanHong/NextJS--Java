import { cn } from "@/lib/utils";
import { Boxes, Dot, Heart, Search, ShoppingBag, UserRound } from "lucide-react";
import Link from "next/link";

const menus = [
    { label: "Furniture", url: "/"},
    { label: "Lighting", url: "/"},
    { label: "Accessories", url: "/"},
    { label: "Outdoor", url: "/"}
];

const actions = [
    { Icon: Search, label: "Search", url: "/" },
    { Icon: Heart, label: "Favorite", url: "/" },
    { Icon: ShoppingBag, label: "Cart", url: "/" },
    { Icon: UserRound, label: "Profile", url: "/login" },
];

const Header = () => {
  return (
    <div className="py-6 border-b border-neutral-300">
    <div className="w-full max-w-7xl mx-auto grid grid-cols-3 items-center justify-center">
      <ul className="flex items-center gap-2 font-medium">
        {menus.map((item, index) => (
            <li key={index} className="flex items-center gap-2">
                <Link href={item.url}>{item.label}</Link>
                {index !== menus.length - 1 && (
                    <Dot size={14} />
                )}
            </li>
        ))}
      </ul>
      <div className="flex items-center justify-center gap-2">
        <Boxes />
        <h5 className="font-semibold text-xl">CORALS</h5>
      </div>
      <div className="w-full flex items-center justify-end gap-4">
        {actions.map((item, index) => (
            <Link key={index} href={item.url} className={cn(index === 2 && "relative")}>
                <item.Icon size={20} />
                {index === 2 && (
                    <span className="absolute -top-1 -right-1.5 w-3 h-3 rounded-full bg-red-500 text-white text-[8px] flex items-center justify-center border-2 border-white">
                        4
                    </span>
                )}
            </Link>
        ))}
      </div>
    </div>
    </div>
  );
};

export default Header;
