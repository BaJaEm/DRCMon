package org.bajaem.netmon.drcmon.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class ProbeType {

	@SequenceGenerator(name="key_seq", sequenceName="key_seq")
	@Id @GeneratedValue(generator="key_seq")
	private int id;
	private String name;
	private String description;

	public int getId() {
		return id;
	}

	public void setId(final int _id) {
		id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String _name) {
		name = _name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String _description) {
		description = _description;
	}

}
