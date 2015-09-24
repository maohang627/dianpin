package com.thestore.microstore.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.HttpResponse;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.yihaodian.mobile.vo.bussiness.Trader;

/**
 * 
 * @author xiaojiaying
 * 
 */
public class DBHelper {
	private static final String TAG = "DBHelper";// 不使用类名，避免logtag被混淆
	private static final String JSON_FORMAT = "Json";
	private static final String XML_FORMAT = "Xml";

	private static final XStream xStream = new XStream(new DomDriver());
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(
			Date.class, new DateDeserializer()).create();

	private static final Trader trader = new Trader();

	private static class DateDeserializer implements JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			// Gson gson = new GsonBuilder().create();
			if (!TextUtils.isDigitsOnly(json.getAsString())) {
				// for eg: 2013-10-15 00:00:00 以后如果返回Date类型需要以这种格式
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateStr = json.getAsString();
				try {
					return format.parse(dateStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return null/* gson.fromJson(json, Date.class) */;
			} else {
				return new Date(json.getAsLong());
			}
		}
	}

	private DBHelper() {
	}

	public static XStream getxStream() {
		return xStream;
	}

	public static Gson getGson() {
		return gson;
	}

}
