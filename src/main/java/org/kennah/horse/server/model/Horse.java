package org.kennah.horse.server.model;

import java.io.Serializable;
import java.util.List;

public class Horse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String number;
	private String draw;
	private String profile;
	private String silks;
	private String age;
	private String lastRun;
	private String form;
	private String weight;
	private String allowance;
	private String jockey;
	private String trainer;
	private List<Past> past;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public String getSilks() {
		return silks;
	}

	public void setSilks(String silks) {
		this.silks = silks;
	}

	public String getLastRun() {
		return lastRun;
	}

	public void setLastRun(String lastRun) {
		this.lastRun = lastRun;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getJockey() {
		return jockey;
	}

	public void setJockey(String jockey) {
		this.jockey = jockey;
	}

	public String getTrainer() {
		return trainer;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

	public List<Past> getPast() {
		return past;
	}

	public void setPast(List<Past> past) {
		this.past = past;
	}

	public String getAllowance() {
		return allowance;
	}

	public void setAllowance(String allowance) {
		this.allowance = allowance;
	}

	@Override
	public String toString() {
		return "\n      Horse " + "[name=" + name + ", number=" + number + ", draw=" + draw + ", lastRun=" + lastRun
				+ ", form=" + form + ", weight=" + weight + ", allowance=" + allowance + ", jockey=" + jockey
				+ ", trainer=" + trainer + ", silks=" + silks + ", profile=" + profile + ", past=" + past + "]";
	}
}
