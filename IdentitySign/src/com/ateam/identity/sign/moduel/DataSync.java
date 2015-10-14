package com.ateam.identity.sign.moduel;

import java.util.List;

public class DataSync extends HBaseObject{
	private List<User> teachers;
	private List<Student> students;
	public List<User> getTeachers() {
		return teachers;
	}
	public void setTeachers(List<User> teachers) {
		this.teachers = teachers;
	}
	public List<Student> getStudents() {
		return students;
	}
	public void setStudents(List<Student> students) {
		this.students = students;
	}
}
