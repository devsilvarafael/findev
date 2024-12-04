package api.findev.enums;

public enum CandidatureStatus {
    /**
     * Candidature submitted but not yet reviewed.
     */
    PENDING_REVIEW,

    /**
     * Candidature has been reviewed and is under consideration.
     */
    UNDER_REVIEW,

    /**
     * Candidate has been invited for an interview.
     */
    INTERVIEW_INVITED,

    /**
     * Candidate attended the interview, and feedback is pending.
     */
    INTERVIEW_COMPLETED,

    /**
     * Feedback has been provided to the candidate.
     */
    FEEDBACK_PROVIDED,

    /**
     * Candidature has been accepted for the position.
     */
    ACCEPTED,

    /**
     * Candidature has been rejected.
     */
    REFUSED,

    /**
     * Candidate has withdrawn their application.
     */
    WITHDRAWN,

    /**
     * Candidate is hired and the process is complete.
     */
    HIRED
}
