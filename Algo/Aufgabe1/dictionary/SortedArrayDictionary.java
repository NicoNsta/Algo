import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

public class SortedArrayDictionary<K extends Comparable<? super K>, V> implements Dictionary<K, V>  {

    private static class Entry<K, V> {
        private K key;
        private V value;

        private Entry(K k, V v) {
            key = k;
            value = v;
        }
    }

    private static final int DEF_CAPACITY = 16;
    private int size;
    private Entry<K, V>[] data;

    public SortedArrayDictionary() {
        size = 0;
        data = new Entry[DEF_CAPACITY];
    }

    private int searchKey(K key) {
        int li = 0;
        int re = size() - 1;

        while (li <= re) {
            int m = (li + re) / 2;
            if (key.compareTo(data[m].key) < 0)
                re = m - 1;
            else if (key.compareTo(data[m].key) > 0)
                li = m + 1;
            else
                return m;
        }
        return -1;
    }

    @Override
    public V insert(K key, V value) {
        int i = searchKey(key);

        // key already exists
        if (i != -1) {
            V oldValue = data[i].value;
            data[i].value = value;
            return oldValue;
        }

        // key does not exist
        if (data.length == size) {
            data = Arrays.copyOf(data, 2 * size);
        }
        int j = size - 1;
        while (j >= 0 && key.compareTo(data[j].key) < 0) {
            data[j + 1] = data[j];
            j--;
        }
        data[j + 1] = new Entry<>(key, value);
        size++;
        return null;
    }

    @Override
    public V search(K key) {
        int i = searchKey(key);
        if (i != -1) {
            return data[i].value;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int i = searchKey(key);
        if (i == -1) {
            return null;
        }
        V oldValue = data[i].value;
        for (int j = i; j < size - 1; j++) {
            data[j] = data[j + 1];
        }
        data[size - 1] = null;
        size--;
        return oldValue;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {
        return new Iterator<Dictionary.Entry<K, V>>() {
            private int pos = 0;

            @Override
            public boolean hasNext() {
                return pos < size;
            }

            @Override
            public Dictionary.Entry<K, V> next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                return new Dictionary.Entry<>(data[pos].key, data[pos++].value);
            }
        };
    }

}
