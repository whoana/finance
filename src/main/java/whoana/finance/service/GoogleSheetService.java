package whoana.finance.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import whoana.finance.util.Util;





@Service
public class GoogleSheetService {

	
	
	

	/**
     * OAUTH 2.0 연동시 지정한 OAuth 2.0 클라이언트 이름
     */
    private static final String APPLICATION_NAME =
            "Google Sheets API";
    		//"Google Sheets API Java Quickstart";

    /**
     * OAUTH 2.0 연동시 credential을 디스크에 저장할 위치
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            //System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");
    		System.getProperty("user.home"), ".credentials/sheets.googleapis.com-whoana-finance");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Google Sheet API 권한을 SCOPE로 지정
     */
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);
    //private static final List<String> SCOPES = Arrays.asList(SheetsScopes.DRIVE);

    /**
     * HTTP_TRANSPORT, DATA_STORE_FACTORY 초기화
     */
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * OAUTH 2.0 연동시 사용될 callback용 local receiver 포트 지정
     */
    private static final int LOCAL_SERVER_RECEIVER_PORT = 18080;

    /**
     * 인증 모드 2개
     */
    private enum AuthMode {
        OAUTH20, SERVICE_ACCOUNT
    }

    /**
     * OAUTH 2.0용 credential 생성
     *
     * @return Credential object.
     * @throws IOException
     */
    public static Credential getOauth2Authorize() throws IOException {
        // OAUTH 2.0용 secret josn 로드
        InputStream in =
        		GoogleSheetService.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(LOCAL_SERVER_RECEIVER_PORT).build();

        Credential credential = new AuthorizationCodeInstalledApp(
                flow, receiver).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Service Account용 credentail 생성
     * @return Credential object.
     * @throws IOException
     */
    public static Credential getServiceAccountAuthorize() throws IOException {

        //InputStream in = GoogleSheetAPI.class.getResourceAsStream("/service.json");
    	InputStream in = GoogleSheetService.class.getResourceAsStream("/service_secret.json");
    	
        Credential credential = GoogleCredential.fromStream(in).createScoped(SCOPES);
        //AIzaSyDo2HhX8RjbRFHcIz0Wx9Ed_oBHEr2Gjz8
        return credential;
    }

    /**
     * Google Credential 정보를 가지고 Google Sheet서비스를 초기화 한다.
     *
     * @return 인증이 통과된 Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService(AuthMode authMode) throws IOException {
        Credential credential = null;
        if (authMode == AuthMode.OAUTH20) {
            credential = getOauth2Authorize();
        } else if (authMode == AuthMode.SERVICE_ACCOUNT) {
            credential = getServiceAccountAuthorize();
        }
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    
    // 아래의 샘플 구글 시트 URL에서 중간의 문자열이 spreed sheet id에 해당한다.
    // https://docs.google.com/spreadsheets/d/1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic/edit
    //https://docs.google.com/spreadsheets/d/1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic/edit#gid=242157832
    //String range = "A2:E43";
    
    String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";
    public List<List<Object>> getData(String spreadsheetId, String range) throws Exception {
        // 기호에 따라 OAUTH2.0용 인증이나 서비스 계정으로 인증을 수행 후 Sheet Service 객체를 불러온다.
        Sheets service = getSheetsService(AuthMode.OAUTH20); 
        
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        
        List<List<Object>> values = response.getValues();
        return values; 
    }
    
    public void toSheet(String spreadsheetId, String range, List<List<Object>> data) throws IOException {
    	Sheets service = getSheetsService(AuthMode.OAUTH20); 
    	
		   ValueRange body = new ValueRange()
				      .setValues(data);
				    UpdateValuesResponse result = service.spreadsheets().values()
				      .update(spreadsheetId, range, body)
				      .setValueInputOption("RAW")
				      .execute();
   }
    
    public Map<String, Object> getData() throws Exception {
        // 기호에 따라 OAUTH2.0용 인증이나 서비스 계정으로 인증을 수행 후 Sheet Service 객체를 불러온다.
        Sheets service = getSheetsService(AuthMode.OAUTH20);
        //Sheets service = getSheetsService(AuthMode.SERVICE_ACCOUNT);

        // 아래의 샘플 구글 시트 URL에서 중간의 문자열이 spreed sheet id에 해당한다.
        // https://docs.google.com/spreadsheets/d/1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic/edit
        //https://docs.google.com/spreadsheets/d/1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic/edit#gid=242157832
        String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";

        String range = "A2:E43";
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
            return null;
        } else {
        	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (List row : values) {
                if (row.size() > 0) {
                	int i = 1;
                	System.out.println("row:" + Util.toJSONPrettyString(row));
                	Map<String, String> record = new HashMap<String, String>();
                	for(Object col : row) {
                		String key = null;
                		if(i == 1) {
                			key = "name";
                		} else if(i == 2) {
                			key = "symbol";
                		} else if(i == 3) {
                			key = "price";
                		} else if(i == 4) {
                			key = "qty";
                		} else if(i == 5) {
                			key = "amt";
                		}
                		
                		if(key != null) record.put(key, col.toString());
                		
                		System.out.print(", {key:"+key + ", value:" + col.toString() + "}");
                		i ++;
                	}
                	if(record.size() > 0) data.add(record);
                }
                System.out.println();
            }
            
            
            Map<String, Object> datas = new HashMap<String, Object>();
            datas.put("data", data);
            return datas;
        }
    }
    
    public static void main(String[] args) throws IOException {
        // 기호에 따라 OAUTH2.0용 인증이나 서비스 계정으로 인증을 수행 후 Sheet Service 객체를 불러온다.
        Sheets service = getSheetsService(AuthMode.OAUTH20);
        //Sheets service = getSheetsService(AuthMode.SERVICE_ACCOUNT);

        // 아래의 샘플 구글 시트 URL에서 중간의 문자열이 spreed sheet id에 해당한다.
        // https://docs.google.com/spreadsheets/d/1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic/edit
        //https://docs.google.com/spreadsheets/d/1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic/edit#gid=242157832
        String spreadsheetId = "1a92otuuxjHw_vCoowXYnKpJjaZTABHD7aEml7mIvuic";

        String range = "A2:D43";
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.size() == 0) {
            System.out.println("No data found.");
        } else {
        	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (List row : values) {
                if (row.size() > 0) {
                	int i = 1;
                	Map<String, String> record = new HashMap<String, String>();
                	for(Object col : row) {
                		String key = null;
                		if(i == 1) {
                			key = "name";
                		} else if(i == 2) {
                			key = "symbol";
                		} else if(i == 3) {
                			key = "price";
                		} else if(i == 4) {
                			key = "qty";
                		} else if(i == 5) {
                			key = "amt";
                		}
                		
                		if(key != null) record.put(key, col.toString());
                		
                		System.out.print(", {key:"+key + ", value:" + col.toString() + "}");
                		i ++;
                	}
                	if(record.size() > 0) data.add(record);
                }
                System.out.println();
            }
            
            
//            Map<String, Object> datas = new HashMap<String, Object>();
//            datas.put("data", data);
//            
//            RestTemplate client = new RestTemplate();
//            List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//    		messageConverters.add(new MappingJackson2HttpMessageConverter());
//    		client.setMessageConverters(messageConverters);
//    		String url = "http://127.0.0.1:8080/portfolios/upload";
//    		Map res = client.postForObject(url, datas, Map.class);
//    		
//    		System.out.println("res:" + res);
    		
        }
    }
    
}
