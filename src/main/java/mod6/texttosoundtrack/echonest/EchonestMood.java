package mod6.texttosoundtrack.echonest;

public enum EchonestMood {
    ACTION ("energetic"), BOMBASTIC("happy"), EERINESS("eerie"), HAPPY("happy"), NEUTRAL("ambient"), PEACEFUL("peaceful"), ROMANTIC("romantic"), SAD("sad"), SILENCE("calming"), THRILLING("cool");

    private String echonestMood;

    /**
     * Maps locally used mood names to echonest mood names
     * @param echonestMood
     */
    private EchonestMood(String echonestMood) {
        this.echonestMood = echonestMood;
    }

    public String getEchonestMood() {
        return echonestMood;
    }
}
