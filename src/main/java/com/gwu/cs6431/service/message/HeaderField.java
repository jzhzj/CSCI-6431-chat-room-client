package com.gwu.cs6431.service.message;

class HeaderField<K, V> {
    private K key;
    private V value;

    public HeaderField(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key.toString() + "=" + value.toString();
    }
}
