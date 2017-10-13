# COMS4200
Welcome to team M's COMS4200 SDN project. Our project is on developing a SDN Monitoring and Visualisation tool. Below we have included a explaination of the code stored in this repo, along with instructions on how to setup and run our application.

## Install & Running instructions
To test our Visualisation tool, you first need to ensure you have visual box runnung the the COMS-4200-IMAGE.ova. You also need Elasticsearch and Kibana running on your system of choice, either on your local device or on a external device accessible over the internet.

### Steps to setup and run the ONOS application
The follow will allow you to execute our ONOS application inside the courses Virtual Machine and collect real data from a SDN.

1. Load the COMS-4200-IMAGE.ova into VirtualBox and run it.
2. Start the SDN using Mininet with the following command: `sudo mn --topo tree,3,2 --mac --controller remote`.
3. Create a new ONOS application with the command `onos-create-app` in a Bash terminal window.
    1. When prompted, assign Property groupId: org.hub.app, Property artifactId: hub, and Property version: 1.10.0 
    2. Press enter for the rest, and then when asked to confirm enter 'y'.
4. Replace the POM.xml file generated with the POM.xml from the project repository (found in `~/hub/`).
5. Replace the AppComponent.java file generated with the AppComponent.java from the project repository (found in `~/hub/src/main/java/org/hub/app`).
6. From the `~/hub` directory, run a clean Maven install of the project with the `mci` command (`~/hub/mci`).
7. In a different terminal window, run `onos-buck run onos-local –- clean` to start ONOS. This could take some time.
8. From a different terminal window again, run `onos-app localhost install! target/hub-1.10.0.oar` to install ONOS and run it.
    1. If you ever need to change the controller, run `onos-app localhost reinstall! target/hub-1.10.0.oar` to reinstall and run ONOS.
9. If you ever need to stop the app, run `onos localhost` to connect to your ONOS service, and then run from the ONOS CLI `deactivate org.hub.app`.
10. To create spikes in the data, perform various ping commands in the Mininet terminal (i.e. `pingall`).

Note: The app currently writes network statistics to `~/filename.json` rather than sending data to Elasticsearch directly through the Java API. The commands required to send this data are still included in the in the AppComponent.java, just they are commented out. The same goes for the required libraries in the pom.xml file.

### Steps to setting up Elasticsearch and Kibana
The follow will allow you to create a Elasticsearch and Kibana application, capable of receiving API messages with data, or capable of receiving data via JSON form from a file.


## The Repo

### Controller Folder
The folder named 'controller' contains our applications ONOS AppController and the builds associated Maven POM file. When setting up the project on your device, ensure you use both of these files.

### Data
This folder contains dummy data we have generated using our ONOS application. This data would normally be sent through to Elasticsearch via the Java REST Client API [5.6], but due to complications, this does not work. As a result, this simulation data has been recorded, and then needs to be feed to Elasticsearch manually, thus simlulating our actual solution.

### Elastic
This folder contains the required scripts for setting up your Elasticsearch and Kibana dashboards to match our project.
