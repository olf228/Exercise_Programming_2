import jdk.jshell.spi.ExecutionControl;
import org.junit.Test;

import javax.swing.text.html.HTML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class UTestPlayList2 {
    @Test
    public void test_getCurrentAudioFile_01() throws Exception {
        PlayList pl = new PlayList();
        assertEquals("Wrong current AudioFile", null, pl.getCurrentAudioFile());
    }


    @Test
    public void test_getCurrentAudioFile_02() throws Exception {
        PlayList pl = new PlayList();
        TaggedFile tf0 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
        pl.add(tf0);
        pl.setCurrent(10); // Wrong index; however, the setter is not checked
        // However, getCurrentAudioFile() is checked
        assertEquals("Wrong current AudioFile", null, pl.getCurrentAudioFile());
    }

    @Test
    public void test_getCurrentAudioFile_04() throws Exception {
        PlayList pl = new PlayList();
        TaggedFile tf0 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
        TaggedFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
        pl.add(tf0);
        pl.add(tf1);
        pl.setCurrent(1);
        assertEquals("Wrong current AudioFile", tf1, pl.getCurrentAudioFile());

        pl.remove(0);   // Removing the first element invalidates current index
        // pointing at position 1. Now, list is too short
        assertEquals("Wrong current AudioFile", null, pl.getCurrentAudioFile());
    }

    @Test
    public void test_changeCurrent_01() throws Exception {
        PlayList pl = new PlayList();
        TaggedFile tf0 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
        TaggedFile tf1 = new TaggedFile("audiofiles/tanom p2 journey.mp3");
        TaggedFile tf2 = new TaggedFile("audiofiles/Rock 812.mp3");
        pl.add(tf0);
        pl.add(tf1);
        pl.add(tf2);
        pl.setCurrent(0);
        assertEquals("Wrong current index", 0, pl.getCurrent());
        pl.changeCurrent();
        assertEquals("Wrong change in current index", 1, pl.getCurrent());
        pl.changeCurrent();
        assertEquals("Wrong change in current index", 2, pl.getCurrent());
        pl.changeCurrent();
        assertEquals("Wrong change in current index", 0, pl.getCurrent());
    }

    @Test
    public void test_changeCurrent_02() throws Exception {
        PlayList pl = new PlayList();
        TaggedFile f0 = new TaggedFile("audiofiles/Eisbach Deep Snow.ogg");
        TaggedFile f1 = new TaggedFile("audiofiles/tanom p2 journey.mp3");
        TaggedFile f2 = new TaggedFile("audiofiles/Rock 812.mp3");
        WavFile f4 = new WavFile("audiofiles/wellenmeister - tranquility.wav");
        pl.add(f0);
        pl.add(f1);
        pl.add(f2);
        pl.add(f4);
        pl.setRandomOrder(true);
        // Note: Only the content of the list is shuffled ;)
        for (int i = 0; i < 5 * pl.size(); i++) {
            System.out.printf("Pos=%d Filename=%s\n", pl.getCurrent(), pl.getCurrentAudioFile().getFilename());
            assertEquals("Wrong current index", i % pl.size(), pl.getCurrent());
            pl.changeCurrent();
            if (pl.getCurrent() == 0) {
                System.out.println("");
            }
        }
    }

    @Test
    public void test_WriteLinesToFile_01() throws Exception{
        FileWriter writer = null;
        // Writes lines to file using line.separator
        String fname = "file.txt";
        String linesep = System.getProperty("line.separator");
        // String linesep = "\n"
        try {
            // Create a FileWriter
            writer = new FileWriter(fname);
            // Since this worked out we know that the file is writable
            // Write some strings to file
            writer.write("Line1" + linesep);
            writer.write("Line2" + linesep);
            writer.write("Line3" + linesep);
        }
        catch (IOException e){
            throw new RuntimeException("Unable to write to file" + fname + ":" + e.getMessage());
        }
        finally {
            // Close the file handle in any case!
            try {
                writer.close();
            }
            catch (Exception e){
                // Just swallow any exception caused y the finally block
            }
        }
    }

    @Test
    public void test_ReadLinesFromFile_01() throws Exception{
        String fname = "file.txt";
        Scanner scanner = null;
        String line;
        try{
            // Create a scanner
            scanner = new Scanner(new File(fname));
            // Since this worked out we know that the file is readable
            // Read line by line
            int i = 1;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                System.out.println("Got the line "+i+"|"+line+"|");
                i++;
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

}
