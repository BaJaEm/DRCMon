package org.bajaem.netmon.drcmon.model;

import java.sql.Date;

public class ProbeConfig {

	private long id;
	private String artifactId;
	private ProbeType probeType;
	private String host;
	private int port;
	private String protocol;
	private int pollingInterval;
	private int delayTime;
	private Date createdOn;
	private String createdBy;
	private Date lastModifiedOn;
	private String lastModifiedBy;
}
