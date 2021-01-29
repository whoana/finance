package whoana.finance.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.HexDump;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;



//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.util.Comparators;
//import org.codehaus.jackson.type.TypeReference;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author athanatoy
 *
 */
public class Util {

	public static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;

	public static boolean isEmpty(String value){
		return StringUtils.isEmpty(value);
	}

	public static boolean isEmpty(Object value){
		if(value == null) return true;
		if(value instanceof String){
			return StringUtils.isEmpty(ObjectUtils.toString(value));
		}else{
			return false;
		}
	}

	public static boolean isEmpty(byte [] array){
		return ArrayUtils.isEmpty(array);
	}

	public static Integer toInteger(String s){
		return Integer.parseInt(s);
	}

	public static Short toShort(String s){
		return Short.parseShort(s);
	}

	public static Long toLong(String s){
		return Long.parseLong(s);
	}

	public static Float toFloat(String s){
		return Float.parseFloat(s);
	}

	public static Double toDouble(String s){
		return Double.parseDouble(s);
	}

	public static Boolean toBoolean(String s){
		return Boolean.parseBoolean(s);
	}

	public static String trim(String s){
		return StringUtils.trim(s);
	}

	public static String toString(byte [] bytes, String charsetName) throws UnsupportedEncodingException{
		return StringUtils.toString(bytes, charsetName);
	}

	public static String toString(byte [] bytes) throws UnsupportedEncodingException{
		return StringUtils.toString(bytes, null);
	}

	public static String toString(Object obj) {
		return ObjectUtils.toString(obj);
	}

	public static String leftPad(String str, int size, String padStr){
		int len = str.getBytes().length;
		int padSize = size - len;
		String res = StringUtils.join(StringUtils.repeat(padStr, padSize),str);
		return res;//StringUtils.leftPad(str, size, padStr);
	}
	public static final String DEFAULT_YMD_FORMAT = "yyyyMMdd";
	public static final String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";
	public static final String DEFAULT_DATE_FORMAT_MI = "yyyyMMddHHmmssSSS";
	public static String getFormatedDate(long timeMillis, String pattern){
		return DateFormatUtils.format(timeMillis, pattern);
	}

	public static String getFormatedDate(String pattern){
		long millis = System.currentTimeMillis();
		return DateFormatUtils.format(millis, pattern);
	}

	public static String getFormatedDate(){
		return getFormatedDate(DEFAULT_DATE_FORMAT);
	}

	public static String rightPad(String str, int size, String padStr){
		int len = str.getBytes().length;
		int padSize = size - len;
		String res = StringUtils.join(str, StringUtils.repeat(padStr, padSize));
		return res;//StringUtils.rightPad(str, size, padStr);
	}

	public static String repeat(String str, int repeat){
		return StringUtils.repeat(str, repeat);
	}

	public static String[] split(String str, String separatorChars){
		return StringUtils.split(str, separatorChars);
	}

	public static String join(String...elements) {
		// TODO Auto-generated method stub
		return StringUtils.join(elements);
	}

	public static String join(Object...elements) {
		// TODO Auto-generated method stub
		return StringUtils.join(elements);
	}

	public static String hexdump(byte[] data){
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			HexDump.dump(data, 0, outputStream, 0);
			return outputStream.toString();
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "hexdump fail";
	}

	public static String hexdump(byte[] data, String charset){
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			HexDump.dump(data, 0, outputStream, 0);
			return outputStream.toString(charset);
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "hexdump fail";
	}

	public static String hexdump2(byte[] data){
		try{
			StringBuffer sb = new StringBuffer();

			ByteBuffer buffer =  ByteBuffer.allocate(data.length);
			buffer.put(data);
			buffer.flip();
			while(buffer.hasRemaining()){
				byte[] dst = null;
				int remainingVal = buffer.remaining();
				if(remainingVal >= 16){
					dst = new byte[16];
					buffer.get(dst);
					for(byte b : dst){
						sb.append(StringUtils.remove(StringUtils.upperCase(Integer.toHexString(b)),"FFFFFF") + " ");
					}

					for(byte b : dst) sb.append((char)b);
				}else{
					dst = new byte[remainingVal];
					buffer.get(dst);
					for(byte b : dst){
						sb.append(StringUtils.remove(StringUtils.upperCase(Integer.toHexString(b)),"FFFFFF") + " ");
					}
					sb.append(repeat("   ",(16 - remainingVal)));

					for(byte b : dst) sb.append((char)b);
					sb.append(repeat(" ",(16 - remainingVal)));
				}


//				sb.append(StringUtils.toString(dst,null)).append("\n");

				sb.append("\n");

			}

			return sb.toString();
		}catch(Exception e){
			return "hexdump fail";
		}
	}

	public static String substring(String str, int start, int end){
		return StringUtils.substring(str, start, end);
	}

	public static String substring(String str, int start){
		return StringUtils.substring(str, start);
	}

	public static String capitalize(String str){
		return StringUtils.capitalize(str);
	}

	public static Object invokeMethod(Object object, String methodName, Object...args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		return MethodUtils.invokeMethod(object, methodName, args);
	}


	public static ObjectMapper jsonMapper = new ObjectMapper();

	public static String toJSONString(Object obj){
		try{
			return jsonMapper.writeValueAsString(obj);
		}catch(Exception e){
			e.printStackTrace();
			return "undefined json string";
		}
	}

	public static String toJSONPrettyString(Object obj){
		try{
			return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		}catch(Exception e){
			e.printStackTrace();
			return "undefined json string";
		}
	}



	public static Map<String, Object> jsonToMap(String json){
		Map<String, Object> res = null;
		try{
			res = jsonMapper.readValue(json, Map.class);
			return res;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static Object jsonToObject(String json){
		Object res = null;
		try{
			res = jsonMapper.readValue(json, Object.class);
			return res;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static <T> T jsonToObject(String json, TypeReference<T> type) throws JsonParseException, JsonMappingException, IOException{
		T res = jsonMapper.readValue(json, type);
		return res;
	}



	public static <T> T jsonToObject(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{
		T res = jsonMapper.readValue(json, clazz);
		return res;
	}

	public static <T> T jsonToObject(byte[] json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{
		T res = jsonMapper.readValue(json, clazz);
		return res;
	}

	public static boolean isNumber(String str){
		return NumberUtils.isNumber(str);
	}


	public static byte[] short2Bytes(short value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Short.SIZE / 8);
		buff.order(order);
		buff.putShort(value);
		return buff.array();
	}

	public static short bytes2Short(byte[] value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Short.SIZE/8);
		buff.order(order);
		buff.put(value);
		buff.flip();
		return buff.getShort();
	}


	public static byte[] int2Bytes(int value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
		buff.order(order);
		buff.putInt(value);
		return buff.array();
	}

	public static int bytes2Int(byte[] value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE/8);
		buff.order(order);
		buff.put(value);
		buff.flip();
		return buff.getInt();
	}

	public static byte[] long2Bytes(long value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Long.SIZE/8);
		buff.order(order);
		buff.putLong(value);
		return buff.array();
	}

	public static long bytes2Long(byte[] value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Long.SIZE/8);
		buff.order(order);
		buff.put(value);
		buff.flip();
		return buff.getLong();
	}

	public static byte[] float2Bytes(float value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Float.SIZE/8);
		buff.order(order);
		buff.putFloat(value);
		return buff.array();
	}

	public static float bytes2Float(byte[] value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Float.SIZE/8);
		buff.order(order);
		buff.put(value);
		buff.flip();
		return buff.getFloat();
	}

	public static byte[] double2Bytes(double value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Double.SIZE/8);
		buff.order(order);
		buff.putDouble(value);
		return buff.array();
	}

	public static double bytes2Double(byte[] value, ByteOrder order) {
		ByteBuffer buff = ByteBuffer.allocate(Double.SIZE/8);
		buff.order(order);
		buff.put(value);
		buff.flip();
		return buff.getDouble();
	}


	public static Field  getDeclaredField(Class<?> cls, String fieldName, boolean forceAccess){
		return FieldUtils.getDeclaredField(cls, fieldName, forceAccess);
	}

	public static Field  getField(Class<?> cls, String fieldName, boolean forceAccess){
		return FieldUtils.getField(cls, fieldName, forceAccess);
	}


	public static void writeField(Field field,
            Object target,
            Object value,
            boolean forceAccess)
     throws IllegalAccessException{
		FieldUtils.writeField(field, target, value, forceAccess);
	}


	public static String parsePropertyToString(Properties p, String key) throws Exception{
		String value = p.getProperty(key);
		if(Util.isEmpty(value))
			throw new Exception(Util.join("mint-error:parsePropertyToString:property(", key ,") value is null"));
		return value;
	}

	public static String parsePropertyToString(Properties p, String key, String defaultValue) throws Exception{
		String value = p.getProperty(key);
		if(Util.isEmpty(value)) {
			return defaultValue;
		}
		return value;
	}

	public static int parsePropertyToInteger(Properties p, String key) throws Exception{
		int value = 0;
		String valueString = parsePropertyToString(p, key);
		if(!Util.isNumber(valueString)){
			throw new Exception(
					Util.join("mint-error:parsePropertyToInteger:property(", key ,") value(",valueString,") must be number"));
		}

		try{
			value = Integer.parseInt(valueString);
		}catch(Exception e){
			throw new Exception(
					Util.join("mint-error:parsePropertyToInteger:property(", key ,") value(",valueString,") transformatting to number"), e);
		}
		return value;
	}

	public static Logger logger = LoggerFactory.getLogger(Util.class);

	public static int parsePropertyToInteger(Properties p,String key, int defaultValue) throws Exception{
		int value = 0;

		try{
			String valueString = parsePropertyToString(p, key);
			if(!Util.isNumber(valueString)){
				if(logger.isDebugEnabled()) logger.debug(Util.join("ListenerService property[",key,"] value[",valueString,"] is not number, so return defaultValue[",defaultValue, "]"));
				return defaultValue;
			}
			value = Integer.parseInt(valueString);
		}catch(Exception e){
//			if(logger.isDebugEnabled()) logger.debug(Util.join("Exception is occurred When getting ListenerService's property[",key,"], so return defaultValue[",defaultValue, "]"), e);
			if(logger.isDebugEnabled()) logger.debug(Util.join("Exception is occurred When getting ListenerService's property[",key,"], so return defaultValue[",defaultValue, "]"));
			return defaultValue;
		}
		return value;
	}

	public static long parsePropertyToLong(Properties p, String key) throws Exception{
		long value = 0;
		String valueString = parsePropertyToString(p, key);
		if(!Util.isNumber(valueString)){
			throw new Exception(
					Util.join("mint-error:parsePropertyToLong:property(", key ,") value(",valueString,") must be number"));
		}

		try{
			value = Long.parseLong(valueString);
		}catch(Exception e){
			throw new Exception(
					Util.join("mint-error:parsePropertyToLong:property(", key ,") value(",valueString,") transformatting to number"), e);
		}
		return value;
	}

	public static long parsePropertyToLong(Properties p,String key, long defaultValue) throws Exception{
		long value = 0;

		try{
			String valueString = parsePropertyToString(p, key);
			if(!Util.isNumber(valueString)){
				if(logger.isDebugEnabled()) logger.debug(Util.join("ListenerService property[",key,"] value[",valueString,"] is not number, so return defaultValue[",defaultValue, "]"));
				return defaultValue;
			}

			value = Long.parseLong(valueString);
		}catch(Exception e){
//			if(logger.isDebugEnabled()) logger.debug(Util.join("Exception is occurred When getting ListenerService's property[",key,"], so return defaultValue[",defaultValue, "]"), e);
			if(logger.isDebugEnabled()) logger.debug(Util.join("Exception is occurred When getting ListenerService's property[",key,"], so return defaultValue[",defaultValue, "]"));
			return defaultValue;
		}

		return value;
	}


	public static boolean parsePropertyToBoolean(Properties p, String key) throws Exception{
		boolean value = false;
		String valueString = parsePropertyToString(p, key);
		try{
			value = Util.toBoolean(valueString);
		}catch(Exception e){
			throw new Exception(
					Util.join("mint-error:parsePropertyToBoolean:property(", key ,") value(",valueString,") transformatting to boolean"), e);
		}

		return value;
	}

	public boolean parsePropertyToBoolean(Properties p, String key, boolean defaultValue) throws Exception{
		boolean value = false;
		try{
			String valueString = parsePropertyToString(p, key);

			value = Util.toBoolean(valueString);
		}catch(Exception e){
//			if(logger.isDebugEnabled()) logger.debug(Util.join("Exception is occurred When getting ListenerService's property[",key,"], so return defaultValue[",defaultValue, "]", e));
			if(logger.isDebugEnabled()) logger.debug(Util.join("Exception is occurred When getting ListenerService's property[",key,"], so return defaultValue[",defaultValue, "]"));
			return defaultValue;
		}

		return value;
	}

	/**
	 *
	 * @param dest
	 * @param clazz
	 * @param ccsid
	 * @return
	 * @throws Exception
	 */
	public static Object readObjectFromJson(File dest, Class clazz, String ccsid) throws Exception{

		FileInputStream fis = null;
		try{
			fis = new FileInputStream(dest);
			byte b[] = new byte[(int)dest.length()];
			fis.read(b);
			return jsonToObject(b, clazz);
		}finally{
			try{if(fis != null) fis.close();}catch(IOException e){}
		}
	}


	public static void logToBuffer(StringBuffer buffer, Object msg){
		buffer
		.append("[").append(Util.getFormatedDate("yyyyMMdd HH:mm:ss.SSS")).append("] ")
		.append(msg)
		.append(Util.LINE_SEPARATOR);
	}



	/**
	 * <blockquote>
	 * <pre>
	 * serializable 객체를 참조가 아닌 value copy할 필요가 있을 경우 사용한다.
 	 *
	 * <div style="background:#BDE12A">
	 * <B>[usage]</B>
	 *
	 * Message msg = ...;
	 * String fieldPath = "a.b.c";
	 * Element element = MessageUtil.getElementFirst(msg, fieldPath);//a[0].b[0].c[0]의 Element를 반환한다.
	 * Object value = element.getValue();//엘리먼트의 값을 얻어온다.
	 * Object copy = MessageUtil.copy(value);//엘리먼트의 원래 값을 레퍼런스하지 않고 새로운 값으로 복사한다.
	 * ...
	 * ...
	 * ...
	 * </pre>
	 * </blockquote>
	 * @param serializable copy할 대상 serializable 객체(Serializable 인터페이스를 implement한 객체여야한다.)
	 * @return copy된 결과 객체 값
	 * @throws Exception
	 */
	public static <T> T copy(Serializable serializable) throws Exception{
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(serializable);

			byte[] b = baos.toByteArray();
			ois = new ObjectInputStream(new ByteArrayInputStream(b));
			return (T) ois.readObject();
		}finally{
			try{if(oos != null) oos.close();}catch(IOException e){}
			try{if(ois != null) ois.close();}catch(IOException e){}
		}

	}

	/**
	 * <blockquote>
	 * <pre>
	 * serializable 객체를 참조가 아닌 value copy할 필요가 있을 경우 사용한다.
	 * 아파치의 SerializationUtils 라이브러를 사용한 함수이다.
 	 *
	 * <div style="background:#BDE12A">
	 * <B>[usage]</B>
	 *
	 * Message msg = ...;
	 * String fieldPath = "a.b.c";
	 * Element element = MessageUtil.getElementFirst(msg, fieldPath);//a[0].b[0].c[0]의 Element를 반환한다.
	 * Object value = element.getValue();//엘리먼트의 값을 얻어온다.
	 * Object copy = MessageUtil.copy2(value);//엘리먼트의 원래 값을 레퍼런스하지 않고 새로운 값으로 복사한다.
	 * ...
	 * ...
	 * ...
	 * </pre>
	 * </blockquote>
	 * @param serializable copy할 대상 serializable 객체(Serializable 인터페이스를 implement한 객체여야한다.)
	 * @return copy된 결과 객체 값
	 * @throws Exception
	 */
	public static <T> T  copy2(Serializable serializable)throws Exception{
		return (T) SerializationUtils.clone(serializable);
	}


	/**
	 * @param pattern : return 받을 date의 pattern
	 * @param date : 계산할 date
	 * @param addDate : 더하거나 빼줄 값
	 */
	public static String getDateAdd(String pattern, String date, int addDate) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		Date dates = null;

		try {
			dates = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(dates);
		cal.add(Calendar.DATE, addDate);

		return dateFormat.format(cal.getTime());
	}

	/**
	 * @param pattern : return 받을 date의 pattern
	 * @param date : 계산할 date
	 * @param addHour : 더하거나 빼줄 값
	 */
	public static String getHourAdd(String pattern, String date, int addHour) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		Date dates = null;
		Calendar cal = Calendar.getInstance();

		try {
			dates = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cal.setTime(dates);
		cal.add(Calendar.HOUR, addHour);

		return dateFormat.format(cal.getTime());
	}


	public static <T> List<String> compare(T origin, T target, String...fields) throws Exception{


		if(origin == null || target == null ) new Exception(Util.join("target or source is null "));
		List<String> changedFields = new ArrayList<String>();

		for (String fieldName : fields) {

			String methodName = Util.join("get", Util.substring(fieldName, 0, 1).toUpperCase(), Util.substring(fieldName, 1));

			Method originMethod = ReflectionUtils.findMethod(origin.getClass(), methodName);
			Method targetMethod = ReflectionUtils.findMethod(origin.getClass(), methodName);
			if (originMethod != null  && targetMethod != null) {
				Object originValue = ReflectionUtils.invokeMethod(originMethod, origin);
				Object targetValue = ReflectionUtils.invokeMethod(targetMethod, target);

				if (!Util.isEmpty(originValue) && !Util.isEmpty(targetValue) ) {
					if (!originValue.equals(targetValue)) {
						changedFields.add(fieldName);
					}
				}else if ((Util.isEmpty(originValue) && !Util.isEmpty(targetValue)) || (!Util.isEmpty(originValue) && Util.isEmpty(targetValue))) {
					changedFields.add(fieldName);
				}else{

				}
			}else{
				throw new Exception(Util.join("class ", origin.getClass()," has no get method for field ", fieldName, "!!!"));
			}

		}
		return changedFields;
	}

	public static int getLastDayOfMonth(){
		GregorianCalendar gc = new GregorianCalendar();
		int lastDayOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		return lastDayOfMonth;
	}

	public static int getLastDayOfMonth(int year, int month){
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, year);
		gc.set(Calendar.MONTH, month);
		int lastDayOfMonth = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
		return lastDayOfMonth;
	}

	public static String replaceString(String originString, String searchString, String replaceString){
		return StringUtils.replace(originString, searchString,  replaceString);
	}





	public static String convert(String str, String encoding) throws IOException {
		ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
		requestOutputStream.write(str.getBytes(encoding));
		return requestOutputStream.toString(encoding);
	}

	public static void unzip(File importFile, File outputFolder) throws IOException {


		byte[] buffer = new byte[1024];
		ZipInputStream zis = null;
		try {

			zis = new ZipInputStream(new FileInputStream(importFile));

			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {
				FileOutputStream fos = null;
				try{
					String fileName = ze.getName();
					File newFile = new File(outputFolder + File.separator + fileName);

					logger.debug("file unzip : " + newFile.getAbsoluteFile());

					// create all non exists folders
					// else you will hit FileNotFoundException for compressed folder
					new File(newFile.getParent()).mkdirs();

					fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}


					ze = zis.getNextEntry();
				}finally{
					if(fos != null) fos.close();
				}
			}



			System.out.println("Done");

		}finally{
			if(zis != null) {
				zis.closeEntry();
				zis.close();
			}
		}


	}

	public static void delete(File file) throws Exception{
		if(file.isDirectory()){

			File[] files = file.listFiles();
			for(int i = 0 ; i < files.length ; i++)
			delete(files[i]);

		}else{
			file.delete();
		}
	}

	public static <K,V> Map<K, V> sortMap(Map<K,V> unsortedMap) throws Exception{
		TreeMap<K, V> tm = new TreeMap<K,V>(unsortedMap);
		return tm;
	}



	public static long  getTimestamp(String time, String pattern) throws Exception{
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		Date parsedTimeStamp = dateFormat.parse(time);
		Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());

		return timestamp.getTime();
	}

}
