package motej.ext.alt.javax.swing.event;

import java.io.*;
import motej.ext.alt.java.util.EventListener;

public class EventListenerList extends Object {

    private static final Object NULL_ARRAY[] = new Object[0];
    protected transient Object listenerList[];


    public EventListenerList() {
        listenerList = NULL_ARRAY;
    }


    public synchronized void add(Class t, EventListener l) {
        if (l == null) {return;}

        if (!t.isInstance(l)) {
            throw new IllegalArgumentException("Listener "+l+" is not of type "+t);
        }

        if (listenerList == NULL_ARRAY) {
            listenerList = new Object[] {t, l};
        } else {
            int i = listenerList.length;
            Object temp[] = new Object[i+2];
            System.arraycopy(listenerList, 0, temp, 0, i);
            temp[i] = t;
            temp[i+1] = l;
            listenerList = temp;
        }
    }

    public int getListenerCount() {
        return listenerList.length / 2;
    }

    public int getListenerCount(Class t) {
        Object temp[] = listenerList;
        return getListenerCount(temp, t);
    }

    public Object[] getListenerList() {
        return listenerList;
    }

    public Object[] getListeners(Class t) {
        Object temp[] = listenerList;
        int cnt = getListenerCount(temp, t);

        Object listeners[] = new Object[cnt];
        int i = 0;
        for (int len = temp.length-2; len >= 0; len -= 2) {
            if (temp[len] == t) {
                listeners[i++] = temp[len+1];
            }
        }

        return listeners;
    }

    public synchronized void remove(Class t, EventListener l) {
        if (l == null) {return;}

        if (!t.isInstance(l)) {
            throw new IllegalArgumentException("Listener "+l+" is not of type "+t);
        }

        int pos = -1;

        for (int len = listenerList.length-2; len >= 0; len -= 2) {
            if ((listenerList[len] == t) && (listenerList[len+1].equals(l))) {
                pos = len;
                break;
            }
        }

        if (pos != -1) {
            Object temp[] = new Object[listenerList.length - 2];
            System.arraycopy(listenerList, 0, temp, 0, pos);
            if(pos < temp.length)
                System.arraycopy(listenerList, pos+2, temp, pos, temp.length-pos);
            listenerList = temp.length != 0 ? temp : NULL_ARRAY;
        }
    }

    public String toString() {
        return "";
    }



    /* --------------------------------------------------------------- */



    private int getListenerCount(Object o[], Class t) {
        int cnt = 0;
        for (int i = 0; i < o.length; i += 2) {
            if(t == (Class)o[i]) {
                cnt++;
            }
        }
        return cnt;
    }

}
