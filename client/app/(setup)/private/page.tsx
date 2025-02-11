'use client';

import { Button } from "@/components/ui/button";
import { signOut } from "next-auth/react";

function PrivatePage() {
  return (
    <div>
      Private Page <br />
      <Button onClick={() => signOut()}>
        Log out
      </Button>
    </div>
  );
}

export default PrivatePage;
