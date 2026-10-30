package ca.hccis.squash.bo;

import ca.hccis.squash.jpa.entity.SkillsAssessmentSquashTechnical;

public class SkillsAssessmentSquashTechnicalBO {

    public static final int POINTS_FOREHAND_DRIVES = 15;
    public static final int POINTS_BACKHAND_DRIVES = 15;
    public static final int POINTS_VOLLEY_MAX = 8;
    public static final int POINTS_VOLLEY_SUM = 5;

    /**
     * Calculate the score based on attribute skill values
     *
     * @return the technical score
     * @author BJM
     * @since 20250915
     */
    public static int calculateTechnicalScore(SkillsAssessmentSquashTechnical skillsAssessmentSquashTechnical) {
        int score = 0;

        score = skillsAssessmentSquashTechnical.getForehandDrives() * POINTS_FOREHAND_DRIVES;
        score += skillsAssessmentSquashTechnical.getBackhandDrives() * POINTS_BACKHAND_DRIVES;
        score += skillsAssessmentSquashTechnical.getForehandVolleyMax() * POINTS_VOLLEY_MAX;
        score += skillsAssessmentSquashTechnical.getForehandVolleySum() * POINTS_VOLLEY_SUM;
        score += skillsAssessmentSquashTechnical.getBackhandVolleyMax() * POINTS_VOLLEY_MAX;
        score += skillsAssessmentSquashTechnical.getBackhandVolleySum() * POINTS_VOLLEY_SUM;

        skillsAssessmentSquashTechnical.setTechnicalScore(score);
        return score;
    }

}
