package assignment2.model;

public enum CancellationReason {

    TIMEOUT ("User's transaction timed out"),
    USER_CANCELLATION ("User cancelled transaction before checkout"),
    CHANGE_NOT_AVAILABLE ("User cancelled transaction because change was not available");

    private String reason;

    CancellationReason(String reason){
        this.reason = reason;
    }

    public String getReason(){
        return reason;
    }

    public static CancellationReason getCancellationReason(String str){
        for (CancellationReason c : CancellationReason.values()){
            if (c.getReason().equals(str)){
                return c;
            }
        }
        return null;
    }
}
