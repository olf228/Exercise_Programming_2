// We still use the Junit 3 framework since the APA server is not yet migrated to JUnit 4

package studiplayer.cert;

import java.lang.reflect.Field;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isFinal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

import studiplayer.audio.AudioFile;
import studiplayer.audio.PlayList;
import studiplayer.ui.Player;
import studiplayer.ui.PlayListEditor;

import junit.framework.TestCase;

public class PlayerTest extends TestCase {
    private boolean debug = false;

    private Class<Player> clazz = Player.class;
    private final static String PLAY = "play";
    private final static String PAUSE = "pause";
    private final static String STOP = "stop";
    private final static String NEXT = "next";
    private final static String EDITOR = "editor";
    private final static String UNKNOWN = "unknown";

    public void testDefaultPlaylist() {
        String attribut = "DEFAULT_PLAYLIST";
        try {
            Field f;
            f = clazz.getDeclaredField(attribut);
            f.setAccessible(true);
            assertEquals("Typ des Attributs " + attribut, "java.lang.String", f
                    .getType().getName());
            int mod = f.getModifiers();
            assertTrue("Attribut " + attribut
                    + " sollte 'public static final' sein", isPublic(mod)
                    && isStatic(mod) && isFinal(mod));
            assertEquals("Konstanter Wert falsch",
                    "playlists/DefaultPlayList.m3u",
                    studiplayer.ui.Player.DEFAULT_PLAYLIST);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            fail("Attribut " + attribut + " existiert nicht.");
        }
    }

    public void testAttributes() {
        Hashtable<String, String> hm = new Hashtable<String, String>();
        hm.put("songDescription", "javax.swing.JLabel");
        hm.put("playTime", "javax.swing.JLabel");
        hm.put("playList", "studiplayer.audio.PlayList");
        hm.put("playListEditor", "studiplayer.ui.PlayListEditor");
        hm.put("stopped", "boolean");
        hm.put("DEFAULT_PLAYLIST", "java.lang.String");

        String attr = null;
        try {
            for (String attribut : hm.keySet()) {
                attr = attribut; // For catch clause;
                Field f = clazz.getDeclaredField(attribut);
                f.setAccessible(true);
                assertEquals("Typ des Attributs " + attribut, hm.get(attribut),
                        f.getType().getName());
                if (!"DEFAULT_PLAYLIST".equals(attribut)) {
                    int mod = f.getModifiers();
                    assertTrue("Zugriff auf " + attribut + " einschraenken",
                            isPrivate(mod));
                }
            }
        } catch (SecurityException e) {
            fail("whatever");
        } catch (NoSuchFieldException e) {
            fail("Attribut " + attr + " existiert nicht.");
        }
    }

    public void testConstructor() {
        // Test initialization of default play list
        try {
            Player player = new Player(new PlayList());
            Field fieldPlayList = clazz.getDeclaredField("playList");
            fieldPlayList.setAccessible(true);
            PlayList pl1 = (PlayList) fieldPlayList.get(player);
            assertEquals("PlayList muss initial leer sein", 0, pl1.size());
            player.dispose();
        } catch (NoSuchFieldException e) {
            fail("Kein Attribut playList definiert in Klasse Player");
        } catch (IllegalAccessException e) {
            fail(e.toString());
        }

        // Test with a special play list
        try {
            // Constants for the test depending on the test list used.
            final int correctSize = 8; // NOTE: must be adapted to size of
                                       // special play list
            final String firstSongToString = "Wellenmeister - "
                    + "TANOM Part I: Awakening - "
                    + "TheAbsoluteNecessityOfMeaning - 05:55";

            // Test parser for play lists
            // and initialization of play list
            PlayList pl = new PlayList("playlists/playList.cert.m3u");
            Player player = new Player(pl);
            Field fieldPlayList = clazz.getDeclaredField("playList");
            fieldPlayList.setAccessible(true);
            PlayList pl2 = (PlayList) fieldPlayList.get(player);
            assertEquals("Anzahl der in PlayList eingefuegten Lieder falsch",
                    correctSize, pl2.size());
            // Test initialization of play list editor
            Field fieldPlayListEditor = clazz
                    .getDeclaredField("playListEditor");
            fieldPlayListEditor.setAccessible(true);
            PlayListEditor plEditor = (PlayListEditor) fieldPlayListEditor
                    .get(player);
            assertNotNull("PlayListEditor wurde nicht korrekt erzeugt",
                    plEditor);

            // Test initialization of play list
            AudioFile currentSong = pl2.getCurrentAudioFile();
            assertNotNull("currentSong nicht gesetzt", currentSong);
            assertEquals("Attribut currentSong falsch", firstSongToString,
                    currentSong.toString());
            // Test setting of GUI label songDescription
            // Must be identical to firstSongToString
            Field fieldSongDescription = clazz
                    .getDeclaredField("songDescription");
            fieldSongDescription.setAccessible(true);
            JLabel songDescription = (JLabel) fieldSongDescription.get(player);
            assertEquals("Label songDescription falsch", firstSongToString,
                    songDescription.getText());
            // Test setting of the window title
            // Must contain (not equals) the firstSongToString
            assertTrue("Fenster-Titel falsch",
                    player.getTitle().contains(firstSongToString));
            // Cleanup
            player.dispose();
        } catch (NoSuchFieldException e) {
            fail("Kein Attribut playList definiert in Klasse Player");
        } catch (IllegalAccessException e) {
            fail(e.toString());
        }
    }

    public void testButtonLayout() {
        // Test with a special play list
        PlayList pl = new PlayList("playlists/playList.cert.m3u");

        Player player = new Player(pl);
        // Test setup of buttons
        HashMap<String, JButton> buttonList = fillButtonList(player);
        String cmdString = "@";
        for (Map.Entry<String, JButton> entry : buttonList.entrySet()) {
            JButton button = entry.getValue();
            String cmd = button.getActionCommand();
            assertFalse("Keine Action Command fuer Button " + button.getText()
                    + " gesetzt", "".equals(cmd));
            assertTrue("Zweimal gleiche Action Command",
                    cmdString.indexOf("@" + cmd + "@") < 0);
            cmdString += cmd + "@";
        }
        assertNotNull("Kein Button fuer PLAY", buttonList.get(PLAY));
        assertNotNull("Kein Button fuer PAUSE", buttonList.get(PAUSE));
        assertNotNull("Kein Button fuer STOP", buttonList.get(STOP));
        assertNotNull("Kein Button fuer NEXT", buttonList.get(NEXT));
        assertNotNull("Kein Button fuer PlayList-Editor",
                buttonList.get(EDITOR));
        // Cleanup
        player.dispose();
    }

    public void testButtons() {
        // Activate debug printing
        // debug=true;
        
        // Test with a special play list
        PlayList pl = new PlayList("playlists/playList.cert.m3u");
        // Constants for the test depending on the test list used.
        // Extract toString() of the first and third song
        final String firstSongToString = pl.get(0).toString();
        final String thirdSongToString = pl.get(2).toString();

        // Create a player
        Player player = new Player(pl);

        // A string that documents the events already occurred
        // Used for error messages
        // Initialized here
        String eventSequence = "Aktionen: <start>";

        // Collect the buttons we try to manipulate
        HashMap<String, JButton> buttonList = fillButtonList(player);
        JButton play = buttonList.get(PLAY);
        JButton pause = buttonList.get(PAUSE);
        JButton stop = buttonList.get(STOP);
        JButton next = buttonList.get(NEXT);

        // Check for correct enabling state of buttons
        assertTrue(eventSequence + " Play muss aktiviert sein",
                play.isEnabled());
        assertFalse(eventSequence + " Pause darf nicht aktiviert sein",
                pause.isEnabled());
        assertFalse(eventSequence + " Stop darf nicht aktiviert sein",
                stop.isEnabled());
        assertTrue(eventSequence + " Next muss aktiviert sein",
                next.isEnabled());

        // Press Start
        eventSequence += "<play1>";
        player.actionPerformed(new ActionEvent(player, 0, play
                .getActionCommand()));

        assertFalse(eventSequence + " Play darf nicht aktiviert sein",
                play.isEnabled());
        assertTrue(eventSequence + " Pause muss aktiviert sein",
                pause.isEnabled());
        assertTrue(eventSequence + " Stop muss aktiviert sein",
                stop.isEnabled());
        assertTrue(eventSequence + " Next muss aktiviert sein",
                next.isEnabled());
        printDebug(String.format("after play1: %s", pl.getCurrentAudioFile()));
        mySleep(1000L);

        // Press Pause (activate pause)
        eventSequence += "<pause1>";
        player.actionPerformed(new ActionEvent(player, 0, pause
                .getActionCommand()));
        // State of buttons should not have changed
        assertFalse(eventSequence + " Play darf nicht aktiviert sein",
                play.isEnabled());
        assertTrue(eventSequence + " Pause muss aktiviert sein",
                pause.isEnabled());
        assertTrue(eventSequence + " Stop muss aktiviert sein",
                stop.isEnabled());
        assertTrue(eventSequence + " Next muss aktiviert sein",
                next.isEnabled());
        printDebug(String.format("after pause1: %s", pl.getCurrentAudioFile()));
        mySleep(2000L);

        // Press Pause (resume playing)
        eventSequence += "<pause2>";
        player.actionPerformed(new ActionEvent(player, 0, pause
                .getActionCommand()));
        // State of buttons should not have changed
        assertFalse(eventSequence + " Play darf nicht aktiviert sein",
                play.isEnabled());
        assertTrue(eventSequence + " Pause muss aktiviert sein",
                pause.isEnabled());
        assertTrue(eventSequence + " Stop muss aktiviert sein",
                stop.isEnabled());
        assertTrue(eventSequence + " Next muss aktiviert sein",
                next.isEnabled());
        printDebug(String.format("after pause2: %s", pl.getCurrentAudioFile()));
        mySleep(1000L);

        // Press Stop
        eventSequence += "<stop1>";
        player.actionPerformed(new ActionEvent(player, 0, stop
                .getActionCommand()));
        // State of buttons should have changed now!
        assertTrue(eventSequence + " Play muss aktiviert sein",
                play.isEnabled());
        assertFalse(eventSequence + " Pause darf nicht aktiviert sein",
                pause.isEnabled());
        assertFalse(eventSequence + " Stop darf nicht aktiviert sein",
                stop.isEnabled());
        assertTrue(eventSequence + " Next muss aktiviert sein",
                next.isEnabled());
        printDebug(String.format("after stop1: %s", pl.getCurrentAudioFile()));

        // Here, we are stopped
        // Give threads a chance to react
        mySleep(1000L);
        try {
            Field fieldPlayTime = clazz.getDeclaredField("playTime");
            fieldPlayTime.setAccessible(true);
            JLabel ptime = (JLabel) fieldPlayTime.get(player);
            // Stop must reset playTime
            assertEquals(eventSequence + " Stop setzt playTime nicht zurueck",
                    "00:00", ptime.getText());

            // Current song must still be first song in list
            Field fieldPlaylist = clazz.getDeclaredField("playList");
            fieldPlaylist.setAccessible(true);
            assertNotNull(
                    eventSequence + " Attribut currentSong nicht gesetzt",
                    pl.getCurrentAudioFile());
            assertEquals(eventSequence + " currentSong falsch",
                    firstSongToString, pl.getCurrentAudioFile().toString());
        } catch (NoSuchFieldException e) {
            fail("Attribut existiert nicht " + e);
        } catch (IllegalAccessException e) {
            fail(e.toString());
        }
        mySleep(1000L);

        // Press Next (and start playing the second song in list)
        eventSequence += "<next1>";
        player.actionPerformed(new ActionEvent(player, 0, next
                .getActionCommand()));

        // State of buttons should have changed
        assertFalse(eventSequence + " Play darf nicht aktiviert sein",
                play.isEnabled());
        assertTrue(eventSequence + " Pause muss aktiviert sein",
                pause.isEnabled());
        assertTrue(eventSequence + " Stop muss aktiviert sein",
                stop.isEnabled());
        assertTrue(eventSequence + " Next muss aktiviert sein",
                next.isEnabled());
        printDebug(String.format("after next1: %s", pl.getCurrentAudioFile()));
        mySleep(1000L);

        // Next (this changes to the third song)
        eventSequence += "<next2>";
        player.actionPerformed(new ActionEvent(player, 0, next
                .getActionCommand()));
        // State of buttons should not have changed
        assertFalse(eventSequence + " Play darf nicht aktiviert sein",
                play.isEnabled());
        assertTrue(eventSequence + " Pause muss aktiviert sein",
                pause.isEnabled());
        assertTrue(eventSequence + " Stop muss aktiviert sein",
                stop.isEnabled());
        assertTrue(eventSequence + " Next muss aktiviert sein",
                next.isEnabled());

        try {
            // Give threads a chance to react to the 'next' command
            mySleep(1000L);
            Field fieldPlaylist = clazz.getDeclaredField("playList");
            fieldPlaylist.setAccessible(true);

            // Current song must be the third song in list
            printDebug(String.format("after next2: %s",
                    pl.getCurrentAudioFile()));
            assertNotNull(
                    eventSequence + " Attribut currentSong nicht gesetzt",
                    pl.getCurrentAudioFile());
            assertEquals(eventSequence + " currentSong falsch",
                    thirdSongToString, pl.getCurrentAudioFile().toString());

            // Check advancement of playTime
            Field fieldPlayTime = clazz.getDeclaredField("playTime");
            fieldPlayTime.setAccessible(true);
            JLabel playTime = (JLabel) fieldPlayTime.get(player);
            // Take first time probe
            String pos1 = playTime.getText();
            // Give player thread a chance to play the song a bit further
            mySleep(2000L);
            // Take second time probe
            String pos2 = playTime.getText();

            // Compare: probe 2 should be ahead of probe 1
            // Note: we compare the formatted string representations here
            assertTrue(eventSequence + " Abspielzeit nicht aktualisiert",
                    pos1.compareTo(pos2) < 0);
            printDebug(String.format("playtime pos1: %s", pos1));
            printDebug(String.format("playtime pos2: %s", pos2));

            // Finally, press stop
            eventSequence += "<stop2>";
            player.actionPerformed(new ActionEvent(player, 0, stop
                    .getActionCommand()));
            // State of buttons should have changed now!
            assertTrue(eventSequence + " Play muss aktiviert sein",
                    play.isEnabled());
            assertFalse(eventSequence + " Pause darf nicht aktiviert sein",
                    pause.isEnabled());
            assertFalse(eventSequence + " Stop darf nicht aktiviert sein",
                    stop.isEnabled());
            assertTrue(eventSequence + " Next muss aktiviert sein",
                    next.isEnabled());
            printDebug(String.format("after stop2: %s",
                    pl.getCurrentAudioFile()));
        } catch (NoSuchFieldException e) {
            fail("Attribut existiert nicht " + e);
        } catch (IllegalAccessException e) {
            fail(e.toString());
        }

        // Cleanup
        // Play it save
        studiplayer.basic.BasicPlayer.stop();
        player.dispose();

    }

    private void mySleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Nothing useful to be done here
        }
    }

    @SuppressWarnings("rawtypes")
    public void testInnerClasses() {
        Class[] classes = clazz.getDeclaredClasses();
        String c = Arrays.asList(classes).toString();
        assertTrue("TimerThread nicht als innere Klasse implementiert",
                c.indexOf("studiplayer.ui.Player$TimerThread") >= 0);
        assertTrue("PlayerThread nicht als innere Klasse implementiert",
                c.indexOf("studiplayer.ui.Player$PlayerThread") >= 0);
        for (int i = 0; i < classes.length; i++) {
            if (classes[i].getName().contains("Player$1"))
                continue;
            if (classes[i].getEnclosingClass() != null) {
                assertTrue(
                        "Innere Klasse ist kein Thread",
                        classes[i].getSuperclass().equals(
                                java.lang.Thread.class)
                                || classes[i].getSuperclass().equals(
                                        javax.swing.JFrame.class));
            }
        }
    }

    // Helper methods

    // Collect all JButtons of all (sub-)components of the traversee
    // and return them as a hash map
    private HashMap<String, JButton> fillButtonList(Container traversee,
            HashMap<String, JButton> map) {
        for (Component c : traversee.getComponents()) {
            if (c instanceof JButton) {
                String buttonName = UNKNOWN;
                String defIcon = ((JButton) c).getIcon().toString();
                if (defIcon.endsWith("play.png"))
                    buttonName = PLAY;
                else if (defIcon.endsWith("pause.png"))
                    buttonName = PAUSE;
                else if (defIcon.endsWith("stop.png"))
                    buttonName = STOP;
                else if (defIcon.endsWith("next.png"))
                    buttonName = NEXT;
                else if (defIcon.endsWith("pl_editor.png"))
                    buttonName = EDITOR;
                map.put(buttonName, (JButton) c);
            } else if (c instanceof Container) {
                fillButtonList((Container) c, map);
            }
        }
        return map;
    }

    // A wrapper for fillButtonList(2)
    private HashMap<String, JButton> fillButtonList(Container traversee) {
        return this.fillButtonList(traversee, new HashMap<String, JButton>());
    }

    // Printing of debug messages
    // Depends on attribute 'debug' of this class
    private void printDebug(String msg) {
        if (this.debug) {
            System.out.printf("DEBUG:%s\n", msg);
        }
    }

}
