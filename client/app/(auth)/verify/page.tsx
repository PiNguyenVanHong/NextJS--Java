"use client";

import * as z from "zod";
import { toast } from "sonner";
import { ArrowLeft, MailOpen } from "lucide-react";
import { useEffect, useState, useTransition } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { redirect, useRouter } from "next/navigation";
import { maskEmail } from "@/lib/utils";
import { resendVerificationCode, verify } from "@/actions/auth.api";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form";
import {
  InputOTP,
  InputOTPGroup,
  InputOTPSeparator,
  InputOTPSlot,
} from "@/components/ui/input-otp";
import { Button } from "@/components/ui/button";

const formSchema = z.object({
  pin: z.string().min(6, {
    message: "Your one-time password must be 6 characters.",
  }),
});

function VerifyPage() {
  const [isPending, startTransition] = useTransition();
  const [email, setEmail] = useState("");
  const router = useRouter();

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      pin: "",
    },
  });

  useEffect(() => {
    const storedEmail = sessionStorage.getItem("email");
    if (!storedEmail) {
      redirect("/login");
    } else {
      setEmail(storedEmail);
    }
  }, []);

  function onSubmit(values: z.infer<typeof formSchema>) {
    startTransition(async () => {
      const { pin } = values;
      try {
        const res = await verify({ email, verificationCode: pin });

        if (res.status === "ok") {
          toast.success(res.message);
          router.push("/login");
        } else {
          toast.error(res.message);
        }
      } catch (error) {
        console.log(error);
        toast.error("Something went wrong!!!");
      }
    });
  }

  const handleResend = () => {
    startTransition(async () => {
      try {
        const res = await resendVerificationCode({ email });

        if (res.status === "ok") {
          toast.success(res.message);
        } else {
          toast.error(res.message);
        }
      } catch (error) {
        console.log(error);
        toast.error("Something went wrong!!!");
      }
    });
  };

  return (
    <div className="w-full h-full flex flex-col px-8 md:px-12 -mt-16 items-center justify-center">
      <div className="mb-8 flex flex-col justify-center items-center">
        <div className="w-fit p-3 border border-neutral-300 rounded-md">
          <MailOpen />
        </div>
        <div>
          <h1 className="mt-8 text-2xl font-semibold text-center">
            Verify your account
          </h1>
          <p className="mt-2 text-sm text-gray-500 text-center">
            We sent a code to{" "}
            <span className="text-[#1a1a1a] font-semibold">
              {maskEmail(email)}
            </span>
          </p>
        </div>
      </div>
      <div className="max-w-md w-full mx-auto">
        <div className="space-y-4">
          <Form {...form}>
            <form
              onSubmit={form.handleSubmit(onSubmit)}
              className="w-full space-y-6"
            >
              <FormField
                control={form.control}
                name="pin"
                render={({ field }) => (
                  <FormItem>
                    <FormControl className="flex items-center justify-center">
                      <InputOTP maxLength={6} {...field} disabled={isPending}>
                        <InputOTPGroup>
                          <InputOTPSlot className="w-16 h-16" index={0} />
                          <InputOTPSlot className="w-16 h-16" index={1} />
                          <InputOTPSlot className="w-16 h-16" index={2} />
                        </InputOTPGroup>
                        <InputOTPSeparator />
                        <InputOTPGroup>
                          <InputOTPSlot className="w-16 h-16" index={3} />
                          <InputOTPSlot className="w-16 h-16" index={4} />
                          <InputOTPSlot className="w-16 h-16" index={5} />
                        </InputOTPGroup>
                      </InputOTP>
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <Button
                className="w-full py-5 rounded-full"
                type="submit"
                disabled={isPending}
              >
                Verify
              </Button>
            </form>
          </Form>
        </div>
        <div className="w-full flex flex-col gap-4 items-center justify-center mt-6">
          <p className="text-center text-sm text-gray-500">
            Didn't receive the email?{" "}
            <button
              className="text-black hover:underline font-medium"
              onClick={handleResend}
              disabled={isPending}
            >
              Click to resend
            </button>
          </p>
          <Button
            className="text-neutral-500"
            variant={"link"}
            onClick={() => redirect("/login")}
          >
            <ArrowLeft />
            <span>Back to log in</span>
          </Button>
        </div>
      </div>
    </div>
  );
}

export default VerifyPage;
