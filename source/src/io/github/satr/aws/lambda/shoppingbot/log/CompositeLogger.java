package io.github.satr.aws.lambda.shoppingbot.log;
// Copyright © 2017, github.com/satr, MIT License

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import io.github.satr.aws.lambda.shoppingbot.log.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class CompositeLogger implements Logger {
    private List<Logger> loggers = new ArrayList<>();
    private LambdaLogger lambdaLogger;

    @Override
    public void log(String message) {
        System.out.println(message);

        for (Logger logger : loggers)
            logger.log(message);

        if(lambdaLogger!= null)
            lambdaLogger.log(message);
    }

    @Override
    public void log(Exception e) {
        PrintWriter pw = null;
        try {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log(sw.toString());
        } finally {
            if(pw != null)
                pw.close();
        }
    }

    public void setLambdaLogger(LambdaLogger logger) {
        this.lambdaLogger = logger;
    }

    public void addLogger(Logger logger) {
        if(!loggers.contains(logger))
            loggers.add(logger);
    }
}
