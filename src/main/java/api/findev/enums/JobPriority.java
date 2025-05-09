package api.findev.enums;
import lombok.Getter;

@Getter
public enum JobPriority {
    HIGH(3),
    MEDIUM(2),
    LOW(1);

    private final int level;

    JobPriority(int level) {
        this.level = level;
    }

    public static JobPriority fromLevel(int level) {
        for (JobPriority priority : values()) {
            if (priority.level == level) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid priority level: " + level);
    }
}
