package beta;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class MediaPlayer {

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

	private JFrame mediaFrame = new JFrame();
	private String mediaPath = "";

	private boolean pause = true;

	MediaPlayer() {

		NativeLibrary.addSearchPath(
				RuntimeUtil.getLibVlcLibraryName(), "/Applications/vlc-2.0.0/VLC.app/Contents/MacOS/lib"
				);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		//Make media player
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		
		final EmbeddedMediaPlayer video = mediaPlayerComponent.getMediaPlayer();

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(mediaPlayerComponent, BorderLayout.CENTER);

		mediaFrame.setContentPane(panel);
		
		//add pause button and a progress bar
		final JToggleButton pauseBtn = new JToggleButton("| |");

		JPanel secondPanel = new JPanel(new GridLayout(2,1));
		panel.add(secondPanel, BorderLayout.SOUTH);
		final JProgressBar pb = new JProgressBar();
		pb.setValue(0);
		secondPanel.add(pb);
		pauseBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				video.pause();	
			}

		});
		
		JPanel btnPanel = new JPanel(); //make panel to host all the control buttons
		//make skip button
		JButton btnSkip = new JButton(">>");
        btnSkip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(5000);
			}
		});
        //make skipback button
        JButton btnSkipBack = new JButton("<<");
        btnSkipBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(-5000);
			}
		});
        
        btnPanel.add(btnSkipBack);
        btnPanel.add(pauseBtn);
        btnPanel.add(btnSkip);
        
        secondPanel.add(btnPanel);
		//Progress Bar shows how long the video has been playing.
        //The timer's actionlistener updates the progress bar
		Timer timer = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long time = (long) video.getTime();
				long totalTime = (long) video.getLength();
				long percentage = ( (time * 100) / totalTime );
				int n = (int) percentage;
				pb.setValue(n);
			}

		});

		timer.start();	
		mediaPath = "final.avi";
		
		//Video stops playing after frame closes
		mediaFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				video.stop();
				mediaFrame.dispose();
			}
		});

		mediaFrame.setLocation(100, 100);
		mediaFrame.setSize(1050, 600);
		mediaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		mediaFrame.setVisible(true);

		video.playMedia(mediaPath);

	}

}
