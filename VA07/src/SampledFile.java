import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {


    public SampledFile() {
        super();
    }

    public SampledFile(String path) {
        super(path);
    }

    // Methods
    /**
     * Converts the microtime into time format minutes:seconds
     */
    public static String timeFormatter(long microtime){
        String result;
        // Exceptions
        if(microtime < 0L){
            throw new RuntimeException("Negative time value provided!");
        }
        if(microtime > 5999999999L){
            throw new RuntimeException("Time value exceeds allowed format!");
        }

        long seconds = microtime /1000000L;

        long minutes = seconds / 60;

        seconds = seconds - minutes*60;

        if(minutes < 10L){
            if(seconds < 10L){
                result = "0"+minutes+":0"+seconds;
            }
            else{
                result = "0"+minutes+":"+seconds;
            }
        }
        else{
            if(seconds < 10L){
                result = ""+minutes+":0"+seconds;
            }
            else{
                result = ""+minutes+":"+seconds;
            }

        }
        return result;
    }

    /**
     * Initiates the playback of a certain song, which is named in the filename variable0
     */
    public void play() {
        studiplayer.basic.BasicPlayer.play(getPathname());
    }

    /**
     * Toggles the music playback (Resume / Pause)
     */
    public void togglePause() {
        studiplayer.basic.BasicPlayer.togglePause();
    }

    /**
     * Stops the music playback
     */
    public void stop() {
        studiplayer.basic.BasicPlayer.stop();
    }

    /**
     * Returns the duration of playback of given file
     */
    public String getFormattedDuration() {
            return timeFormatter(duration);
    }

    /**
     * Returns the current playback position of the file
     */
    public String getFormattedPosition() {
        try {
            return timeFormatter(BasicPlayer.getPosition());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

