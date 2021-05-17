package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class PlayList extends LinkedList<AudioFile> {

    // Attributes
    private int curPosIndex;
    private boolean randomOrder;

    // Constructors

    public PlayList(String pathname){
        curPosIndex = 0;
        loadFromM3U(pathname);
    }

    public PlayList(){
        curPosIndex = 0;
    }


    // Methods
    public void changeCurrent(){
        if(curPosIndex >= size()-1 || curPosIndex < 0) {
            if (randomOrder) {
                setRandomOrder(true);
            }
            setCurrent(0);
        }
        else{
            setCurrent(curPosIndex+1);
        }
    }
    public void setRandomOrder(boolean randomize){
        randomOrder = randomize;
        if(randomOrder) {
            Collections.shuffle(this);
        }

    }

    public void saveAsM3U(String pathname){
        FileWriter writer = null;
        try {
            writer = new FileWriter(pathname);
            for (int i = 0; i < size(); i++) {
                writer.write(get(i).getPathname() + System.getProperty("line.separator"));
            }
        }
        catch(IOException e){
            throw new RuntimeException("Unable to write file" + pathname +":"+ e.getMessage());
        }
        finally {
            try {
                writer.close();
            }
            catch (Exception e){
                ;
                // Swallow Exceptions
            }
        }
    }

    public void loadFromM3U(String pathname){
        Scanner scanner = null;
        String line;

        try{
            // Create a scanner
            scanner = new Scanner(new File(pathname));
            // Since this worked out we know that the file is readable
            // Read line by line
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                if(!line.trim().isEmpty() && !line.startsWith("#")){
                    try{
                        this.add(AudioFileFactory.getInstance(line));
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
        catch (IOException e){
            // e.printStackTrace();
            throw new RuntimeException(e);
        }
        finally {
            try {
                scanner.close();
            }
            catch (Exception e){
                // Swallow e
            }
        }
    }

    public void sort(SortCriterion order){
        if(order == SortCriterion.AUTHOR){
            Collections.sort(this, new AuthorComparator());
        }
        else if( order == SortCriterion.ALBUM){
            Collections.sort(this, new AlbumComparator());
        }
        else if(order == SortCriterion.TITLE){
            Collections.sort(this, new TitleComparator());
        }
        else if(order == SortCriterion.DURATION){
            Collections.sort(this, new DurationComparator());
        }
    }


    // Setter
    public void setCurrent(int newValue){
        curPosIndex = newValue;
    }

    // Getter
    public int getCurrent(){
        return curPosIndex;
    }

   public AudioFile getCurrentAudioFile() {
       if ((curPosIndex < 0) || (curPosIndex > size() - 1) || size() == 0) {
           return null;
       } else
           return get(curPosIndex);
   }



}
