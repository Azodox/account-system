package net.valneas.account.permission;

public class PermissionDatabase {

    private final AccountSystem accountSystem;
    private final MongoClient mongo;

    public PermissionDatabase(AccountSystem accountSystem){
        this.accountSystem = accountSystem;
        this.mongo = accountSystem.getMongo().getMongoClient();
    }

    public Permission[] getPermissions(){
        //TODO : find each document and retrive that into an array of Permission object.
       return null;
    }

    public MongoCollection<Document> getCollection(){
        return this.getDatabase().getCollection("permissions");
    }

    public MongoDatabase getDatabase(){
        return this.mongo.getDatabase(this.accountSystem.getConfig().getString("mongodb.database"));
    }

    private static class Permission {
        private final String permission;
        private final UUID player;
        private final RankUnit rank;

        public Permission(String permission, UUID player, RankUnit rank){
            this.permission = permission;
            this.player = player;
            this.rank = rank;
        }

        public String getPermission(){
            return this.permission;
        }

        @Nullable
        public UUID getPlayerUUID(){
            return this.player;
        }

        @Nullable
        public RankUnit getRank(){
            return this.rank;
        }
    }

}