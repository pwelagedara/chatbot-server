package com.pnc.microservices.openapi.chatbot.util;

import java.text.SimpleDateFormat;

/**
 * @author Palamayuran
 */
public interface DateConstants {
    String DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT_STRING);

    String DISPLAY_DATE_FORMAT_STRING = "yyyy-MM-dd' at 'hh:mm' 'a";
    SimpleDateFormat DISPLAY_DATE_FORMATTER = new SimpleDateFormat(DISPLAY_DATE_FORMAT_STRING);
}
