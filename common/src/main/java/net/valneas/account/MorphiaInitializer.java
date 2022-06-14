package net.valneas.account;

import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */

public class MorphiaInitializer {

    private @Getter final Datastore datastore;

    public MorphiaInitializer(Class<?> mainClazz, MongoClient client, String dbName, String[] packages) {
        this.datastore = Morphia.createDatastore(client, dbName, MapperOptions.builder().storeEmpties(true).classLoader(mainClazz.getClassLoader()).build());
        Arrays.stream(packages).forEach(datastore.getMapper()::mapPackage);

        datastore.ensureIndexes();
    }
}
