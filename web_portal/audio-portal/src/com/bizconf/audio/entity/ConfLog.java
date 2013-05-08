package com.bizconf.audio.entity;

import java.util.Date;
 


public class ConfLog  implements java.io.Serializable {

	private static final long serialVersionUID = 6377270844320706381L;
	private Integer id = 0;
     private Integer siteId =0;
     private Integer confId = 0;
     private Integer userId =0;
     private String userName ="";
     private Integer userRole = 2;
     private String email = "";
     private String phone = "";
     private Integer termType = 3;
     private Integer joinType;
     private Integer leaveType;
     private Date joinTime;
     private Date exitTime;
     private String joinIp;
     private Date createTime;
     private Integer delFlag;
     private Date delTime;


    // Constructors

    /** default constructor */
    public ConfLog() {
    }

	/** minimal constructor */
    public ConfLog(Integer id, Date joinTime, Date exitTime) {
        this.id = id;
        this.joinTime = joinTime;
        this.exitTime = exitTime;
    }
    
    /** full constructor */
    public ConfLog(Integer id, Integer siteId, Integer confId, Integer userId, String userName, Integer userRole, String email, String phone, Integer termType, Integer joinType, Integer leaveType, Date joinTime, Date exitTime, String joinIp, Date createTime, Integer delFlag, Date delTime) {
        this.id = id;
        this.siteId = siteId;
        this.confId = confId;
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.email = email;
        this.phone = phone;
        this.termType = termType;
        this.joinType = joinType;
        this.leaveType = leaveType;
        this.joinTime = joinTime;
        this.exitTime = exitTime;
        this.joinIp = joinIp;
        this.createTime = createTime;
        this.delFlag = delFlag;
        this.delTime = delTime;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return this.siteId;
    }
    
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getConfId() {
        return this.confId;
    }
    
    public void setConfId(Integer confId) {
        this.confId = confId;
    }

    public Integer getUserId() {
        return this.userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserRole() {
        return this.userRole;
    }
    
    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTermType() {
        return this.termType;
    }
    
    public void setTermType(Integer termType) {
        this.termType = termType;
    }

    public Integer getJoinType() {
        return this.joinType;
    }
    
    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public Integer getLeaveType() {
        return this.leaveType;
    }
    
    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public Date getJoinTime() {
        return this.joinTime;
    }
    
    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public Date getExitTime() {
        return this.exitTime;
    }
    
    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public String getJoinIp() {
        return this.joinIp;
    }
    
    public void setJoinIp(String joinIp) {
        this.joinIp = joinIp;
    }

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlag() {
        return this.delFlag;
    }
    
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getDelTime() {
        return this.delTime;
    }
    
    public void setDelTime(Date delTime) {
        this.delTime = delTime;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfLog other = (ConfLog) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConfLog [id=" + id + ", siteId=" + siteId + ", confId="
				+ confId + ", userId=" + userId + ", userName=" + userName
				+ ", userRole=" + userRole + ", email=" + email + ", phone="
				+ phone + ", termType=" + termType + ", joinType=" + joinType
				+ ", leaveType=" + leaveType + ", joinTime=" + joinTime
				+ ", exitTime=" + exitTime + ", joinIp=" + joinIp
				+ ", createTime=" + createTime + ", delFlag=" + delFlag
				+ ", delTime=" + delTime + "]";
	}
   








}