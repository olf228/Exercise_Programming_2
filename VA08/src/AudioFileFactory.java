import java.io.File;

public class AudioFileFactory {

    // Methods

    // Getter
    public static AudioFile getInstance(String pathname){
        AudioFile aff = null;
        if(new File(pathname).canRead()){
            if(pathname.toLowerCase().endsWith("wav")){
                aff = new WavFile(pathname);
            }
            else if(pathname.toLowerCase().endsWith("mp3") || pathname.toLowerCase().endsWith("ogg")){
                aff = new TaggedFile(pathname);
            }
            else{
                throw new RuntimeException("File extension can't be processed: " +pathname.substring(pathname.lastIndexOf("."), pathname.length()));
            }
        }
        else{
            throw new RuntimeException("File can't be read: "+pathname);
        }
        return aff;
    }

}
