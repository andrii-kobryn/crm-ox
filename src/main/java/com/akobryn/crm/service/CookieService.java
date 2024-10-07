package com.akobryn.crm.service;

import com.akobryn.crm.constants.CookieConstants;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
public class CookieService {

    public Cookie createJwtCookie(String jwt) {
        Cookie cookie = new Cookie(CookieConstants.JWT_KEYWORD, jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(CookieConstants.COOKIE_PATH);
        cookie.setMaxAge(Long.valueOf(Duration.ofHours(CookieConstants.COOKIE_MAX_AGE).toSeconds()).intValue());
        return cookie;
    }
}

