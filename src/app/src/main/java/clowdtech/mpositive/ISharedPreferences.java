package clowdtech.mpositive;

public interface ISharedPreferences {
    boolean getCashDrawerIntegrated();

    boolean getPrinterIntegration();

    String getPrinterCommandType();

    String getPrinterHeader();

    String getPrinterFooter();

    String getPrinterName();

    int getPrinterCommunicationType();

    int getRetry();
}
