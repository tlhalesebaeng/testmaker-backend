package com.testmaker.api.service.route;

import com.testmaker.api.utils.Route;

import java.util.List;

public class RouteService implements RouteServiceInterface{
    @Override
    public List<Route> getProtected() {
        return List.of();
    }

    @Override
    public boolean isProtected(String URI, String method) {
        for(Route route : this.getProtected()) {
            if(route.matches(method, URI)) return true;
        }

        return false;
    }
}
