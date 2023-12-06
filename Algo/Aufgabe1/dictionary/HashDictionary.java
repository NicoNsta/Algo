import java.util.Iterator;
import java.util.LinkedList;

public class HashDictionary<K, V> implements Dictionary<K, V> {

    private int size;
    private LinkedList<Entry<K, V>>[] data;
    private int count = 0;

    private static class Entry<K, V> {
        private K key;
        private V value;

        private Entry(K k, V v) {
            key = k;
            value = v;
        }
    }

    @SuppressWarnings("unchecked")
    public HashDictionary() {
        size = 3;
        data = new LinkedList[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList<>();
        }
    }

    private int hash(K key) {
        int adr = 0;
        for (int i = 0; i < key.toString().length(); i++) {
            adr += 31 * adr + key.toString().charAt(i);
        }

        if (adr < 0) {
            adr = -adr;
        }
        
        adr = adr % size;
        return adr;
    }
    
    @Override
    public V insert(K key, V value) {

        int hash = hash(key);
        for (Entry<K, V> entry : data[hash]) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }
        data[hash].addFirst(new Entry<>(key, value));
        resize();
        return null;
    }

    private void resize() {
        count++;

        if (count > size){
            size = nextPrime(size * 2);

            LinkedList<Entry<K, V>>[] newData = new LinkedList[size];
            for (int i = 0; i < newData.length; i++) {
                newData[i] = new LinkedList<>();
            }
    
            // Kopieren von Daten in neues Array
            for (LinkedList<Entry<K, V>> list : data) {
                for (Entry<K, V> entry : list) {
                    int hash = hash(entry.key);
                    newData[hash].add(entry);
                }
            }
            data = newData;
        }
    }

    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static int nextPrime(int n) {
        if (n % 2 == 0) {
            n++;
        }
        while (!isPrime(n)) {
            n += 1;
        }
        return n;
    }



    @Override
    public V search(K key) {
        int hash = hash(key);
        for (Entry<K, V> entry : data[hash]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int hash = hash(key);
        for (Entry<K, V> entry : data[hash]) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                data[hash].remove(entry);
                count--;
                return oldValue;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {
        return new Iterator<Dictionary.Entry<K,V>>() {
            int i = 0;
            int j = 0;
            @Override
            public boolean hasNext() {
                while (i < data.length) {
                    if (j < data[i].size()) {
                        return true;
                    } else {
                        i++;
                        j = 0;
                    }
                }
                return false;
            }

            @Override
            public Dictionary.Entry<K, V> next() {
                if (hasNext()) {
                    Entry<K, V> entry = data[i].get(j);
                    j++;
                    if (j == data[i].size()) {
                        i++;
                        j = 0;
                    }
                    return new Dictionary.Entry<>(entry.key, entry.value);
                }
                return null;
            }
        };
    }
}
