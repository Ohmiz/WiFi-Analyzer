package kr.co.generic.wifianalyzer;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UserMonitor extends AsyncTask<Void, Void, Void> {

	private String reg_nm = "";
	private String total_cnt = "";
	private String known_cnt = "";
	private boolean bActive = false;
	
	public void init(String reg_nm, String total_cnt, String known_cnt, boolean bActive) {
		this.reg_nm = reg_nm;
		this.total_cnt = total_cnt;
		this.known_cnt = known_cnt;
		this.bActive = bActive;
		
	}

	@Override
	protected Void doInBackground(Void... params) {
		HttpURLConnection httpcon = null;
		boolean sendOK = false;

		try {
			httpcon = (HttpURLConnection) ((new URL(
					HoonProperty.UserMonitorUrl).openConnection()));
			httpcon.setDoInput(true);
			httpcon.setUseCaches(false);
			httpcon.setDoOutput(true);
			httpcon.setRequestMethod("POST");


			httpcon.setRequestProperty("deviceUuid", HoonProperty.DevicesUUID);

			String appNm = HoonProperty.AppNM;
			
			if (appNm.length() > 0)
				appNm = URLEncoder.encode(HoonProperty.AppNM, "UTF-8");
			httpcon.setRequestProperty("appNm", appNm);

			if (reg_nm.length() > 0)
				reg_nm = URLEncoder.encode(reg_nm, "UTF-8");
			httpcon.setRequestProperty("regNm", reg_nm);
			
			if (total_cnt.length() > 0)
				total_cnt = URLEncoder.encode(total_cnt, "UTF-8");
			httpcon.setRequestProperty("totalCnt", total_cnt);
			
			if (known_cnt.length() > 0)
				known_cnt = URLEncoder.encode(known_cnt, "UTF-8");
			httpcon.setRequestProperty("knownCnt", known_cnt);

			if(bActive)
			{
				httpcon.setRequestProperty("bActive", "Y");
			}else 
			{
				httpcon.setRequestProperty("bActive", "N");
			}
			

			httpcon.connect();
			sendOK = getSendResult(httpcon);

			httpcon.disconnect();
			if (httpcon != null)
				httpcon.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpcon != null)
					httpcon.disconnect();
			} catch (Exception e) {
			}
		}
		return null;
	}

	protected void onProgressUpdate(Void... progress) {
		// setProgressPercent(progress[0]);
	}

	protected void onPostExecute(Void... result) {
		// showDialog("Downloaded " + result + " bytes");
	}

	private class PhotoUploadResponseHandler implements ResponseHandler<Object> {

		@Override
		public Object handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {

			HttpEntity r_entity = response.getEntity();
			String responseString = EntityUtils.toString(r_entity);
			Log.d("UPLOAD", responseString);

			return null;
		}

	}

	private boolean getSendResult(HttpURLConnection _httpcon)
			throws IOException {
		boolean isOK = false;
		int n = 0; // n=0 has no key, and the HTTP return status in the value
					// field
		String headerKey;
		String headerVal;
		HttpURLConnection httpcon = _httpcon;

		try {
			while (true) {
				isOK = false;
				headerKey = httpcon.getHeaderFieldKey(n);
				headerVal = httpcon.getHeaderField(n);

				if (headerKey != null || headerVal != null) {
					// System.out.println("[getSendResult] headerKey="+headerKey+", headerVal="+headerVal);
					if ("result".equals(headerKey)) {
						if (httpcon.getHeaderField("result").equals("Y")) {
							// System.out.println("[getSendResult] Servlet Result = "+httpcon.getHeaderField(mdtiConstant.wRESULT));
							isOK = true;
						} else {
							isOK = false;
						}
						break;
					}
					n++;
				} else
					break;
			} // while
		} catch (Exception e) {
			isOK = false;
		}
		return isOK;
	} // getSendResult
}
