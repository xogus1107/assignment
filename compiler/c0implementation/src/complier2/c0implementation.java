package complier2;

import java.io.*;
import java.util.ArrayList;

public class c0implementation {

	ArrayList<String> firstrule = new ArrayList<String>();
	ArrayList<String> implementation = new ArrayList<String>();
	String temp = new String();

	// 원래있는 replacefirst를 개선한 함수
	public String replaceOne(String fullStr, String oldStr, String newStr) {
		StringBuffer res = new StringBuffer();
		try {
			if (fullStr == null || fullStr.length() == 0) {
				return fullStr;
			}

			if (oldStr == null || oldStr.length() == 0) {
				return fullStr;
			}

			if (newStr == null || newStr.length() == 0) {
				return fullStr;
			}

			int posOldStr = fullStr.indexOf(oldStr);

			if (posOldStr >= 0) {
				res.append(fullStr.substring(0, posOldStr));
				res.append(newStr);
				res.append(fullStr.substring(posOldStr + oldStr.length()));
			} else {
				return fullStr;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return fullStr;
		}

		if (res == null || res.length() == 0) {
			return fullStr;
		}

		return res.toString();
	}

	private void closure(String rule) {
		//closure위해 화살표앞에 .찍음
		rule = replaceOne(rule, ">", "->.");
		temp += "[" + rule + "] ";
		//'.'다음거 찾아서 nonterminal 이면 재귀적으로closure
		int intpoint = rule.indexOf(".") + 1;
		if (intpoint == rule.length())
			return;
		char charpoint = rule.charAt(intpoint);
		if (Character.isUpperCase(charpoint) == false)
			return;

		if (Character.isUpperCase(charpoint) == true && charpoint != rule.charAt(0)) {
			for (int i = 1; i < firstrule.size(); i++) {
				if (firstrule.get(i).charAt(0) == charpoint) {
					closure(firstrule.get(i));
				}
			}
		}
	}

	private void togo(String item) {
		String[] str = item.split(" ");

		ArrayList<String> charpointList = new ArrayList<String>();
		// goto 해야하는 rule즉 .다음 문자를 찾는다.
		for (int i = 0; i < str.length; i++) {
			int intpoint = str[i].indexOf(".") + 1;
			if (intpoint == str[i].indexOf(".") - 1)
				continue;
			String charpoint = str[i].charAt(intpoint) + "\0";
			charpointList.add(charpoint);
		}
		for (int i = 0; i < charpointList.size(); i++) {
			if (charpointList.get(i) == "[]]") {
				charpointList.remove(i);
			}
		}
		// 중복삭제
		for (int i = 0; i < charpointList.size(); i++) {

			for (int j = 0; j < charpointList.size(); j++) {
				if (i == j)
					continue;
				else if (charpointList.get(i).equals(charpointList.get(j))) {
					charpointList.remove(j);
				}
			}
		}
		///찾은 문자로 goto
		for (int i = 0; i < charpointList.size(); i++) {
			Goto(item, charpointList.get(i).charAt(0));

		}

	}

	private void Goto(String item, char alpha) {
		String[] str = item.split(" ");
		//'.'위치 옮기고 다음이 nonterminal이면 closure
		for (int i = 0; i < str.length; i++) {
			if (str[i].contains("." + alpha) == true && alpha != ']') {

				str[i] = replaceOne(str[i], "." + alpha, alpha + ".");
				str[i] = str[i] + " ";
				temp += (str[i]);
				int intpoint = str[i].indexOf(".") + 1;

				if (Character.isUpperCase(str[i].charAt(intpoint)) == true) {
					for (int j = 0; j < firstrule.size(); j++) {
						if (str[i].charAt(intpoint) == firstrule.get(j).charAt(0)) {
							closure(firstrule.get(j));
						}
					}
				}
			}

		}
		//새로운 I arraylist에 저장
		if (temp != "") {
			implementation.add(temp);
			temp = "";
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		c0implementation c0 = new c0implementation();
		//inputfile read
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		while (br.readLine() != null) {
			c0.firstrule.add(br.readLine());
		}
		br.close();

		// argumented productionrule
		c0.firstrule.add(0, "S>" + c0.firstrule.get(0).charAt(0));

		// closure(I0)
		c0.closure(c0.firstrule.get(0));
		c0.implementation.add(c0.temp);
		c0.temp = "";
		//모든 goto, 중복삭제
		for (int i = 0; i < c0.implementation.size(); i++) {
			c0.togo(c0.implementation.get(i));
			for (int j = 0; j < c0.implementation.size(); j++) {
				for (int k = 0; k < c0.implementation.size(); k++) {
					if (j == k) {
					} else if (c0.implementation.get(k).equals(c0.implementation.get(j))) {
						c0.implementation.remove(k);
					}
				}
			}
		}
		//write outputfile
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		for (int i = 0; i < c0.implementation.size(); i++) {
			writer.write("I" + i);
			writer.newLine();
			writer.write(c0.implementation.get(i));
			writer.newLine();
		}
		writer.flush();
		writer.close();

		for (int i = 0; i < 12; i++)
			System.out.println(c0.implementation.get(i));

	}
}
