# video-inventory-managment

This repo will contain customer management APIs.


**Prerequisite: **
*  openjdk 17
*  mysql
*  git client


**Prepare Database: **

    CREATE USER 'bappi'@'localhost' IDENTIFIED BY '123456';
    CREATE DATABASE inventory_management_db;
    GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES, RELOAD on *.* TO 'bappi'@'localhost' WITH GRANT OPTION;


Build the war file using this command

./gradlew clean bootWar

The war will found on \build\libs\video-inventory-management.war

Finally, copy the generated war to /path/to/tomcat10/webapps/ directory as video-inventory-management.war

now as we have our db created we need to run the db script

1. resources/db_script/v1_insert_user_data.sql

For test please export the postman collection from this location
resources/postman_collection/video_inventory_management.postman_collection.json
