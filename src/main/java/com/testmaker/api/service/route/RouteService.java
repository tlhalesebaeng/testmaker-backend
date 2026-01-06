package com.testmaker.api.service.route;

import com.testmaker.api.utils.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService implements RouteServiceInterface{

    @Value("${api.prefix}")
    private String prefix;

    @Override
    public List<Route> getProtected() {
        return List.of(
                new Route(prefix + "/test", "POST"),
                new Route(prefix + "/test/save-progress", "POST")
        );
    }

    @Override
    public boolean isProtected(String URI, String method) {
        for(Route route : this.getProtected()) {
            if(route.matches(method, URI)) return true;
        }

        return false;
    }
}
