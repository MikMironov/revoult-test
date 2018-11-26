package ru.mm.config;

import org.glassfish.jersey.server.ResourceConfig;
import ru.mm.rest.AccountingController;

import javax.ws.rs.ApplicationPath;


@ApplicationPath("/")
public class AppConfig extends ResourceConfig {

    public AppConfig() {
        register(AccountingController.class);
        register(new ProjectBinder());
    }
}

