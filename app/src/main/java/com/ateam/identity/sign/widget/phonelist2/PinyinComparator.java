package com.ateam.identity.sign.widget.phonelist2;

import java.util.Comparator;

import com.ateam.identity.sign.moduel.Student;


/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<Student> {

	public int compare(Student o1, Student o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
