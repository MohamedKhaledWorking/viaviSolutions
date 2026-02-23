// src/pages/Home.jsx
import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";

import { api } from "../lib/api";
import { useAuth } from "../context/AuthContext";

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { Button } from "../components/ui/button";
import { Alert, AlertDescription } from "../components/ui/alert";

export default function Home() {
  const nav = useNavigate();
  const { user, logout } = useAuth();

  const [serverError, setServerError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let mounted = true;

    (async () => {
      setLoading(true);
      setServerError("");

      try {
        // verify token / session
        await api.me();
      } catch (e) {
        if (!mounted) return;

        setServerError(
          e?.message || "Something went wrong while loading your session."
        );
      } finally {
        if (mounted) setLoading(false);
      }
    })();

    return () => {
      mounted = false;
    };
  }, []);

  function handleGoLogin() {
    logout();
    nav("/login");
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.3 }}
        className="w-full max-w-xl"
      >
        <Card>
          <CardHeader>
            <CardTitle>Home</CardTitle>
            <CardDescription>You are logged in ✅</CardDescription>
          </CardHeader>

          <CardContent className="space-y-4">
            {/* ✅ Alert يظهر لما الباك يرجع error */}
            {serverError ? (
              <Alert className="border-destructive/50">
                <AlertDescription className="flex items-center justify-between gap-3">
                  <span>{serverError}</span>
                  <Button variant="destructive" onClick={handleGoLogin}>
                    Go to Login
                  </Button>
                </AlertDescription>
              </Alert>
            ) : null}

            <div className="rounded-lg border p-4">
              <p className="text-sm text-muted-foreground">Logged user</p>
              <p className="font-medium">{user?.name || "User"}</p>
              <p className="text-sm">{user?.email}</p>
            </div>

            <div className="flex gap-2">
              <Button variant="outline" onClick={() => nav("/home")} disabled={loading}>
                {loading ? "Checking..." : "Refresh"}
              </Button>

              <Button variant="destructive" onClick={logout}>
                Logout
              </Button>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}
