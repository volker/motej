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

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public enum IrCameraSensitivity {
	
	/**
	 * Sensitivity Settings as provided by <a href="TODO">Marcan</a>.
	 */
	MARCAN(new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0x90, 0x00,
			(byte) 0xc0 }, new byte[] { 0x40, 0 }),

	/**
	 * Sensitivity settings as provided by <a href="TODO">Cliff</a>.
	 */
	CLIFF(new byte[] { 0x02, 0x00, 0x00, 0x71, 0x01, 0x00, (byte) 0xaa, 0x00,
			0x64 }, new byte[] { 0x63, 0x03 });

	private final byte[] block1;
	private final byte[] block2;

	private IrCameraSensitivity(byte[] block1, byte[] block2) {
		this.block1 = block1;
		this.block2 = block2;
	}

	public byte[] block1() {
		return block1;
	}

	public byte[] block2() {
		return block2;
	}
}
