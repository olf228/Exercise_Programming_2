import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

    public WavFile(){
    }
    public WavFile(String path){
        super(path);
        readAndSetDurationFromFile(getPathname());

    }

    // Methods
    public static long computeDuration(long numberOfFrames, float frameRate){
        return (long) (((float) numberOfFrames /  frameRate) * 1000000);
    }
    public void readAndSetDurationFromFile(String pathname){
        WavParamReader.readParams(pathname);
        duration = computeDuration(WavParamReader.getNumberOfFrames(), WavParamReader.getFrameRate());
    }
    public String[] fields(){
        return new String[]{getAuthor(), getTitle(), "", getFormattedDuration()};
    }
    @Override
    public String toString(){
        return super.toString() + " - " + getFormattedDuration();
    }

}
