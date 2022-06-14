package net.valneas.account.rank;

import com.google.common.base.Preconditions;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 */

public abstract class AbstractRankHandler {

    protected final Datastore datastore;

    public AbstractRankHandler(Datastore datastore) {
        this.datastore = datastore;
    }

    public Query<AbstractRankUnit> getAllRanksQuery(){
        return this.datastore.find(AbstractRankUnit.class);
    }


    public AbstractRankUnit getDefaultRank(){
        var defaultRank = this.datastore.find(AbstractRankUnit.class).filter(Filters.eq("default", true)).first();
        if(defaultRank == null){
            System.err.println("No default rank set. Will use the first rank in the database.");
            defaultRank = this.datastore.find(AbstractRankUnit.class).first();
            Preconditions.checkNotNull(defaultRank, "No ranks in the database.");
        }
        return defaultRank;
    }

    public Query<AbstractRankUnit> getById(int id){
        return this.datastore.find(AbstractRankUnit.class).filter(Filters.eq("id", id));
    }

    public Query<AbstractRankUnit> getByPower(int power){
        return this.datastore.find(AbstractRankUnit.class).filter(Filters.eq("power", power));
    }

    public Query<AbstractRankUnit> getByName(String name){
        return this.datastore.find(AbstractRankUnit.class).filter(Filters.eq("name", name));
    }

    public Query<AbstractRankUnit> getByCommandArg(String arg){
        try{
            int i = Integer.parseInt(arg);
            return getById(i);
        } catch (Exception e){
            return getByName(arg);
        }
    }
}
