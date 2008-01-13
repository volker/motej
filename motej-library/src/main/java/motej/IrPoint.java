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

import java.awt.Point;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class IrPoint extends Point {

	private static final long serialVersionUID = -7473956039643032186L;

	public int size;

	public IrPoint() {
		this.x = 0;
		this.y = 0;
		this.size = 1;
	}

	public IrPoint(int x, int y) {
		this(x, y, 1);
	}

	public IrPoint(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public IrPoint(IrPoint p) {
		this.x = p.x;
		this.y = p.y;
		this.size = p.size;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof IrPoint) {
			IrPoint p = (IrPoint) obj;
			return (x == p.x) && (y == p.y) && (size == p.size);
		}
		return super.equals(obj);
	}

	public int getSize() {
		return size;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + x;
		hash = hash * 31 + y;
		hash = hash * 31 + size;
		return hash;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
