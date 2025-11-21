import java.util.Scanner;

public interface IQuestion {
    String getText();

    // Tüm süreci (yazdırma, cevap alma, kontrol etme) bu metod yönetecek
    boolean ask(Scanner scanner);
}