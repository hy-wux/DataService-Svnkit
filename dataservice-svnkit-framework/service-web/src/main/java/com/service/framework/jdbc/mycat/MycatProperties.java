package com.service.framework.jdbc.mycat;

enum Status {
    keep, remove
}

public class MycatProperties {

    /**
     * The status setting the mycat hint. You can set with value keep or remove.
     */
//    private String hint;
//
//    public String getHint() {
//        return hint;
//    }
//
//    public void setHint(String hint) {
//        this.hint = hint;
//    }
    /**
     * The status setting the mycat hint. You can set with value keep or remove.
     */
    private Status hint;

    public Status getHint() {
        return hint;
    }

    public void setHint(Status hint) {
        this.hint = hint;
    }
}
