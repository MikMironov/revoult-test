import ru.mm.common.Account;
import ru.mm.datasource.AccountDAO;
import ru.mm.exception.AccountException;
import org.junit.*;

import java.sql.SQLException;

public class AppTest {

    private static AccountDAO dao;

    @BeforeClass
    public static void init(){
        dao = new AccountDAO();
        dao.init();
    }

    @AfterClass
    public static void close() {
        dao.close();
    }

    @Test
    public void createAccount() throws SQLException {
        Account account = new Account(555L);
        account = dao.add(account);
        Assert.assertNotNull(account.getId());
    }

    @Test
    public void getAccount() throws SQLException {
        Account account = new Account(555L);
        account = dao.add(account);

        Account fromDb = dao.get(account.getId());
        Assert.assertEquals(fromDb, account);
    }

    @Test
    public void correctTransferTest() throws SQLException {
        Account source = dao.add(new Account(555L));

        Account target = dao.add(new Account(0L));

        final Long amount = 500L;

        Assert.assertTrue(dao.transfer(source.getId(), target.getId(), amount));

        Account getSource = dao.get(source.getId());
        Assert.assertEquals(Long.valueOf(source.getBalance() - amount), getSource.getBalance());

        Account getTarget = dao.get(target.getId());
        Assert.assertEquals(Long.valueOf(target.getBalance() + amount), getTarget.getBalance());
    }

    @Test
    public void wrongSourceTest() throws SQLException {
        try {
            Account source = dao.add(new Account(555L));
            dao.transfer(source.getId(), null, 500L);
        } catch (AccountException e) {
            Assert.assertEquals(e.getMessage(), "Account with id doesn't exist");
        }
    }

    @Test
    public void lowBalanceTest() throws SQLException {
        Account source = dao.add(new Account(400L));

        Account target = dao.add(new Account(0L));

        final Long amount = 500L;

       try {
           dao.transfer(source.getId(), target.getId(), amount);
       } catch (AccountException e ) {
           Assert.assertEquals(e.getMessage(), "Low balance");
       }
    }
}
