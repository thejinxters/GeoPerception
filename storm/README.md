#Storm-Setup
**Instructions for running counter **

##Run in Terminal

From GeoPerceptionApp directory: 

```bash
cd storm/topologies/TwitterTopStart/src/com/geoperception/startingtop/
javac -cp .:../../../../../../apache-storm-0.9.3/lib/*:../../../../../../twitter4j-4.0.2/lib/* *.java
 cd ../../..
 java -cp .:../../../apache-storm-0.9.3/lib/*:../../../twitter4j-4.0.2/lib/*:./com/geoperception/startingtop/* com.gperception.startingtop.StartingTopology <ConsumerKey> <ConsumerSecret> <AccessToken> <AccessTokenSecret>
```

The Consumer and Access keys can either be acquired through twitter's developer website or from the team's slack group.

##Run in Eclipse

The terminal commands are verbose and extensive, it's easier to install eclipse and run the storm topology through the IDE.

###1. Install Eclispe
In a browser, visit: https://www.eclipse.org/downloads/.
Follow install instructions for eclipse IDE for Java Developers.

###2. Import TwitterStartTop Project
From Eclipse workspace, Navigate to File -> Import -> General -> Existing Projects.
Use file browser to find the TwitterStartTop directory in the GeoPerceptionApp.
Import Project.

###3. Edit BuildPath for Jars
Right click on project in package explorer, select Buildpath -> Configure Buildpath.
Enter the Libraries tab and select the add external jars option.
Navigate to the storm folder in the GeoPerceptionApp.
Browse into the apache-storm-0.9.3/lib folder, select all jars, and click open.
Repeat for the twitter4j-4.0.2/lib folder.

###4. Edit Run Configurations
Right Click on TwitterTopStart in Eclipse Package Explorer.
Select Run As -> Run Configurations.
Select the StartingTopology configuration, Enter the Arguments tab.
Enter the following items `"<ConsumerKey>" "<ConsumerSecret>" "<AccessToken>" "<AccessTokenSecret>"`

The Consumer and Access keys can either be acquired through twitter's developer website or from the team's slack group.

###5. Run the project
