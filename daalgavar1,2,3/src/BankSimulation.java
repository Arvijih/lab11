import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class BankAccount {
    private int balance = 1000;

    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println(Thread.currentThread().getName() +
            " deposited " + amount + ", Balance: " + balance);
    }

    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() +
                " withdrew " + amount + ", Balance: " + balance);
        } else {
            System.out.println(Thread.currentThread().getName() +
                " attempted to withdraw " + amount + " but insufficient funds. Balance: " + balance);
        }
    }

    public int getBalance() {
        return balance;
    }
}

class Customer implements Runnable {
    private final BankAccount account;
    private final String action;
    private final int amount;

    public Customer(BankAccount account, String action, int amount) {
        this.account = account;
        this.action = action;
        this.amount = amount;
    }

    @Override
    public void run() {
        if ("deposit".equalsIgnoreCase(action)) {
            account.deposit(amount);
        } else if ("withdraw".equalsIgnoreCase(action)) {
            account.withdraw(amount);
        } else {
            System.out.println("Unknown action: " + action);
        }
    }
}

public class BankSimulation {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(new Customer(account, "deposit", 500));
        executor.execute(new Customer(account, "withdraw", 700));
        executor.execute(new Customer(account, "withdraw", 600));

        executor.shutdown();
    }
}
