package studiplayer.audio;

import studiplayer.basic.TagReader;

import java.util.Map;

public class TaggedFile extends SampledFile {

    private String album ="";
    // Constructors

    public TaggedFile() throws NotPlayableException {
    }

    public TaggedFile(String s) throws NotPlayableException {
        super(s);
        readAndStoreTags(getPathname());
    }

    // Methods

    @Override
    public String toString () {
        if (album.isEmpty()) {
            return super.toString() +" - "+getFormattedDuration();
        } else {
            return super.toString()+" - "+getAlbum()+" - "+getFormattedDuration();
        }
    }

    /**
     * Reads the filename and extracts tags, if there are any
     */
    public void readAndStoreTags(String pathname) throws NotPlayableException {
        try{
            Map<String, Object> tags;
            tags = TagReader.readTags(pathname);

            if (tags.get("title") instanceof String && !((String)tags.get("title")).isEmpty()){
                title = ((String) tags.get("title")).trim();
            }
            if (tags.get("author") instanceof String && !((String)tags.get("author")).isEmpty()) {
                author = ((String) tags.get("author")).trim();
            }
            if (tags.get("album") instanceof String && !((String)tags.get("album")).isEmpty()) {
                album = ((String) tags.get("album")).trim();
            }
            if (((long) tags.get("duration")) != 0L) {
                duration = (long) tags.get("duration");
            }
        }
        catch(RuntimeException e){
            throw new NotPlayableException(getPathname(), e.getMessage());
        }

    }

    public String [] fields(){

        return new String[] {getAuthor(), getTitle(), getAlbum(), getFormattedDuration()};
    }
    // Get Album
    public String getAlbum(){
        return  album;
    }

}
