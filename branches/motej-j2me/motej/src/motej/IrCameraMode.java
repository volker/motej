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

import motej.ext.alt.java.lang.Enum;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public final class IrCameraMode extends Enum
{
    private IrCameraMode(String s, int i, byte byte0)
    {
        super(s, i);
        mode = byte0;
    }

    public byte modeAsByte()
    {
        return mode;
    }

    public static final IrCameraMode BASIC;
    public static final IrCameraMode EXTENDED;
    public static final IrCameraMode FULL;
    private final byte mode;
    private static final IrCameraMode $VALUES[];

    static 
    {
        BASIC = new IrCameraMode("BASIC", 0, (byte)1);
        EXTENDED = new IrCameraMode("EXTENDED", 1, (byte)3);
        FULL = new IrCameraMode("FULL", 2, (byte)5);
        $VALUES = (new IrCameraMode[] {
            BASIC, EXTENDED, FULL
        });
    }
}
