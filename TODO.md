# DRCMon TODO
##Probes
###Include info
* Start
* End
* Success 
* Failure message 
* Host info
* Returned data value 

###Message Queue for response
* Option of AMPQ vs JMS

###Monitor engine 
* Get probes from dB
* Schedule probes
* Check for updates 
* Check health
* Local message queue


###UI 
* Add probes
* Modify probes
* Delete probes 
* (Re)Start engine
* Show message queue
* Show probe status

###Data Store

#### Probe Config
* Unique identifier - internal
* Artifact - External Reference
* Monitor type
* Target -- 
host/ip, Port, Protocol
* Polling Interval
* Last modification
* Delay time

####Alert policy
* Responses
* Start time
* End time
* Success - boolean
* Failure message
* Metrics -- Key Value pairs
* Events
* Maintenance windows

####Probe Types
* Ping ( TCP isReachable() )
* Port Monitor
* Web Monitor - using Selenium
* REST Get
* SQL Query
* SNMP?
* Custom?
* Traceroute?

###INFR DB ( id ? )
* Environments - Production
* Services - Database/Web/App etc...
* Resources - IP, DB instance, Port, FileSystem, Memory, CPU
* Software/Applications
* Could be tied to hierarchy in Monitoring system
* Low Level Resources would be “probed”


### Random items
* distribution method for probes - i.e. the Distributed part
* track Schema ( liquibase )
* Better mechanism for probe initialization
* ad-hoc config for probes ( JSON field )