package com.example.demo.model;

import java.util.List;

public class Post {

	private String id;
	private String title;
	private String desription;
	private String img;
	private String datecreate;
	private String dateupdate;
	private String userid;
	private String statusid;
	private List<String> likes;
	private String isdelete;
	private String idisvisible;
	private String flg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesription() {
		return desription;
	}

	public void setDesription(String desription) {
		this.desription = desription;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDatecreate() {
		return datecreate;
	}

	public void setDatecreate(String datecreate) {
		this.datecreate = datecreate;
	}

	public String getDateupdate() {
		return dateupdate;
	}

	public void setDateupdate(String dateupdate) {
		this.dateupdate = dateupdate;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getStatusid() {
		return statusid;
	}

	public void setStatusid(String statusid) {
		this.statusid = statusid;
	}

	public List<String> getLikes() {
		return likes;
	}

	public void setLikes(List<String> likes) {
		this.likes = likes;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getIdisvisible() {
		return idisvisible;
	}

	public void setIdisvisible(String idisvisible) {
		this.idisvisible = idisvisible;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", desription=" + desription + ", img=" + img + ", datecreate="
				+ datecreate + ", dateupdate=" + dateupdate + ", userid=" + userid + ", statusid=" + statusid
				+ ", likes=" + likes + ", isdelete=" + isdelete + ", idisvisible=" + idisvisible + "]";
	}

}
