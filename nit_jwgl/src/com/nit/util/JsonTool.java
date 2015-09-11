package com.nit.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nit.bean.BorrowedBook;
import com.nit.bean.Cet46;
import com.nit.bean.Course;
import com.nit.bean.Level;
import com.nit.bean.Resit;
import com.nit.bean.RollCallHis;
import com.nit.bean.RollCallMenber;
import com.nit.bean.Score;

public class JsonTool {
	public static String getMessage(JSONObject jsonObject)
			throws JSONException, UnsupportedEncodingException {

		String message = jsonObject.getString("message");
		return encode(message);
	}

	public static Map<String, String> getMessageAndName(String json)
			throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		JSONObject jsonObject = new JSONObject(json);
		String message = jsonObject.getString("message");
		String name = null;
		if (message.equals("登录成功")) {
			JSONObject data = (JSONObject) jsonObject.get("data");
			name = data.getString("name");
		} else {
			name = "";
		}
		values.put("message", message);
		values.put("name", name);
		return values;
	}

	public static Map<String, String> getMessageAndName(JSONObject jsonObject)
			throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		String message = jsonObject.getString("message");
		String name = null;
		if (message.equals("登录成功")) {
			JSONObject data = (JSONObject) jsonObject.get("data");
			name = data.getString("name");
		} else {
			name = "";
		}
		values.put("message", message);
		values.put("name", name);
		return values;
	}

	public static Map<String, String> getMessageNameAndCookie(
			JSONObject jsonObject) throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		String message = jsonObject.getString("message");
		String name = null;
		String cookie = null;

		if (message.equals("登录成功")) {
			JSONObject data = (JSONObject) jsonObject.get("data");
			name = data.getString("name");
			cookie = jsonObject.getString("cookie");
		} else {
			name = "";
			cookie="";
		}
		values.put("message", message);
		values.put("name", name);
		values.put("cookie", cookie);
		return values;
	}

	public static Map<String, String> getMessageNameAndCount(
			JSONObject jsonObject) throws Exception {
		Map<String, String> values = new HashMap<String, String>();
		String message = jsonObject.getString("message");
		String name = null;
		String count = null;
		String cookie = null;
		if (message.equals("登录成功")) {
			JSONObject data = (JSONObject) jsonObject.get("data");
			cookie = jsonObject.getString("cookie");
			name = data.getString("name");
			count = data.getString("allLendBookNum");
		} else {
			name = "";
			cookie = "";
		}
		values.put("message", message);
		values.put("name", name);
		values.put("count", count);
		values.put("cookie", cookie);
		return values;
	}

	public static String getCookie(JSONObject jsonObject) throws Exception {
		String cookie = null;
		if (getMessage(jsonObject).equals("登录成功")) {
			cookie = jsonObject.getString("cookie");
		} else {
			cookie = "";
		}

		return encode(cookie);
	}

	public static Map<String, Object> getCurrentBorrowBook(JSONObject jsonObject)
			throws Exception {
		Map<String, Object> info = new HashMap<String, Object>();
		List<BorrowedBook> books = null;

		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		books = new ArrayList<BorrowedBook>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			books.add(new BorrowedBook(object.getString("code"), encode(object
					.getString("title")), object.getString("borrowDate"),
					object.getString("returnDate"),
					object.getString("isRenew"), object.getString("check"),
					encode(object.getString("image"))));
		}
		info.put("message", message);
		info.put("data", books);

		return info;

	}

	public static Map<String, Object> getHistoryBorrowBook(JSONObject jsonObject)
			throws Exception {
		Map<String, Object> info = new HashMap<String, Object>();
		List<BorrowedBook> books = null;

		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		books = new ArrayList<BorrowedBook>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			books.add(new BorrowedBook(object.getString("code"), encode(object
					.getString("title")), object.getString("author"), object
					.getString("borrowDate"), object.getString("returnDate")));
		}
		info.put("message", message);
		info.put("data", books);

		return info;

	}

	public static int getCode(String json) throws Exception {

		JSONObject jsonObject = new JSONObject(json);
		int code = Integer.parseInt(jsonObject.getString("code"));
		return code;
	}

	public static Map<String, Object> getResits(String json)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<Resit> resits = null;

		JSONObject jsonObject = new JSONObject(json);
		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		resits = new ArrayList<Resit>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			resits.add(new Resit(encode(object.getString("name")),
					encode(object.getString("time")), encode(object
							.getString("place")), encode(object
							.getString("seat"))));
		}

		info.put("message", message);
		info.put("data", resits);
		return info;
	}

	public static Map<String, Object> getResits(JSONObject jsonObject)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<Resit> resits = null;

		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		resits = new ArrayList<Resit>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			resits.add(new Resit(encode(object.getString("name")),
					encode(object.getString("time")), encode(object
							.getString("place")), encode(object
							.getString("seat"))));
		}

		info.put("message", message);
		info.put("data", resits);
		return info;
	}

	public static Map<String, Object> getLevels(JSONObject jsonObject)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<Level> scores = null;

		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		scores = new ArrayList<Level>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			scores.add(new Level(encode(object.getString("name")),
					encode(object.getString("identify")), encode(object
							.getString("time")), encode(object
							.getString("sumScore")), encode(object
							.getString("listenScore")), encode(object
							.getString("readScore")), encode(object
							.getString("writeScore")), encode(object
							.getString("integrateScore"))));
		}

		/*
		 * String message = "获取成功"; resits = new ArrayList<Resit>();
		 * resits.add(new Resit("大学英语(3)","2014年9月13日(08:00-10:00)", "SC216",
		 * "36")); resits.add(new Resit("大学英语(4)","2014年9月13日(08:00-10:00)",
		 * "SC216", "3"));
		 */
		info.put("message", message);
		info.put("data", scores);
		return info;
	}

	public static Map<String, Object> getScoreUnpassOrHighest(
			JSONObject jsonObject) throws JSONException,
			UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<Score> levels = null;

		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		levels = new ArrayList<Score>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			levels.add(new Score(encode(object.getString("name")),
					encode(object.getString("property")), encode(object
							.getString("credit")), encode(object
							.getString("highestScore"))));
		}
		info.put("message", message);
		info.put("data", levels);
		return info;
	}

	public static Map<String, Object> getScoreHistoryOrYearOrTerm(
			JSONObject jsonObject) throws JSONException,
			UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<Score> levels = null;

		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		levels = new ArrayList<Score>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			levels.add(new Score(encode(object.getString("name")),
					encode(object.getString("year")), encode(object
							.getString("term")), encode(object
							.getString("property")), encode(object
							.getString("credit")), encode(object
							.getString("point")), encode(object
							.getString("score")), encode(object
							.getString("reScore")), encode(object
							.getString("rebuildScore"))));
		}
		info.put("message", message);
		info.put("data", levels);
		return info;
	}

	public static Map<String, Object> getRollCAllHis(JSONObject jsonObject)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<RollCallHis> his = null;

		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		his = new ArrayList<RollCallHis>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			his.add(new RollCallHis(object.getString("date"), object
					.getString("status")));
		}

		info.put("message", message);
		info.put("data", his);
		return info;
	}

	public static Map<String, Object> getCet46(JSONObject jsonObject)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<Cet46> cet46s = null;

		String message = encode(jsonObject.getString("message"));
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");
		JSONObject object = null;
		cet46s = new ArrayList<Cet46>();
		for (int i = 0; i < jsonArray.length(); i++) {
			object = (JSONObject) jsonArray.get(i);
			cet46s.add(new Cet46(encode(object.getString("name")),
					encode(object.getString("school")), encode(object
							.getString("item")),
					encode(object.getString("id")), encode(object
							.getString("time")), encode(object
							.getString("score")), encode(object
							.getString("listening")), encode(object
							.getString("reading")), encode(object
							.getString("writing"))));
		}

		info.put("message", message);
		info.put("data", cet46s);
		return info;
	}

	public static Map<String, Object> getRollCallMenber(JSONObject jsonObject)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<RollCallMenber> menbers = null;

		String message = encode(jsonObject.getString("message"));
		JSONObject dataObject = (JSONObject) jsonObject.get("data");
		String date = dataObject.getString("date");
		JSONObject persons = (JSONObject) dataObject.get("person");
		menbers = new ArrayList<RollCallMenber>();
		JSONArray names = persons.names();
		for (int i = 0; i < persons.length(); i++) {
			String id = (String) names.get(i);
			String name = persons.getString(id);
			menbers.add(new RollCallMenber(id, name));
		}

		info.put("message", message);
		info.put("data", menbers);
		info.put("date", date);
		return info;
	}

	public static Map<String, Object> getCourses(JSONObject jsonObject)
			throws JSONException, UnsupportedEncodingException {
		Map<String, Object> info = new HashMap<String, Object>();
		List<Course> courses = null;

		String message = encode(jsonObject.getString("message"));
		JSONObject jsonDataObject = (JSONObject) jsonObject.get("data");
		// JSONObject object = null;
		JSONArray jsonArray = null;
		courses = new ArrayList<Course>();
		for (int i = 1; i <= 7; i++) {
			jsonArray = (JSONArray) jsonDataObject.get(i + "");
			for (int j = 0; j < jsonArray.length(); j++) {
				String data = (String) jsonArray.get(j);
				if (!"".equals(data)) {
					courses.add(getString2Course(data));
				}
			}

		}

		info.put("message", message);
		info.put("data", courses);
		return info;
	}

	public static String encode(String message)
			throws UnsupportedEncodingException {
		byte[] bytes = message.getBytes("UTF-8");
		return new String(bytes, "UTF-8");
	}

	private static Course getString2Course(String s) {
		String weeks = "一二三四五六日";
		String num = "0123456789";

		int wk = s.indexOf("周");
		int index = weeks.indexOf(s.charAt(wk + 1));
		while (-1 == index) {
			wk = s.indexOf("周", wk + 1);
			index = weeks.indexOf(s.charAt(wk + 1));
		}
		String courseName = s.substring(0, wk);
		String week = s.substring(wk + 1, wk + 2);
		// System.out.println("className==" + courseName);
		// System.out.println("week==" + week);

		int start, end;
		char tmp = s.charAt(wk + 2 + 1);

		end = start = wk + 2 + 1;
		while (tmp <= '9' && tmp >= '0' || tmp == ',') {
			tmp = s.charAt(end++);
		}
		String jies = s.substring(start, end - 1);
		String[] split = jies.split(",");
		int[] jie = new int[split.length];
		int j = 0;
		for (String s1 : split) {
			jie[j] = Integer.parseInt(s1);
			j++;
			// System.out.println("jieshu==" + s1);
		}

		int zuo = Integer.parseInt((end - 1) + "");
		int you = s.indexOf("}", zuo + 1);
		String during = s.substring(zuo + 2, you);
		// System.out.println("longs==" + during);

		int i = you;
		while (s.charAt(i) < '0' || s.charAt(i) > '9') {
			i++;
		}
		String teacher, room;
		if (s.charAt(i - 2) <= 'z' && s.charAt(i - 2) >= 'a'
				|| s.charAt(i - 2) <= 'Z' && s.charAt(i - 2) >= 'A') {
			room = s.substring(i - 2, i + 3);
			teacher = s.substring(you + 1, i - 2);
		} else {
			room = s.substring(i - 1, i + 3);
			teacher = s.substring(you + 1, i - 1);
		}
		// System.out.println("teacher==" + teacher);
		// System.out.println("room==" + room);
		return new Course(courseName, getWeek2int(week), jie, during, teacher,
				room);
	}

	private static int getWeek2int(String week) {
		switch (week) {
		case "一":
			return 1;
		case "二":
			return 2;
		case "三":
			return 3;
		case "四":
			return 4;
		case "五":
			return 5;
		case "六":
			return 6;
		case "日":
			return 7;
		default:
			break;
		}
		return 7;
	}

}
