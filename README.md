# account-system (v0.4.0)
My original account system to manage accounts on a minecraft server.

## Développeurs

---

**PS :** Pour récupérer une instance de la classe `AccountSystem` il suffit de faire ceci :

```java
var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
if(provider != null){
    var accountSystem = provider.getProvider();
    //CODE HERE
}
```

---

### Gérer le compte d'un joueur 
Pour récupérer et gérer le compte d'un joueur il vous suffit de créer une nouvelle instance de la classe `AccountManager` et ceci à chaque fois que vous ne disposez pas déjà d'une instance de cette dernière.

Pour en créer une, rien de plus simple :

```java
final AccountManager accountManager = new AccountManager(*instance de AccountSystem*, *Un objet Player*);
```
ou :

```java
final AccountManager accountManager = new AccountManager(*instance de AccountSystem*, *le nom du joueur (String)*, *l'uuid du joueur (String)*);
```

Si vous souhaitez récupérer la valeur d'un des champs d'un compte, vous pouvez utiliser la méthode `get(String)`. Celle-ci prend en paramètre un String qui représente la clé, soit le nom du champ recherché, puis retourne un Object représentant la valeur du champ. Cet object peut être casté, cependant, il est recommandé de prioriser la méthode `getAccount#get(+ type de l'objet (ex: Boolean))` afin de ne pas avoir à caster la valeur retournée.\
![](https://i.imgur.com/ScdTX8Q.png)

Par contre, si vous souhaitez changer la valeur d'un des champs d'un compte, une seule méthode existe pour rendre la chose générique : `set(String, Object)`, prenant en paramètre le nom de champ et la nouvelle valeur.\
![](https://i.imgur.com/CjwUVUh.png)

### Gérer les rangs d'un joueur

Pour gérer les d'un joueur il vous faut récupérer une instance de la classe `Rank` pour ceci rien de plus simple, il vous faut au préalable une instance de la classe `AccountSystem` de disponible.

```java
final Rank rank = account.newRankManager();
```

**Attention : Cette méthode CREER une NOUVELLE instance de la classe Rank, vous n'obtiendrez donc pas la même instance à chaque fois que vous appelez cette méthode, c'est bien pour ça que le concept de variable a été inventé :smile:**

## Les commandes

### /rank
La seule commande disponible actuellement est la commande /rank, elle permet de set le major rank de quelqu'un, voir ses rangs, lui ajouter un rang et lui en enlever un.

**/rank show [joueur/uuid]** permet de voir les rangs d'un joueur\
**/rank set [joueur/uuid] [id/nom du rang/power]** permet de changer le `rang majeur` d'un joueur\
**/rank add [joueur/uuid] [id/nom du rang/power]** permet d'ajouter un rang fictif à un joueur\
**/rank remove [joueur/uuid] [id/nom du rang/power]** permet de retirer un rang fictif à un joueur

## 'super-user'

Le champ super-user est ajouté manuellement à un compte dans la base de données
Il est a été ajouté à 2 utilisateurs jusqu'à présent.

Pour vérifier si un joueur a cette autorisation :

```java
if(accountManager.getAccount().containsKey("super-user") && accountManager.getAccount().getBoolean("super-user")){
  //CODE
}
```

Et pour vérifier s'il ne l'a pas :

```java
if(!accountManager.getAccount().containsKey("super-user") || !accountManager.getAccount().getBoolean("super-user")){
  //CODE
}
```

:warning: Cette version supporte le champ '**super-user**' :warning: 

## Permissions

Depuis la version __0.3.3__, un système de permission est disponible. En effet, celui va être lié à la base de données, il suffit de créer une collection dans la base de données nommée **permissions**. Ensuite, afin de gérer les permissions correctement, une commande est disponible : la commande `permission`.

D'autre part, voici le pattern d'une permission (sous la forme d'un document BSON) dans la db
![pattern](https://i.imgur.com/JdzRJHO.png)

Une commande est mise à disposition pour gérer les permissions :

**/permission show <target>** permet de voir les permissions d'un joueur/une uuid/un rang\
**/permission add <target> <permission>** permet d'ajouter une permission à un joueur/une uuid/un rang\
**/permission remove <target> <permission>** permet de retirer une permission à un joueur/une uuid/un rang\
**/permission reload <target>** permet de recharger les permissions d'un joueur/une uuid/un rang\
**/permission except <target> <permission>** permet d'ajouter une exception à une permission\
**/permission unexcept <target> <permission>** permet de retirer une exception à une permission\
**/setdefault <a_permission> <true/false>** permet de définir une permission par défaut ou non
