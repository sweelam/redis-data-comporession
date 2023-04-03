package com.example.datacompression.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.xerial.snappy.Snappy;

import java.io.*;

class SnappyConfig<T> implements RedisSerializer<T> {
    @Override
    public byte[] serialize(T object) throws SerializationException {
        if (object == null) {
            return null;
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();

            byte[] compressedBytes = Snappy.compress(byteArrayOutputStream.toByteArray());
            return compressedBytes;

        } catch (IOException e) {
            throw new SerializationException("Error while serializing object", e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }

        try {
            byte[] decompressedBytes = Snappy.uncompress(bytes);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decompressedBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (T) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Error while    deserializing object", e);
        }
    }
}