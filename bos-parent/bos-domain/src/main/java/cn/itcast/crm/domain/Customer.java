package cn.itcast.crm.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "t_customer", catalog = "db_crm")
@XmlRootElement(name = "Customer")
public class Customer {
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	private String id;
	private String name;
	private String address;
	private String telephone;
	private String station;
	private String decidedzoneId;

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", address=" + address + ", telephone=" + telephone + ", station=" + station + ", decidedzoneId=" + decidedzoneId + "]";
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getDecidedzoneId() {
		return decidedzoneId;
	}

	public void setDecidedzoneId(String decidedzoneId) {
		this.decidedzoneId = decidedzoneId;
	}

}
