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
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.bluetooth.BlueCoveConfigProperties;

/**
 * 
 * <p>
 * @author Volker Fritzsch
 * @author Christoph Krichenbauer
 */
public class MoteFinder {
	
	// initialization on demand holder idiom
	private static class SingletonHolder {
		
		private static final MoteFinder INSTANCE = new MoteFinder();
		
	}
	
	/**
	 * Returns the <code>WiimoteFinder</code> instance.
	 * 
	 * @return WiimoteFinder
	 */
	public static MoteFinder getMoteFinder() {
		try {
			// disable PSM minimum flag because the wiimote has a PSM below 0x1001
			System.setProperty(BlueCoveConfigProperties.PROPERTY_JSR_82_PSM_MINIMUM_OFF, "true");

			SingletonHolder.INSTANCE.localDevice = LocalDevice.getLocalDevice();
			SingletonHolder.INSTANCE.discoveryAgent = SingletonHolder.INSTANCE.localDevice.getDiscoveryAgent();
			return SingletonHolder.INSTANCE;
		} catch (BluetoothStateException ex) {
			throw new RuntimeException(ex);
		}
	}

	private Logger log = LoggerFactory.getLogger(MoteFinder.class);
	
	private EventListenerList listenerList = new EventListenerList();
	
	private DiscoveryAgent discoveryAgent;
	
	private Set<String> bluetoothAddressCache = new HashSet<String>();
	
	private Set<DiscoveryListener> discoveryListeners = new HashSet<DiscoveryListener>();
	
	public void addDiscoveryListener(DiscoveryListener listener) {
		discoveryListeners.add(listener);
	}
	
	public void removeDiscoveryListener(DiscoveryListener listener) {
		discoveryListeners.remove(listener);
	}

	protected final DiscoveryListener listener = new DiscoveryListener() {
		
		public void deviceDiscovered(final RemoteDevice device, DeviceClass clazz) {
			if (log.isInfoEnabled()) {
				try {
					log.info("found device: " + device.getFriendlyName(true)
							+ " - " + device.getBluetoothAddress() + " - "
							+ clazz.getMajorDeviceClass() + ":"
							+ clazz.getMinorDeviceClass() + " - "
							+ clazz.getServiceClasses());
				} catch (IOException ex) {
					log.error(ex.getMessage(), ex);
					throw new RuntimeException(ex);
				}
			}

			try {
				if (!device.getFriendlyName(true).startsWith("Nintendo")) { //("Nintendo RVL-CNT-01") != 0) {
					return;
				}
			} catch (IOException ex) {
				log.error(ex.getMessage(), ex);
				throw new RuntimeException(ex);
			}

			final String address = device.getBluetoothAddress();
			
			// is this already registered?
			if (!bluetoothAddressCache.contains(address)) {
				Thread connectThread = new Thread("connect: " + address) {
					public void run() {
						Mote mote = new Mote(address);
						fireMoteFound(mote);
						bluetoothAddressCache.add(address);
					};
				};
				connectThread.start();
				
				for (DiscoveryListener delegate : discoveryListeners) {
					delegate.deviceDiscovered(device, clazz);
				}
			}
		}

		public void inquiryCompleted(int discType) {
			if (discType == DiscoveryListener.INQUIRY_COMPLETED) {
				if (log.isInfoEnabled()) {
					log.info("inquiry completed");
				}
			}

			if (discType == DiscoveryListener.INQUIRY_TERMINATED) {
				if (log.isInfoEnabled()) {
					log.info("inquiry terminated");
				}
			}
			
			if (discType == DiscoveryListener.INQUIRY_ERROR) {
				if (log.isInfoEnabled()) {
					log.info("inquiry error");
				}
			}
			
			for (DiscoveryListener delegate : discoveryListeners) {
				delegate.inquiryCompleted(discType);
			}
		}

		public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
			// internally unused
			
			for (DiscoveryListener delegate : discoveryListeners) {
				delegate.servicesDiscovered(transID, servRecord);
			}
		}

		public void serviceSearchCompleted(int transID, int respCode) {
			// internally unused
			
			for (DiscoveryListener delegate : discoveryListeners) {
				delegate.serviceSearchCompleted(transID, respCode);
			}
		}
	};

	private LocalDevice localDevice;

	private MoteFinder() {};

	public void addMoteFinderListener(MoteFinderListener listener) {
		listenerList.add(MoteFinderListener.class, listener);
	}
	
	protected void fireMoteFound(Mote mote) {
		MoteFinderListener[] listeners = listenerList.getListeners(MoteFinderListener.class);
		for (MoteFinderListener l : listeners) {
			l.moteFound(mote);
		}
	}

	public void removeMoteFinderListener(MoteFinderListener listener) {
		listenerList.remove(MoteFinderListener.class, listener);
	}
	
	public void startDiscovery() {
		try {
			discoveryAgent.startInquiry(DiscoveryAgent.GIAC, listener);
		} catch (BluetoothStateException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void stopDiscovery() {
		discoveryAgent.cancelInquiry(listener);
	}

}
