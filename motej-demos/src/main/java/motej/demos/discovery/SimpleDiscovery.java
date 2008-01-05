package motej.demos.discovery;

import motej.Mote;
import motej.MoteFinder;

public class SimpleDiscovery {

	public static void main(String[] args) throws InterruptedException {
		Mote mote = MoteFinder.getMoteFinder().findMote();
		mote.setPlayerLeds(new boolean[] {true, false, false, false});
		Thread.sleep(5000l);
		mote.disconnect();
	}

}
