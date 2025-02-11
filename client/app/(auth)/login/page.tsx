"use client";

import { z } from "zod";
import { useState, useTransition } from "react";
import { useForm } from "react-hook-form";
import { Eye, EyeClosed } from "lucide-react";
import { zodResolver } from "@hookform/resolvers/zod";
import { authenticate } from "@/utils/actions";
import { toast } from "sonner";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Button } from "@/components/ui/button";
import { Checkbox } from "@/components/ui/checkbox";
import { Input } from "@/components/ui/input";
import { useRouter } from "next/navigation";

const formSchema = z.object({
  email: z.string().email().min(2).max(50),
  password: z.string().min(2).max(50),
  termsAccepted: z
    .boolean()
    .refine((val) => val === true, "You must accept the terms and conditions"),
});

export default function LoginPage() {
  const router = useRouter();
  const [isPending, startTransition] = useTransition();
  const [isHide, setIsHide] = useState(true);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "phi@gmail.com",
      password: "123456",
      termsAccepted: true,
    },
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    startTransition(() => {
      const { email, password } = values;

      authenticate(email, password)
        .then((res) => {
          if (res.error) {
            toast.error(res.error);
            return;
          }

          router.push("/private");
          toast.success("Login Successfully");
        })
        .catch((error) => {
          console.log(error);
          toast.error("Something went wrong!!!");
        });
    });
  }

  return (
    <div className="flex flex-col px-8 md:px-12">
      <div className="mb-8">
        <h1 className="mt-8 text-2xl font-semibold text-center">
          Welcome back!
        </h1>
        <p className="mt-2 text-sm text-gray-500 text-center">
          Please enter the details below to continute.
        </p>
      </div>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="max-w-md w-full mx-auto"
        >
          <Button
            variant="outline"
            className="mb-6 py-5 w-full rounded-full items-center justify-center"
            disabled={isPending}
          >
            <div className="w-5 h-5">
              <img
                src="https://img.icons8.com/?size=100&id=17949&format=png&color=000000"
                alt=""
              />
            </div>
            <span className="font-semibold">Login with Google</span>
          </Button>
          <div className="relative mb-6">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t" />
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="bg-background px-2 text-muted-foreground">
                or
              </span>
            </div>
          </div>
          <div className="space-y-4">
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>
                    Email <span className="text-red-500">*</span>
                  </FormLabel>
                  <FormControl>
                    <Input
                      className="rounded-full"
                      type="email"
                      placeholder="Enter your email"
                      {...field}
                      disabled={isPending}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>
                    Password <span className="text-red-500">*</span>
                  </FormLabel>
                  <FormControl>
                    <div className="relative">
                      <Input
                        className="rounded-full pr-8"
                        type={isHide ? "password" : "text"}
                        placeholder="Enter your password"
                        {...field}
                        maxLength={50}
                        disabled={isPending}
                      />
                      <button
                        type="button"
                        className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground"
                        onClick={() => setIsHide(!isHide)}
                        disabled={isPending}
                      >
                        {isHide ? <EyeClosed size={18} /> : <Eye size={18} />}
                      </button>
                    </div>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="termsAccepted"
              render={({ field }) => (
                <FormItem className="flex items-center gap-3 py-2 space-y-0">
                  <FormControl>
                    <Checkbox
                      checked={field.value}
                      onCheckedChange={field.onChange}
                      disabled={isPending}
                    />
                  </FormControl>
                  <div className="space-y-1 leading-none tracking-wide">
                    <FormLabel>
                      I agree to all Term, Privacy Policy and Fees
                    </FormLabel>
                    <FormMessage />
                  </div>
                </FormItem>
              )}
            />
          </div>

          <Button
            className="mt-6 py-5 w-full bg-black text-white hover:bg-black/90 rounded-full"
            type="submit"
            disabled={isPending}
          >
            Sign in
          </Button>

          <p className="mt-6 text-left text-sm text-gray-500">
            You don't have an account?{" "}
            <a href="/register" className="text-black hover:underline">
              Sign up
            </a>
          </p>
        </form>
      </Form>
    </div>
  );
}
