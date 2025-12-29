## Structure complet de l'API Backend

```sh
ift2255-template-javalin/
├── rest-api/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/diro/ift2255/
│   │   │   │   ├── config/
│   │   │   │   │   └── Routes.java
│   │   │   │   ├── controller/
│   │   │   │   │   ├── AvisController.java
│   │   │   │   │   ├── CourseController.java
│   │   │   │   │   ├── PreferenceController.java
│   │   │   │   │   └── UserController.java
│   │   │   │   ├── model/
│   │   │   │   │   ├── Avis.java
│   │   │   │   │   ├── Course.java
│   │   │   │   │   ├── Opinion.java
│   │   │   │   │   ├── Preference.java
│   │   │   │   │   └── User.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── AvisService.java
│   │   │   │   │   ├── CourseService.java
│   │   │   │   │   ├── PreferenceService.java
│   │   │   │   │   ├── StatsCoursService.java
│   │   │   │   │   └── UserService.java
│   │   │   │   └── util/
│   │   │   │       ├── DatabaseUtil.java
│   │   │   │       ├── HttpClientApi.java
│   │   │   │       ├── HttpClientApiResponse.java
│   │   │   │       ├── HttpStatus.java
│   │   │   │       ├── ResponseUtil.java
│   │   │   │       ├── ValidationUtil.java
│   │   │   │       └── Main.java
│   │   │   └── resources/
│   │   │       └── data/
│   │   │           └── historique_cours_prog_117510...
│   │   └── test/
│   │       └── java/com/diro/ift2255/service/
│   │           ├── AvisServiceTest.java
│   │           ├── CourseServiceTest.java
│   │           ├── PreferenceServiceTest.java
│   │           ├── StatsCoursServiceTest.java
│   │           └── UserServiceTest.java
│   ├-avis.db
|   |-- target/
│   ├── pom.xml
│   └── README.md
└── .gitignore

```

## Architecture

- Ce template suit principalement le modèle MVC étendu avec une couche de services :  

    Model (model/) : Représentation des entités du domaine (ex. User, Course, Avis, Preference, Opinion).  

- Controller (controller/) : Gestion des requêtes HTTP (validation de base, paramètres, statuts) et délégation vers les services pour la logique métier.

- Service (service/) : Logique métier centrale (orchestration des opérations, règles de domaine, préparation des réponses à partir des modèles et des accès aux données).

- Util (util/) : Fonctions utilitaires réutilisables (validation, formatage de réponses HTTP, accès à la base de données, wrappers HTTP externes, constantes de statut, etc.).

​- Config (config/) : Configuration du serveur Javalin et définition des routes HTTP (mapping des URL vers les contrôleurs).

​- Ressources (resources/data/) : Données externes et fichiers de configuration utilisés par l’API (ex. historique des cours).

​- Tests (src/test/java/...) : Tests unitaires des services et de la logique métier (ex. UserServiceTest, CourseServiceTest, etc.).

- Main.java : Point d’entrée de l’application qui initialise le serveur Javalin, applique la configuration et enregistre les routes.

- avis.db ( SQLite ) : Base de donnée qui contient la liste des données agréer pour les statistiques, et contient aussi les avis des étudiants


## Installation des dépendences et lancement

Ouvrez la commande de ligne ( Moi je suis sur linux, en fonction de votre système suivez les instructions de votre système)
Il faut être dans le dossier rest-api.

- mvn exec:java -Dexec.mainClass=com.diro.ift2255.Main
- ou bien le lancer directement dans visual studio code

## Message de confirmation 

[INFO] Scanning for projects...  
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-metadata.xml  
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/maven-metadata.xml  
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/maven-metadata.xml (20 kB at 23 kB/s)  
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-metadata.xml (14 kB at 16 kB/s)  
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/exec-maven-plugin/maven-metadata.xml  
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/exec-maven-plugin/maven-metadata.xml (1.1 kB at 3.4 kB/s)  
[INFO]   
[INFO] ---------------------< com.diro.ift2255:rest-api >----------------------  
[INFO] Building rest-api 1.0-SNAPSHOT  
[INFO]   from pom.xml   
[INFO] --------------------------------[ jar ]---------------------------------  
[INFO]   
[INFO] --- exec:3.6.3:java (default-cli) @ rest-api ---  
Import stats_cours terminé.  
[com.diro.ift2255.Main.main()] INFO io.javalin.Javalin - Starting Javalin ...  
[com.diro.ift2255.Main.main()] INFO org.eclipse.jetty.server.Server - jetty-11.0.25; built: 2025-03-13T00:15:57.301Z; git:   a2e9fae3ad8320f2a713d4fa29bba356a99d1295; jvm 21.0.9+10-Ubuntu-125.04  
[com.diro.ift2255.Main.main()] INFO org.eclipse.jetty.server.session.DefaultSessionIdManager - Session workerName=node0  
[com.diro.ift2255.Main.main()] INFO org.eclipse.jetty.server.handler.ContextHandler - Started o.e.j.s.ServletContextHandler@4e162f5a{/,null,AVAILABLE}  
[com.diro.ift2255.Main.main()] INFO org.eclipse.jetty.server.AbstractConnector - Started ServerConnector@77a96387{HTTP/1.1, (http/1.1)}{0.0.0.0:7070}  
[com.diro.ift2255.Main.main()] INFO org.eclipse.jetty.server.Server - Started Server@13665341{STARTING}[11.0.25,sto=0] @6195ms  
[com.diro.ift2255.Main.main()] INFO io.javalin.Javalin -   
```sh
       __                  ___           _____  
      / /___ __   ______ _/ (_)___      / ___/  
 __  / / __ `/ | / / __ `/ / / __ \    / __ \  
/ /_/ / /_/ /| |/ / /_/ / / / / / /   / /_/ /  
\____/\__,_/ |___/\__,_/_/_/_/ /_/    \____/  
```
  
       https://javalin.io/documentation  
  
[com.diro.ift2255.Main.main()] INFO io.javalin.Javalin - Javalin started in 296ms \o/  
[com.diro.ift2255.Main.main()] INFO io.javalin.Javalin - Listening on http://localhost:7070/  
[com.diro.ift2255.Main.main()] INFO io.javalin.Javalin - You are running Javalin 6.7.0 (released June 22, 2025. Your Javalin version is 187 days old. Consider   checking for a newer version.).  
Serveur démarré sur http://localhost:7070  
  
## Lancement pour le test

Toujours dans le même dossier rest-api  

- mvn test

## Message de confirmation avec tous les tests

[INFO] Scanning for projects...  
[INFO]   
[INFO] ---------------------< com.diro.ift2255:rest-api >----------------------  
[INFO] Building rest-api 1.0-SNAPSHOT  
[INFO]   from pom.xml  
[INFO] --------------------------------[ jar ]---------------------------------  
[INFO]   
[INFO] --- resources:3.3.1:resources (default-resources) @ rest-api ---  
[INFO] Copying 1 resource from src/main/resources to target/classes  
[INFO]   
[INFO] --- compiler:3.13.0:compile (default-compile) @ rest-api ---  
[INFO] Nothing to compile - all classes are up to date.  
[INFO]   
[INFO] --- resources:3.3.1:testResources (default-testResources) @ rest-api ---  
[INFO] skip non existing resourceDirectory /home/yeah/Desktop/ift2255/implementation/ift2255-template-javalin/rest-api/src/test/resources  
[INFO]   
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ rest-api ---  
[INFO] Nothing to compile - all classes are up to date.  
[INFO]   
[INFO] --- surefire:3.3.1:test (default-test) @ rest-api ---  
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider  
[INFO]   
[INFO] -------------------------------------------------------  
[INFO]  T E S T S  
[INFO] -------------------------------------------------------  
[INFO] Running com.diro.ift2255.service.AvisServiceTest  
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.420 s -- in com.diro.ift2255.service.AvisServiceTest  
[INFO] Running com.diro.ift2255.service.UserServiceTest   
  
================================================================================  
UserService Tests  
================================================================================  
  
TEST: Get user by ID should return the correct user when found  
    ├─ Method: testGetUserByIdFound  
    ├─ Assertions:  
    │   ├─ [PASS] User with ID 1 exists  
    │   └─ [PASS] Retrieved user: Alice  
    └─ Duration: 20 ms  
  
TEST: Get all users should return 2 mock users  
    ├─ Method: testGetAllUsers  
    ├─ Assertions:  
    │   └─ [PASS] Retrieved 2 users as expected  
    └─ Duration: 9 ms  
  
TEST: Get user by ID should return empty when user is not found  
    ├─ Method: testGetUserByIdNotFound  
    ├─ Assertions:  
    │   └─ [PASS] No user found as expected (Optional.empty())  
    └─ Duration: 2 ms  
  
================================================================================  
COMPLETED: UserService Tests  
================================================================================  
  
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.065 s -- in com.diro.ift2255.service.UserServiceTest  
[INFO] Running com.diro.ift2255.service.StatsCoursServiceTest  
[StatsCoursService] Recherche stats pour: IFT9999  
[StatsCoursService] Aucune stats pour: IFT9999  
[StatsCoursService] Recherche stats pour: IFT2255  
[StatsCoursService] Stats trouvées pour: IFT2255  
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.063 s -- in com.diro.ift2255.service.StatsCoursServiceTest  
[INFO] Running com.diro.ift2255.service.CourseServiceTest  
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended  
WARNING: A Java agent has been loaded dynamically (/home/yeah/.m2/repository/net/bytebuddy/byte-buddy-agent/1.14.15/byte-buddy-agent-1.14.15.jar)  
WARNING: If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning  
WARNING: If a serviceability tool is not in use, please run with -Djdk.instrument.traceUsage for more information  
WARNING: Dynamic loading of agents will be disallowed by default in a future release  
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.488 s -- in com.diro.ift2255.service.CourseServiceTest  
[INFO] Running com.diro.ift2255.service.PreferenceServiceTest  
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.130 s -- in com.diro.ift2255.service.PreferenceServiceTest  
[INFO] Running com.diro.ift2255.service.CourseControllerTest  
[CourseController] GET /courses/IFT9999/stats  
[CourseController] ID valide, appel statsCoursService.getStatsForCourse(IFT9999)  
[CourseController] AUCUNE statistique trouvée pour: IFT9999  
[CourseController] GET /courses/IFT2255/stats  
[CourseController] ID valide, appel statsCoursService.getStatsForCourse(IFT2255)  
[CourseController] Stats trouvées pour IFT2255 -> com.diro.ift2255.service.StatsCoursService$CoursStats@60783105  
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.563 s -- in com.diro.ift2255.service.CourseControllerTest  
[INFO]   
[INFO] Results:  
[INFO] 
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0  
[INFO]   
[INFO] ------------------------------------------------------------------------  
[INFO] BUILD SUCCESS  
[INFO] ------------------------------------------------------------------------  
[INFO] Total time:  5.316 s  
[INFO] Finished at: 2025-12-26T12:03:45-05:00  
[INFO] ------------------------------------------------------------------------  
