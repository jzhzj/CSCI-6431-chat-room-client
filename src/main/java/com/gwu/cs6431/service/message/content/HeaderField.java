package com.gwu.cs6431.service.message.content;

public class HeaderField<V> {
    private Header key;
    private V value;

    public HeaderField(Header key, V value) {
        this.key = key;
        this.value = value;
    }

    public Header getKey() {
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
