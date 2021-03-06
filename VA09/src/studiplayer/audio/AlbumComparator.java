package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {
    public int compare(AudioFile af1, AudioFile af2){

        int result = 0;
        if(af1 == null || af2 == null){
            throw new NullPointerException("At least one Audiofile is empty");
        }

        if(af1 instanceof TaggedFile && af2 instanceof TaggedFile){
            result = ((TaggedFile) af1).getAlbum().compareTo(((TaggedFile) af2).getAlbum());
        }
        else if(af1 instanceof TaggedFile && !(af2 instanceof TaggedFile)){
            result = 1;
        }
        else if(!(af1 instanceof TaggedFile) && af2 instanceof TaggedFile){
            result = -1;
        }

        return result;
    }
}
