package demo.task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

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


}
