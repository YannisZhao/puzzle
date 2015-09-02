package pintu;

import java.net.*;
import java.applet.*;

public class MusicController {
	AudioClip backMusic;
	AudioClip clickMusic;

	public MusicController() {
		try {
			backMusic = Applet
					.newAudioClip(new URL("file:"
							+ System.getProperty("user.dir")
							+ "/music/background.wav"));
			backMusic.loop();
			clickMusic = Applet.newAudioClip(new URL("file:"
					+ System.getProperty("user.dir") + "/music/click.wav"));
		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
		}
	}
}