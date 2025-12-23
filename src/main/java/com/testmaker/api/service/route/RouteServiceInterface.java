package com.testmaker.api.service.route;


import com.testmaker.api.utils.Route;

import java.util.List;

public interface RouteServiceInterface {
    List<Route> getProtected();
    boolean isProtected(String URI, String method);
}
