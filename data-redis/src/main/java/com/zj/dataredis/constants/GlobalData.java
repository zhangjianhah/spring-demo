package com.zj.dataredis.constants;

public class GlobalData {

    private static String EMAIL_QUEUE_WAIT = "email_queue_wait";
    private static String EMAIL_QUEQU_EXECUTE = "email_queue_execute";

    public static String getEmailQueueWait() {
        return EMAIL_QUEUE_WAIT;
    }

    public static String getEmailQuequExecute() {
        return EMAIL_QUEQU_EXECUTE;
    }
}
