package studiplayer.audio;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

    public WavFile() throws NotPlayableException {
    }
    public WavFile(String path) throws NotPlayableException {
        super(path);
        try{
            readAndSetDurationFromFile(getPathname());
        }
        catch(Exception e){
            throw new NotPlayableException(getPathname(), "Not playable");
        }


    }

    // Methods
    public static long computeDuration(long numberOfFrames, float frameRate){
        return (long) (((float) numberOfFrames /  frameRate) * 1000000);
    }
    public void readAndSetDurationFromFile(String pathname) throws NotPlayableException {
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
