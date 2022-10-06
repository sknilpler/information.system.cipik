package com.information.system.cipik.web.handler;


import org.apache.commons.lang.exception.ExceptionUtils;

public final class ExceptionHandlerUtils {

    private ExceptionHandlerUtils() {
    }

    public static String buildErrorMessage(Throwable t) {
        StringBuilder message =
                new StringBuilder(ExceptionUtils.getMessage(t));

        Throwable cause;
        if ((cause = t.getCause()) != null) {
            message.append(", cause: ").append(ExceptionUtils.getMessage(cause));
        }

        return message.toString();
    }
}
