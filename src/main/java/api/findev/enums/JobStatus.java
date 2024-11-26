package api.findev.enums;

public enum JobStatus {
    ACTIVE(1),
    INACTIVE(0);

    private final int value;

    JobStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static JobStatus fromValue(int value) {
        for (JobStatus status : JobStatus.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
