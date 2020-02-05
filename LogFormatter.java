import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    // Create a DateFormat to format the logger timestamp.
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
    
    public static String loggingfor ;
    
    public String format(LogRecord record) {
        StringBuilder FinalFormat = new StringBuilder(1000);
        FinalFormat.append(dateFormat.format(new Date(record.getMillis()))).append(" - ");
        FinalFormat.append("[" + loggingfor + " - log").append("] - ");
        
        String newline = System.getProperty("line.separator");
        FinalFormat.append(newline);
        
        FinalFormat.append("[").append(record.getLevel()).append("] -");
       
        FinalFormat.append(formatMessage(record));
        
        FinalFormat.append(newline);
        FinalFormat.append(newline);
        
        return FinalFormat.toString();
    }

    public String getHead(Handler h) {
        return super.getHead(h);
    }

    public String getTail(Handler h) {
        return super.getTail(h);
    }

	
	
}