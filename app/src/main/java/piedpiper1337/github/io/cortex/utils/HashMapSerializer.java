package piedpiper1337.github.io.cortex.utils;

import com.activeandroid.serializer.TypeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

/**
 * Created by brianzhao on 2/28/16.
 */
final public class HashMapSerializer extends TypeSerializer {
    private static Gson sGson = new GsonBuilder().create();

    @Override
    public Class<?> getDeserializedType() {
        return HashMap.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public Object serialize(Object data) {
        if (data == null) {
            return null;
        }
        return sGson.toJson(data);
    }

    @Override
    public Object deserialize(Object data) {
        if (data == null) {
            return null;
        }
        return sGson.fromJson((String)data, HashMap.class);
    }
}
