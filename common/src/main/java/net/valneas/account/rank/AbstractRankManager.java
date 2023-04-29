package net.valneas.account.rank;

import dev.morphia.query.experimental.filters.Filters;
import dev.morphia.query.experimental.updates.UpdateOperators;
import net.valneas.account.AbstractAccountManager;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author Azodox_ (Luke)
 * 5/6/2022.
 * @param <E> The type of account manager.
 * @param <T> The type of rank unit.
 */

public abstract class AbstractRankManager<E extends AbstractAccountManager<?>, T extends AbstractRankUnit & RankUnit> implements RankManager<T> {

    protected final JedisPool jedisPool;
    protected final E accountManager;

    public AbstractRankManager(JedisPool jedis, E accountManager) {
        this.jedisPool = jedis;
        this.accountManager = accountManager;
    }

    public void setMajorRank(int rankPower){
        if(jedisPool != null) {
            try (var jedis = jedisPool.getResource()) {
                if(jedis.exists("account#" + accountManager.getAccount().getUuid())) {
                    jedis.hset("account#" + accountManager.getAccount().getUuid(), "major-rank", String.valueOf(rankPower));
                    jedis.close();
                } else
                    accountManager.getAccountQuery().update(UpdateOperators.set("major-rank", rankPower)).execute();
            }
        }else {
            accountManager.getAccountQuery().update(UpdateOperators.set("major-rank", rankPower)).execute();
        }
    }

    public void addRank(int rankPower){
        var uuid = accountManager.getAccount().getUuid();
        if(jedisPool != null) {
            try (var jedis = jedisPool.getResource()) {
                if (jedis.exists("account#" + uuid)) {
                    var ranksKey = jedis.hget("account#" + uuid, "ranks");

                    if (jedis.sismember(ranksKey, String.valueOf(rankPower)))
                        return;

                    jedis.sadd(ranksKey, String.valueOf(rankPower));
                    jedis.close();
                } else
                    accountManager.getAccountQuery().update(UpdateOperators.addToSet("ranks", rankPower)).execute();
            }
        } else
            accountManager.getAccountQuery().update(UpdateOperators.addToSet("ranks", rankPower)).execute();
    }

    public void removeRank(int rankPower){
        var uuid = accountManager.getAccount().getUuid();
        if(jedisPool != null) {
            try (var jedis = jedisPool.getResource()) {
                if (jedis.exists("account#" + uuid)) {
                    var ranksKey = jedis.hget("account#" + uuid, "ranks");

                    if (!jedis.sismember(ranksKey, String.valueOf(rankPower)))
                        return;

                    jedis.srem(ranksKey, String.valueOf(rankPower));
                    jedis.close();
                } else
                    accountManager.getAccountQuery().update(UpdateOperators.pullAll("ranks", List.of(rankPower))).execute();
            }
        }else
            accountManager.getAccountQuery().update(UpdateOperators.pullAll("ranks", List.of(rankPower))).execute();
    }

    public boolean hasExactMajorRank(int rankId){
        return this.getMajorRank().getId() == rankId;
    }

    public boolean hasExactRank(int rankId){
        return this.getRanks().stream().anyMatch(rank -> rank.getId() == rankId);
    }

    public boolean hasExactMajorOrNotRank(int rankId){
        return this.hasExactMajorRank(rankId) || this.hasExactRank(rankId);
    }

    public boolean hasMajorRank(){
        if(jedisPool != null) {
            try (var jedis = jedisPool.getResource()) {
                if (jedis.exists("account#" + accountManager.getAccount().getUuid())) {
                    try {
                        return jedis.hget("account#" + accountManager.getAccount().getUuid(), "major-rank") != null;
                    } finally {
                        jedis.close();
                    }
                }else
                    return accountManager.getAccountQuery().filter(Filters.exists("major-rank")).count() != 0;
            }
        }
        return accountManager.getAccountQuery().filter(Filters.exists("major-rank")).count() != 0;
    }

    public boolean hasRanks(){
        return this.getRanks().size() != 0;
    }

    public abstract T getMajorRank();
    public abstract List<T> getRanks();
}
