package com.hawen;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static Logger log = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        log.info("-------------project run-------------");
        DeleteProgram.run();
        log.info("-------------project exit-------------");
    }

}
