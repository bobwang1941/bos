package cn.itcast.bos.domain.auth;
// Generated 2016-11-22 9:13:18 by Hibernate Tools 3.2.2.GA


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

/**
 * Function generated by hbm2java
 */
@Entity
@Table(name="auth_function"
    ,catalog="db_bos"
)
public class Function  implements java.io.Serializable {


     private String id;
     private String name;
     private String code;
     private String description;
     private String page;//路径，页面的
     private String generatemenu;
     private Integer zindex;
     private Function function;
     private Set<Role> roles = new HashSet<Role>(0);
     private Set<Function> functions = new HashSet<Function>(0);

    public Function() {
    }

    public Function(Function function, String name, String code, String description, String page, String generatemenu, Integer zindex, Set<Function> functions, Set<Role> roles) {
       this.function = function;
       this.name = name;
       this.code = code;
       this.description = description;
       this.page = page;
       this.generatemenu = generatemenu;
       this.zindex = zindex;
       this.functions = functions;
       this.roles = roles;
    }
    
    @Transient
    public String getpId(){
    	if(function != null){
    		return function.getId();
    	}
    	return "0";
    }
   
    @GenericGenerator(name="generator", strategy="uuid")@Id @GeneratedValue(generator="generator")
    @Column(name="id", unique=true, nullable=false, length=32)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pid")
    public Function getFunction() {
        return this.function;
    }
    
    public void setFunction(Function function) {
        this.function = function;
    }
    
    @Column(name="name")
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="code")
    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    @Column(name="description")
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name="page")
    public String getPage() {
        return this.page;
    }
    
    public void setPage(String page) {
        this.page = page;
    }
    
    @Column(name="generatemenu")
    public String getGeneratemenu() {
        return this.generatemenu;
    }
    
    public void setGeneratemenu(String generatemenu) {
        this.generatemenu = generatemenu;
    }
    
    @Column(name="zindex", precision=8, scale=0)
    public Integer getZindex() {
        return this.zindex;
    }
    
    public void setZindex(Integer zindex) {
        this.zindex = zindex;
    }
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "function")
    public Set<Function> getFunctions() {
        return this.functions;
    }
    
    public void setFunctions(Set<Function> functions) {
        this.functions = functions;
    }
@ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    @JoinTable(name="role_function", catalog="db_bos", joinColumns = { 
        @JoinColumn(name="function_id", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="role_id", nullable=false, updatable=false) })
	@JSON(serialize=false)
    public Set<Role> getRoles() {
        return this.roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }




}


