package com.testmaker.api.service.route;

import com.testmaker.api.utils.Route;

import java.util.List;

public class RouteService implements RouteServiceInterface{
    @Override
    public List<Route> getProtected() {
        return List.of();
    }
}
