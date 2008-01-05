package motej.demos.buttons;

import motej.Mote;
import motej.MoteFinder;
import motej.event.CoreButtonEvent;
import motej.event.CoreButtonListener;

public class ButtonsDemo {

	public static void main(String[] args) {
		System.out.println("press 'q' to quit.");
		
		Mote mote = MoteFinder.getMoteFinder().findMote();
		mote.addCoreButtonListener(new CoreButtonListener() {
		
			public void buttonPressed(CoreButtonEvent evt) {
				if (evt.isButtonAPressed()) {
					System.out.println("Button A pressed!");
				}
				if (evt.isButtonBPressed()) {
					System.out.println("Button B pressed!");
				}
				if (evt.isNoButtonPressed()) {
					System.out.println("No button pressed.");
				}
			}
		
		});
		
		while (true) {
			String line = System.console().readLine();
			if (line.indexOf("q") != -1) {
				mote.disconnect();
				System.exit(0);
			}
		}
	}
}
