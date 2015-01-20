package mod6.texttosoundtrack.echonest;

public enum EchonestMood {
    ACTION ("aggresive"), BOMBASTIC("happy"), EERINESS("haunting"), HAPPY("happy"), NEUTRAL("ambient"), PEACEFUL("peaceful"), ROMANTIC("romantic"), SAD("sad"), SILENCE("calming"), THRILLING
            ("thrilling");

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
