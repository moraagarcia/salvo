package com.codeoftheweb.salvo.models;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public static Map<String,Object> makeMap(String string, Object object){
        Map<String,Object> map = new HashMap<>();
        map.put(string,object);
        return map;
    }

    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

}
