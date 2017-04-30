package cn.itcast.bos.domain.bc;

// Generated 2016-11-14 11:01:03 by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

/**
 * Staff generated by hbm2java
 */
@Entity
@Table(name = "bc_staff", catalog = "db_bos")
public class Staff implements java.io.Serializable {

	private String id;
	private String name;
	private String telephone;
	private Character haspda;
	private Character deltag = '0';// 0 未删除 1 已经删除 delete tag 删除标记 取派员删除 逻辑删除 物理删除(数据库彻底删除)
	private String station;
	private String standard;
	private Set<DecidedZone> decidedZones = new HashSet<DecidedZone>(0);

	public Staff() {
	}

	public Staff(String name, String telephone, Character haspda, Character deltag, String station, String standard, Set<DecidedZone> decidedZones) {
		this.name = name;
		this.telephone = telephone;
		this.haspda = haspda;
		this.deltag = deltag;
		this.station = station;
		this.standard = standard;
		this.decidedZones = decidedZones;
	}

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TELEPHONE", length = 20)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "HASPDA", length = 1)
	public Character getHaspda() {
		return this.haspda;
	}

	public void setHaspda(Character haspda) {
		this.haspda = haspda;
	}

	@Column(name = "DELTAG", length = 1)
	public Character getDeltag() {
		return this.deltag;
	}

	public void setDeltag(Character deltag) {
		this.deltag = deltag;
	}

	@Column(name = "STATION", length = 40)
	public String getStation() {
		return this.station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	@Column(name = "STANDARD", length = 100)
	public String getStandard() {
		return this.standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "staff")
	@JSON(serialize = false)
	public Set<DecidedZone> getDecidedZones() {
		return this.decidedZones;
	}

	public void setDecidedZones(Set<DecidedZone> decidedZones) {
		this.decidedZones = decidedZones;
	}

}
