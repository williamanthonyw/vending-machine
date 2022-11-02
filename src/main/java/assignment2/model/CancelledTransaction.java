package assignment2.model;

import java.time.LocalDateTime;

public class CancelledTransaction {

    private LocalDateTime timeCancelled;
    private CancellationReason cancellationReason;
    private String username;

    public CancelledTransaction(String username, CancellationReason cancellationReason, LocalDateTime timeCancelled){
        this.username = username;
        this.cancellationReason = cancellationReason;
        this.timeCancelled = timeCancelled;
    }

    public LocalDateTime getTimeCancelled() {
        return timeCancelled;
    }

    public void setTimeCancelled(LocalDateTime timeCancelled) {
        this.timeCancelled = timeCancelled;
    }

    public CancellationReason getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(CancellationReason cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
