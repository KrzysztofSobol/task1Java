package demo.task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BankTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BankImpl bank;

    @Test
    void test_create_account() {
        when(accountRepository.create("x","y", BigDecimal.ZERO)).thenReturn(new Account(1L,"x","y", BigDecimal.ZERO));
        when(accountRepository.findByNameAndAddress(anyString(),anyString())).thenReturn(Optional.empty());

        Long id = bank.createAccount("x","y");
        assert id != null;

        verify(accountRepository).create("x","y", BigDecimal.ZERO);
        verify(accountRepository).findByNameAndAddress(anyString(),anyString());
    }

    @Test
    void test_create_account_if_account_exists() {
        Long id = bank.createAccount("x","y");
        Long id2 = bank.createAccount("x","y");

        assert id.equals(id2);
    }

    @Test
    void test_findAccount() {
        Long id = bank.createAccount("x","y");
        Long foundId = bank.findAccount("x","y");

        assert id.equals(foundId);
    }

    @Test
    void test_findAccount_if_account_doesnt_exists() {
        Long foundId = bank.findAccount("x","y");

        assert foundId == null;
    }

    @Test
    void test_deposit() {
        Long id = bank.createAccount("x","y");

        bank.deposit(id, BigDecimal.valueOf(1));
        bank.deposit(id, BigDecimal.valueOf(10));
        bank.deposit(id, BigDecimal.valueOf(100));

        BigDecimal result = bank.getBalance(id);

        assertEquals(result, BigDecimal.valueOf(111));
    }

    @Test
    void test_deposit_zero() {
        Long id = bank.createAccount("x","y");

        bank.deposit(id, BigDecimal.valueOf(0));

        BigDecimal result = bank.getBalance(id);

        assertEquals(result, BigDecimal.ZERO);
    }

    @Test
    void test_deposit_negative() {
        Long id = bank.createAccount("x","y");

        bank.deposit(id, BigDecimal.valueOf(-10));

        BigDecimal result = bank.getBalance(id);

        assert result.equals(BigDecimal.valueOf(-10));
    }

    @Test
    void test_deposit_floats() {
        Long id = bank.createAccount("x","y");

        bank.deposit(id, BigDecimal.valueOf(5.5));
        bank.deposit(id, BigDecimal.valueOf(5.5));
        bank.deposit(id, BigDecimal.valueOf(10.9));

        BigDecimal result = bank.getBalance(id);

        assertEquals(result, BigDecimal.valueOf(21.9));
    }

    @Test
    void test_deposit_null() {
        Long id = bank.createAccount("x","y");

        bank.deposit(id, null);

        BigDecimal result = bank.getBalance(id);

        assertEquals(result, BigDecimal.ZERO);
    }

    @Test
    void test_deposit_not_existing_id() {
        assertThrows(Bank.AccountIdException.class, () -> bank.deposit(1L, BigDecimal.ZERO));
    }

    @Test
    void test_deposit_null_id() {
        assertThrows(Bank.AccountIdException.class, () -> bank.deposit(null, BigDecimal.ZERO));
    }

    @Test
    void test_deposit_when_account_doesnt_exists() {
        assertThrows(Bank.AccountIdException.class, () -> bank.deposit(1L, BigDecimal.ONE));
    }

    @Test
    void test_getBalance() {
        Long id = bank.createAccount("x","y");

        bank.deposit(id, BigDecimal.ONE);
        BigDecimal result = bank.getBalance(id);

        assertEquals(result, BigDecimal.ONE);
    }

    @Test
    void test_getBalance_with_no_previous_deposits() {
        Long id = bank.createAccount("x","y");

        BigDecimal balance = bank.getBalance(id);

        assertEquals(balance, BigDecimal.ZERO);
    }

    @Test
    void test_getBalance_when_account_doesnt_exists() {
        assertThrows(Bank.AccountIdException.class, () -> bank.deposit(1L, BigDecimal.ONE));
    }

    @Test
    void test_getBalance_when_null_id() {
        assertThrows(Bank.AccountIdException.class, () -> bank.deposit(null, BigDecimal.ONE));
    }

    @Test
    void test_withdraw() {
        Long id = bank.createAccount("x","y");
        bank.deposit(id, BigDecimal.ONE);
        bank.withdraw(id, BigDecimal.ONE);
        BigDecimal balance = bank.getBalance(id);

        assertEquals(balance, BigDecimal.ONE);
    }

    @Test
    void test_withdraw_null() {
        Long id = bank.createAccount("x","y");
        bank.withdraw(id, null);
        BigDecimal balance = bank.getBalance(id);

        assertEquals(balance, BigDecimal.ZERO);
    }

    @Test
    void test_withdraw_when_account_doesnt_exists() {
        assertThrows(Bank.AccountIdException.class, () -> bank.withdraw(1L, BigDecimal.ONE));
    }

    @Test
    void test_withdraw_when_null_id() {
        assertThrows(Bank.AccountIdException.class, () -> bank.withdraw(null, BigDecimal.ONE));
    }
}
