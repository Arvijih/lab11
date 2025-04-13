import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Банкны дансны класс
class BankAccount {
    private int balance = 1000; // Эхний баланс: 1000₮

    // synchronized түлхүүр үг нь олон урсгал зэрэгцэн энэ функцэд хандахаас сэргийлнэ
    public synchronized void deposit(int amount) {
        balance += amount;
        System.out.println("Deposited " + amount + ", Balance: " + balance);
    }

    // synchronized түлхүүр үг нь олон урсгал зэрэгцэн энэ функцэд хандахаас сэргийлнэ
    public synchronized void withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn " + amount + ", Balance: " + balance);
        } else {
            System.out.println("Insufficient funds for " + amount);
        }
    }

    // Балансын утгыг буцаана
    public int getBalance() {
        return balance;
    }
}

// Хэрэглэгчийг төлөөлөх класс
class Customer implements Runnable {
    private BankAccount account; // Хэрэглэгчийн данс
    private String action;       // Гүйлгээний төрөл: "deposit" эсвэл "withdraw"
    private int amount;          // Гүйлгээний дүн

    // Конструктор
    public Customer(BankAccount account, String action, int amount) {
        this.account = account;
        this.action = action;
        this.amount = amount;
    }

    // Runnable интерфэйсийн run() функц
    public void run() {
        if (action.equals("deposit")) {
            account.deposit(amount); // Хадгаламж хийх
        } else if (action.equals("withdraw")) {
            account.withdraw(amount); // Мөнгө татах
        }
    }
}

// Үндсэн симуляцийн класс
public class BankSimulation {
    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount(); // Шинэ банкны данс үүсгэх

        // 2 урсгалтай ExecutorService үүсгэх
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Гурван хэрэглэгчийн гүйлгээг executor-д оруулах
        executor.submit(new Customer(account, "deposit", 500));   // 500₮ хадгалах
        executor.submit(new Customer(account, "withdraw", 700));  // 700₮ татах
        executor.submit(new Customer(account, "withdraw", 600));  // 600₮ татах

        // ExecutorService-г хаах (шинэ даалгавар хүлээж авахгүй)
        executor.shutdown();

        // Бүх гүйлгээ дуусахыг хүлээх
        while (!executor.isTerminated()) {
            Thread.sleep(100); // 100 миллисекунд хүлээх
        }

        // Эцсийн балансыг хэвлэх
        System.out.println("Final Balance: " + account.getBalance());
    }
}
