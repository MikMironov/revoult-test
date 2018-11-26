package ru.mm.config;

import ru.mm.datasource.AccountDAO;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import ru.mm.service.AccountService;
import ru.mm.service.impl.AccountServiceImpl;

import javax.inject.Singleton;

public class ProjectBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(AccountServiceImpl.class).to(AccountService.class).in(Singleton.class);
        bind(AccountDAO.class).to(AccountDAO.class).in(Singleton.class);
    }
}
