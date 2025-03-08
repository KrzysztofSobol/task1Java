package demo.task1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class BankImpl implements Bank {

    private AccountRepository accountRepository;

    public BankImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Long createAccount(String name, String address) {
        Optional<Account> account = accountRepository.findByNameAndAddress(name, address);
        if (account.isPresent()) { return account.get().getId(); }
        return accountRepository.create(name,address,BigDecimal.ZERO).getId();
    }

    @Override
    public Long findAccount(String name, String address) {
        return null;
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {

    }

    @Override
    public BigDecimal getBalance(Long id) {
        return null;
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {

    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {

    }
}
