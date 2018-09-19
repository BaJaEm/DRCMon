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
* __Option of AMPQ vs JMS__ /Decided to go with JMS

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
* _Add probes_
* _Modify probes_
* Delete probes – we will not delete probes, only disable them -- just use update
* _(Re)Start engine_
* Show message queue _future_
* Show probe status - running - failed, etc...
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
*   Add Key - needs API to create - store keys in DB? - encrypted field
	
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
* Integration with SSO ( KeyCloak, etc... )
* services on linux/unix and Windows