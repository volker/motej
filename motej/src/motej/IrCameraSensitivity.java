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
public final class IrCameraSensitivity extends Enum
{
    private IrCameraSensitivity(String s, int i, byte abyte0[], byte abyte1[])
    {
        super(s, i);
        block1 = abyte0;
        block2 = abyte1;
    }

    public byte[] block1()
    {
        return block1;
    }

    public byte[] block2()
    {
        return block2;
    }

    public static final IrCameraSensitivity MARCAN;
    public static final IrCameraSensitivity CLIFF;
    private final byte block1[];
    private final byte block2[];
    private static final IrCameraSensitivity $VALUES[];

    static 
    {
        MARCAN = new IrCameraSensitivity("MARCAN", 0, new byte[] {
            0, 0, 0, 0, 0, 0, -112, 0, -64
        }, new byte[] {
            64, 0
        });
        CLIFF = new IrCameraSensitivity("CLIFF", 1, new byte[] {
            2, 0, 0, 113, 1, 0, -86, 0, 100
        }, new byte[] {
            99, 3
        });
        $VALUES = (new IrCameraSensitivity[] {
            MARCAN, CLIFF
        });
    }
}
