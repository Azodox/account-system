package net.valneas.account.rank;

import com.google.common.base.Preconditions;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */

public abstract class AbstractRankHandler<T extends AbstractRankUnit & RankUnit> implements RankHandler<T> {

    protected final Datastore datastore;
    protected final Class<T> clazz;

    public AbstractRankHandler(Datastore datastore, Class<T> clazz) {
        this.datastore = datastore;
        this.clazz = clazz;
    }

    @Override
    public Query<T> getAllRanksQuery(){
        return this.datastore.find(this.clazz);
    }


    @Override
    public T getDefaultRank(){
        var defaultRank = this.datastore.find(this.clazz).filter(Filters.eq("default", true)).first();
        if(defaultRank == null){
            System.err.println("No default rank set. Will use the first rank in the database.");
            defaultRank = Preconditions.checkNotNull(this.datastore.find(this.clazz).first(), "No ranks in the database.");
        }
        return defaultRank;
    }

    @Override
    public Query<T> getById(int id){
        return this.datastore.find(this.clazz).filter(Filters.eq("id", id));
    }

    @Override
    public Query<T> getByPower(int power){
        return this.datastore.find(this.clazz).filter(Filters.eq("power", power));
    }

    @Override
    public Query<T> getByName(String name){
        return this.datastore.find(this.clazz).filter(Filters.eq("name", name));
    }

    @Override
    public Query<T> getByCommandArg(String arg){
        try{
            int i = Integer.parseInt(arg);
            return getById(i);
        } catch (Exception e){
            return getByName(arg);
        }
    }
}
