package commons;

import java.util.ArrayList;
import java.util.List;

public class Debugger {

    private final List<String> log;

    public Debugger() {
        log = new ArrayList<>();
    }

    public void log(String message){
        log.add(message);
    }

    public List<String> getLog(){
        return log;
    }
}
