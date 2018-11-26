package ru.mm.service;

import ru.mm.common.Account;

import java.sql.SQLException;

public interface AccountService {

    Account create(Account account) throws SQLException;

    Account get(Long id) throws SQLException;

    boolean transfer(Long source, Long target, Long amount) throws SQLException;
}
