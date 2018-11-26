package ru.mm.common;

import java.util.Objects;

public class Account {

    private Long id;
    private Long balance;

    public Account() {
    }

    public Account(Long id, Long balance) {
        this.id = id;
        this.balance = balance;
    }

    public Account(Long balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, balance);
    }
}
