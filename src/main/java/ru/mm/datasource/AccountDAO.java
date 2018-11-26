package ru.mm.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.mm.common.Account;
import ru.mm.exception.AccountException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.*;

public class AccountDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDAO.class);

    private static final String DB_URL = "jdbc:h2:~/mem";
    private static final String USER = "sa";
    private static final String PASS = "";

    private HikariDataSource ds;

    private final String GET_ACCOUNT = "SELECT * FROM accounts WHERE id=?";
    private final String ADD_ACCOUNT = "INSERT INTO accounts(balance) VALUES(?)";
    private final String UPDATE_ACCOUNT = "UPDATE accounts SET balance =? WHERE id =?";

    @PostConstruct
    public void init() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("minimum-idle", "1");
        config.addDataSourceProperty("maximum-pool-size", "20");
        config.setConnectionInitSql("CREATE TABLE IF NOT EXISTS accounts (id integer auto_increment, balance integer)");
        ds = new HikariDataSource(config);

    }

    @PreDestroy
    public void close() {
        ds.close();
    }


    public Account add(Account account) throws SQLException {
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(ADD_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, account.getBalance());
            int newRows = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (newRows != 0 && rs.next()) {
                account.setId(rs.getLong(1));
            } else {
                String message = "Account is not created";
                LOGGER.error(message);
                throw new AccountException(message);
            }

            return account;
        }
    }

    public Account get(@NotNull Long id) throws SQLException {
        if (id == null) {
            final String message = "Account with id doesn't exist";
            LOGGER.error(message);
            throw new AccountException(message);
        }
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ACCOUNT)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Account(rs.getLong(1), rs.getLong(2));
            } else {
                final String message = "Account with id doesn't exist";
                LOGGER.error(message);
                throw new AccountException(message);
            }

        }
    }

    public boolean transfer(@NotNull Long source, @NotNull Long target, @NotNull Long amount) throws SQLException {

        final Account sourceAccount = get(source);
        final Account targetAccount = get(target);
        if (sourceAccount.getBalance() - amount < 0) {
            final String message = "Low balance";
            LOGGER.warn(message);
            throw new AccountException(message);
        } else {
            try (Connection con = ds.getConnection();
                 PreparedStatement updateSource = con.prepareStatement(UPDATE_ACCOUNT);
                 PreparedStatement updateTarget = con.prepareStatement(UPDATE_ACCOUNT)) {
                con.setAutoCommit(false);
                updateSource.setLong(1, sourceAccount.getBalance() - amount);
                updateSource.setLong(2, sourceAccount.getId());
                updateSource.execute();

                updateTarget.setLong(1, targetAccount.getBalance() + amount);
                updateTarget.setLong(2, targetAccount.getId());
                updateTarget.execute();
                con.commit();
            }
        }
        return true;
    }
}
