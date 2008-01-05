/*
 * Copyright 2007-2008 Volker Fritzsch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package motej;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class MoteFinder {
	
	private Log log = LogFactory.getLog(MoteFinder.class);

	private static MoteFinder instance;

	/**
	 * Returns the <code>WiimoteFinder</code> instance.
	 * 
	 * @return WiimoteFinder
	 */
	public static MoteFinder getMoteFinder() {
		synchronized (MoteFinder.class) {
			if (instance == null)
				instance = new MoteFinder();
		}
		try {
			instance.localDevice = LocalDevice.getLocalDevice();
			instance.discoveryAgent = instance.localDevice.getDiscoveryAgent();
			return instance;
		} catch (BluetoothStateException ex) {
			throw new RuntimeException(ex.fillInStackTrace());
		}
	}

	private Object inquiryCompletedEvent = new Object();

	private DiscoveryAgent discoveryAgent;

	protected final DiscoveryListener listener = new DiscoveryListener() {

		public void deviceDiscovered(RemoteDevice device, DeviceClass clazz) {
			try {
				if (log.isInfoEnabled()) {
					log.info("found device: "
							+ device.getFriendlyName(true) + " - "
							+ device.getBluetoothAddress() + " - "
							+ clazz.getMajorDeviceClass() + ":"
							+ clazz.getMinorDeviceClass() + " - "
							+ clazz.getServiceClasses());
				}

				if (device.getFriendlyName(true).compareTo(
						"Nintendo RVL-CNT-01") == 0) {
					mote = new Mote(device);
					discoveryAgent.cancelInquiry(this);
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex.fillInStackTrace());
			}
		}

		public void inquiryCompleted(int arg0) {
			if (log.isInfoEnabled()) {
				log.info("inquiry completed.");
			}
			synchronized (inquiryCompletedEvent) {
				inquiryCompletedEvent.notifyAll();
			}
		}

		public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
			// unused
		}

		public void serviceSearchCompleted(int arg0, int arg1) {
			// unused
		}
	};

	private LocalDevice localDevice;;

	private Mote mote;

	protected MoteFinder() {
	}

	public Mote findMote() {
		try {
			synchronized (inquiryCompletedEvent) {
				discoveryAgent.startInquiry(DiscoveryAgent.GIAC, listener);
				if (log.isInfoEnabled()) {
					log.info("waiting for inquiry...");
				}
				inquiryCompletedEvent.wait();
				if (log.isInfoEnabled()) {
					log.info("found " + mote == null ? 0 : 1 + " motes.");
				}
			}
			return mote;
		} catch (BluetoothStateException ex) {
			throw new RuntimeException(ex.fillInStackTrace());
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex.fillInStackTrace());
		}
	}

//	public List<Mote> findPreknownOrCachedWiimotes() {
//		try {
//			RemoteDevice[] devices = discoveryAgent
//					.retrieveDevices(DiscoveryAgent.CACHED
//							| DiscoveryAgent.PREKNOWN);
//			if (devices == null) {
//				System.out.println("neither cached or preknown devices found.");
//				return null;
//			}
//			for (int i = 0; i < devices.length; i++) {
//				System.out.println(devices[i].getFriendlyName(false));
//			}
//		} catch (IOException ex) {
//			throw new RuntimeException(ex.fillInStackTrace());
//		}
//		return null;
//	}
}
