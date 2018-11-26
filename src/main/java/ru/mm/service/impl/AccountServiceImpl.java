package ru.mm.service.impl;

import ru.mm.common.Account;
import ru.mm.datasource.AccountDAO;
import ru.mm.exception.AccountException;
import org.jetbrains.annotations.NotNull;
import ru.mm.service.AccountService;

import javax.inject.Inject;
import java.sql.SQLException;

public class AccountServiceImpl implements AccountService {

    private AccountDAO accountDAO;

    @Inject
    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public Account create(Account account) throws SQLException {
        return this.accountDAO.add(account);
    }

    @Override
    public Account get(@NotNull Long id) throws SQLException {
        return this.accountDAO.get(id);
    }

    @Override
    public boolean transfer(@NotNull Long source, @NotNull Long target, @NotNull Long amount) throws SQLException {
        if (amount == null || amount < 0) {
            throw new AccountException("Amount must be positive");
        }
        return this.accountDAO.transfer(source, target, amount);
    }
}
