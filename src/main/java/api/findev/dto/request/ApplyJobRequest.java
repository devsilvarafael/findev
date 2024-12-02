package api.findev.dto.request;

import java.util.UUID;

public class ApplyJobRequest {
    private UUID developerId;

    public UUID getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(UUID developerId) {
        this.developerId = developerId;
    }
}
