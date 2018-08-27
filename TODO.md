# DRCMon TODO
#Probes
##Include info
* Start
* End
* Success 
* Failure message 
* Host info
* Returned data value 

##Message Queue for response
* Option of AMPQ vs JMS

##Monitor engine 
* _Get probes from dB_
* _Schedule probes_
* _Check for updates_ 
* Check health
* Local message queue


#UI 
DRCMon – UI


Overview
* Status
* Add probes
* Modify probes
* Delete probes – we will not delete probes, only disable them
* (Re)Start engine
* Show message queue _future_
* Show probe status
* Overview
*	running status
*	<start> <stop> <refresh>
*	number probes total/running/succ/last run
* Menu?
*	Search for probe ( by type, by string? )
*	Display all probes
*	Add probe
*	update probe – update custom data – enable disable
*	show probe history
	
##Data Store

### Probe Config
* Unique identifier - internal
* Artifact - External Reference
* Monitor type
* Target -- 
host/ip, Port, Protocol
* Polling Interval
* Last modification
* Delay time

###Alert policy
* Responses
* Start time
* End time
* Success - boolean
* Failure message
* Metrics -- Key Value pairs
* Events
* Maintenance windows

###Probe Types
* _Ping ( TCP isReachable() )_ 
* _Port Monitor_
* Web Monitor - using Selenium
* _REST Get_
* _SQL Query_
* SNMP?
* Custom?
* Traceroute?
* Ping - (native) ?

##INFR DB ( id ? )
* Environments - Production
* Services - Database/Web/App etc...
* Resources - IP, DB instance, Port, FileSystem, Memory, CPU
* Software/Applications
* Could be tied to hierarchy in Monitoring system
* Low Level Resources would be “probed”


## Random items
* distribution method for probes - i.e. the Distributed part
* track Schema ( liquibase )
* _Better mechanism for probe initialization_
* _ad-hoc config for probes ( JSON field )_