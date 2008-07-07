package motej.ext.alt.java.util.concurrent;

public class ConcurrentLinkedQueue {

    private static final Object NULL_ARRAY[] = new Object[0];
    protected transient Object Que[];


    public ConcurrentLinkedQueue() {
        Que = NULL_ARRAY;
    }


    public boolean add(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }

        if (Que == NULL_ARRAY) {
            Que = new Object[] {obj};
        } else {
            int len = Que.length;
            Object temp[] = new Object[len+1];
            System.arraycopy(Que, 0, temp, 0, len);
            temp[len] = obj;
            Que = temp;
        }
        return true;
    }

    public Object poll() {
        if (Que == NULL_ARRAY) {
            return null;
        }

        int len = Que.length;
        Object obj = Que[0];

        if (len > 1) {
            Object temp[] = new Object[len-1];
            System.arraycopy(Que, 1, temp, 0, len-1);
            Que = temp;
        } else {
            Que = NULL_ARRAY;
        }
        return obj;
    }

    public Object peek() {
        if (Que == NULL_ARRAY) {
            return null;
        }

        Object obj = Que[0];
        return obj;
    }
}
