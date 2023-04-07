package ApplsActor23;

import unibo.basicomm23.actors23.Actor23Utils;
import unibo.basicomm23.actors23.example.MainConfigActors23_all;
import unibo.basicomm23.utils.CommUtils;

public class MainProdConsAll {
    public void configureTheSystem(){
        String userDir = System.getProperty("user.dir"); //C:\Didattica2023\issLab23\ProducerConsumer
        CommUtils.outblue("Working Directory = " + userDir);
        //Actor23Utils.trace = true;
        //Connection.trace   = true;
        Actor23Utils.createContexts("localhost",
                "app/src/main/java/ApplsActor23/ProdConsActor23_all.pl",
                "app/src/main/java/ApplsActor23/sysRules.pl");
    }
    public static void main(String[] args ){
        new MainProdConsAll().configureTheSystem();
    }
}
