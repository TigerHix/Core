package net.cogzmc.core.model.mongo;

import com.google.common.collect.ImmutableList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import lombok.*;
import net.cogzmc.core.Core;
import net.cogzmc.core.model.Model;
import net.cogzmc.core.model.ModelSerializer;
import net.cogzmc.core.model.ModelStorage;
import net.cogzmc.core.model.SerializationException;
import net.cogzmc.core.player.mongo.CMongoDatabase;
import net.cogzmc.core.player.mongo.MongoKey;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
class MongoModelStorage<T extends Model> implements ModelStorage<T> {
    @NonNull private final DBCollection collection;
    @NonNull private final CMongoDatabase database;
    @NonNull private final ModelSerializer<T> modelSerializer;
    @NonNull private final Class<T> modelType;

    @Setter(AccessLevel.NONE) private ImmutableList<T> values;

    @Override
    public void saveValue(T value) throws SerializationException {
        DBObject serialize = (DBObject) modelSerializer.serialize(value);
        boolean needsId = value.getId() != null;
        if (needsId) serialize.put(MongoKey.ID_KEY.toString(), new ObjectId(value.getId()));
        collection.save(serialize);
        if (!needsId) value.setId(serialize.get(MongoKey.ID_KEY.toString()).toString());
        reload();
    }

    @Override
    public void updateValue(T value) throws SerializationException {
        saveValue(value);
    }

    @Override
    public void deleteValue(T value) {
        collection.remove(new BasicDBObject(MongoKey.ID_KEY.toString(), new ObjectId(value.getId())));
        reload();
    }

    @Override
    @SneakyThrows
    public T findValue(String key, Object value) {
        List<T> values1 = findValues(key, value);
        return values1 == null || values1.isEmpty() ? null : values1.get(0);
    }

    @Override
    @SneakyThrows
    public List<T> findValues(String key, Object value) {
        Field declaredField;
        try {
            declaredField = modelType.getDeclaredField(key);
        } catch (NoSuchFieldException e) {
            try {
                declaredField = modelType.getField(key);
            } catch (NoSuchFieldException e2) {
                return null;
            }
        }
        if (!declaredField.getType().isAssignableFrom(value.getClass())) return null;
        List<T> ts = new ArrayList<>();
        declaredField.setAccessible(true);
        for (T t : values) {
            Object o = declaredField.get(t);
            if (o != null && o.equals(value)) ts.add(t);
        }
        return ts;
    }

    @Override
    public T getById(String id) {
        for (T value : values) {
            if (value.getId().equals(id)) return value;
        }
        return null;
    }

    @Override
    public void reload() {
        List<T> ts = new ArrayList<>();
        for (DBObject dbObject : collection.find()) {
            try {
                T model = modelSerializer.deserialize(dbObject, modelType);
                model.setId(dbObject.get(MongoKey.ID_KEY.toString()).toString());
                ts.add(model);
            } catch (SerializationException ignored) {} //Ignored because failure means we skip.
        }
        this.values = ImmutableList.copyOf(ts);
    }
}
