package com.sampahin.util;

import com.sampahin.model.Akun; // (Import model Akun Anda)

public class SessionManager {

    private static SessionManager instance;
    private Akun loggedInAkun;

    private SessionManager() {
        // Private constructor
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setLoggedInAkun(Akun akun) {
        this.loggedInAkun = akun;
    }

    public Akun getLoggedInAkun() {
        return this.loggedInAkun;
    }

    public void clearSession() {
        this.loggedInAkun = null;
    }
}