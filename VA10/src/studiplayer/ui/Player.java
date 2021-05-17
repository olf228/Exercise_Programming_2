package studiplayer.ui;

import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Player extends JFrame implements ActionListener {
    private static final String EMPTY_SONG = "no current song";
    private static final String EMPTY_TITLE = "Studiplayer: empty play list";
    private static final String EMPTY_DURATION = "--:--";
    public static final String DEFAULT_PLAYLIST = "playlists/DefaultPlayList.m3u";
    private static final String INITIAL_STARTPOSITION = "00:00";
    private static final String INITIAL_TITLE_PREFIX = "Current song: ";
    private volatile boolean stopped = true;

    private PlayList playList = null;
    private PlayListEditor playListEditor;
    private boolean editorVisible = false;


    private JLabel songDescription = null;
    private JLabel playTime = null;

    private JButton b_play = new JButton(new ImageIcon("icons/play.png"));
    private JButton b_pause = new JButton(new ImageIcon("icons/pause.png"));
    private JButton b_stop = new JButton(new ImageIcon("icons/stop.png"));

    public Player(PlayList playList){
        this.playList = playList;
         playListEditor = new PlayListEditor(this, this.playList);
        // Initialize the main frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }
        // Create GUI components
        if(!playList.isEmpty()){
            this.setTitle(INITIAL_TITLE_PREFIX + playList.getCurrentAudioFile().toString());
        } else{
            this.setTitle(EMPTY_TITLE);
        }
        // Button panel
        JPanel buttons = new JPanel();
        // Buttons

        b_play.setActionCommand("AC_PLAY");
        b_play.addActionListener(this);
        buttons.add(b_play);


        b_pause.setActionCommand("AC_PAUSE");
        b_pause.addActionListener(this);
        buttons.add(b_pause);

        b_stop.setActionCommand("AC_STOP");
        b_stop.addActionListener(this);
        buttons.add(b_stop);

        JButton b_next = new JButton(new ImageIcon("icons/next.png"));
        b_next.setActionCommand("AC_NEXT");
        b_next.addActionListener(this);
        buttons.add(b_next);

        JButton b_pl_editor = new JButton(new ImageIcon("icons/pl_editor.png"));
        b_pl_editor.setActionCommand("AC_PL_EDITOR");
        b_pl_editor.addActionListener(this);
        buttons.add(b_pl_editor);

        this.add(buttons, BorderLayout.CENTER);

        // Labels
        // Current title
        songDescription = new JLabel();
        playTime = new JLabel();

        this.add(songDescription, BorderLayout.NORTH);
        updateSongInfo(playList.getCurrentAudioFile());

        // Current playtime

        if(!playList.isEmpty()){
            playTime.setText(INITIAL_STARTPOSITION);
        } else{
            playTime.setText(EMPTY_DURATION);
        }
        this.add(playTime, BorderLayout.WEST);

        // Set visible buttons
        b_play.setEnabled(true);
        b_pause.setEnabled(false);
        b_stop.setEnabled(false);

        // Activate GUI
        this.pack();
        this.setVisible(true);
    }

    public void updateSongInfo(AudioFile af){
        if(af == null){
            setTitle(EMPTY_TITLE);
            songDescription.setText(EMPTY_SONG);
            playTime.setText(EMPTY_DURATION);
        }else{
            setTitle(INITIAL_TITLE_PREFIX+ af.toString());
            songDescription.setText(af.toString());
            playTime.setText(""+af.getFormattedDuration());
        }
    }



    // Implement interface of ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        AudioFile af = playList.getCurrentAudioFile();
        String cmd = e.getActionCommand();

        if (cmd.equals("AC_PLAY")) {
            playCurrentSong();
            b_play.setEnabled(false);
            b_pause.setEnabled(true);
            b_stop.setEnabled(true);
        } else if (cmd.equals("AC_PAUSE")) {

            System.out.println("Pausing " + af.toString());
            System.out.println("Filename is " + af.getFilename());
            System.out.println("Current index is " + playList.getCurrent());

            if(playList.getCurrentAudioFile() != null){
                af.togglePause();
                b_play.setEnabled(false);
                b_pause.setEnabled(true);
                b_stop.setEnabled(true);
            }

        } else if (cmd.equals("AC_STOP")) {
            stopCurrentSong();
            b_play.setEnabled(true);
            b_pause.setEnabled(false);
            b_stop.setEnabled(false);
            System.out.println("");


        } else if(cmd.equals("AC_NEXT")){
            System.out.println("Switching to next audio file ");
            if(!stopped){
                // We are playing
                // Stop playing the last song
                stopCurrentSong();
            }
            // Now, we are stopped and not playing
            // Move on to next song in the playlist
            playList.changeCurrent();
            // Play the next song
            playCurrentSong();
            // For Info: Get the current song from the list
            if(af != null){
                System.out.println("Switched to next audiofile");
            } else{
                System.out.println("Playlist is empty");
            }
            b_play.setEnabled(false);
            b_pause.setEnabled(true);
            b_stop.setEnabled(true);
            System.out.println("");

        } else if(cmd.equals("AC_PL_EDITOR")){
            if(editorVisible){
                editorVisible = false;
            } else{
                editorVisible = true;
            }
            playListEditor.setVisible(editorVisible);
        }
        else{
            System.out.println("Should never enter this area");
        }
    }

    private void playCurrentSong(){
        updateSongInfo(playList.getCurrentAudioFile());
        stopped = false;
        if(playList.getCurrentAudioFile() != null){
            (new TimerThread()).start();
            (new PlayerThread()).start();
        }

        System.out.println("Playing " + playList.getCurrentAudioFile().toString());
        System.out.println("Filename is " + playList.getCurrentAudioFile().getFilename());
        System.out.println("Current index is " + playList.getCurrent());
    }

    private void stopCurrentSong(){
        stopped = true;
        playList.getCurrentAudioFile().stop();

        if(playList.getCurrentAudioFile() != null){
            playList.getCurrentAudioFile().stop();
            playTime.setText(INITIAL_STARTPOSITION);
        }

        updateSongInfo(playList.getCurrentAudioFile());
        playTime.setText("00:00");

        System.out.println("Stopping " + playList.getCurrentAudioFile().toString());
        System.out.println("Filename is " + playList.getCurrentAudioFile().getFilename());
        System.out.println("Current index is " + playList.getCurrent());
    }

    public static void main(String[] args){
        // Intialize a playlist
        PlayList playlist = new PlayList();

        // Load a customized playlist or the default playlist
        if(args.length > 0){
            try{
                playlist.loadFromM3U(args[0]);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            try{
                playlist.loadFromM3U(DEFAULT_PLAYLIST);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        // Intialize the player
        new Player(playlist);
    }
    // Thread
    private class TimerThread extends Thread{
        public void run(){
            while(!stopped && !playList.isEmpty()){
                playTime.setText(playList.getCurrentAudioFile().getFormattedPosition());
                try{
                    sleep(100);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private class PlayerThread extends Thread{
        public void run(){
            while(!stopped && !playList.isEmpty()){
                try{
                    playList.getCurrentAudioFile().play();
                } catch (NotPlayableException e){
                    e.printStackTrace();
                }
                if(!stopped){
                    playList.changeCurrent();
                    updateSongInfo(playList.getCurrentAudioFile());
                }
            }
        }
    }

}
