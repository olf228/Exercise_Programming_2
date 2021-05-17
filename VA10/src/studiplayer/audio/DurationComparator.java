package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
    public int compare(AudioFile af1, AudioFile af2){
        int result = 0;
        if(af1 == null || af2 == null){
            throw new NullPointerException("At least one Audiofile is empty!");
        }

        result = af1.getFormattedDuration().compareTo(af2.getFormattedDuration());

        /*if(af1 instanceof TaggedFile && af2 instanceof TaggedFile){

            result = af1.getFormattedDuration().compareTo(af2.getFormattedDuration());
        }
        else if(af1 instanceof TaggedFile){
            result = af1.getFormattedDuration().compareTo(af2.getFormattedDuration());
        }
        else if(af2 instanceof TaggedFile){
            result = af1.getFormattedDuration().compareTo(af2.getFormattedDuration());
        }
        else{
            result = af1.getFormattedDuration().compareTo(af2.getFormattedDuration());
        }*/
        return result;

    }
}
