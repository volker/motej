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
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.bluetooth.L2CAPConnection;
import javax.microedition.io.Connector;

import motej.request.PlayerLedRequest;
import motej.request.ReportModeRequest;
import motej.request.RumbleRequest;
import motej.request.MoteRequest;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
class OutgoingThread extends Thread {

		private static final long THREAD_SLEEP = 10l;

		private volatile boolean active;

		private L2CAPConnection outgoing;

		private ConcurrentLinkedQueue<MoteRequest> requestQueue;
		
		private byte ledByte;
		
		private long rumbleMillis = Long.MIN_VALUE;

		protected OutgoingThread(String btaddress) throws IOException, InterruptedException {
			super("OutgoingWiimoteThread:" + btaddress);
			outgoing = (L2CAPConnection) Connector.open("btl2cap://"
					+ btaddress
					+ ":11;authenticate=false;encrypt=false;master=false",
					Connector.WRITE);

//			outgoing.send(new byte[] { 82, 17, (byte) (1 << 7) });
			requestQueue = new ConcurrentLinkedQueue<MoteRequest>();
			Thread.sleep(THREAD_SLEEP);
			active = true;
		}

		public void sendRequest(MoteRequest request) {
			requestQueue.add(request);
		}

		public void disconnect() {
			active = false;
			try {
				sendRequest(new ReportModeRequest(ReportModeRequest.DATA_REPORT_0x30));
				while(!requestQueue.isEmpty()) {
					try {
						Thread.sleep(10l);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				outgoing.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run() {
			while (active) {
				try {
					if (rumbleMillis > 0) {
						rumbleMillis -= THREAD_SLEEP;
					}
					if (rumbleMillis == 0) {
						rumbleMillis = Long.MIN_VALUE;
						outgoing.send(RumbleRequest.getStopRumbleBytes(ledByte));
						Thread.sleep(THREAD_SLEEP);
						continue;
					}
					if (requestQueue.peek() != null) {
						MoteRequest request = requestQueue.poll();
						if (request instanceof PlayerLedRequest) {
							ledByte = ((PlayerLedRequest) request).getLedByte();
						}
						if (request instanceof RumbleRequest) {
							((RumbleRequest)request).setLedByte(ledByte);
							rumbleMillis = ((RumbleRequest) request).getMillis();
						}
						outgoing.send(request.getBytes());
					}
					Thread.sleep(THREAD_SLEEP);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					System.out.println("connection closed?");
					active = false;
				}
			}
		}

	}