package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile>{
    public int compare(AudioFile af1, AudioFile af2){
        if(af1 == null || af2 == null){
            throw new NullPointerException("At least one Audiofile is empty");
        }
        else{
            return af1.getTitle().compareTo(af2.getTitle());
        }
    }
}
