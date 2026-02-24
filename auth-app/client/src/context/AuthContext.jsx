import React, { createContext, useContext, useEffect, useMemo, useState } from "react";
import { api } from "../lib/api";
import { clearToken, setToken } from "../lib/api";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [booting, setBooting] = useState(true);

  useEffect(() => {
    (async () => {
      try {
        const { user } = await api.me();
        setUser(user);
      } catch {
        setUser(null);
      } finally {
        setBooting(false);
      }
    })();
  }, []);

  const value = useMemo(
    () => ({
      user,
      booting,
      async login(email, password) {
        const { token, user } = await api.login({ email, password });
        setToken(token);
        setUser(user);
        return user;
      },
      async signup(name, email, password) {
        const { token, user } = await api.signup({ name, email, password });
        setToken(token);
        setUser(user);
        return user;
      },
      logout() {
        clearToken();
        setUser(null);
      }
    }),
    [user, booting]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
