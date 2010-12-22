/**
 * 
 */
package nl.coralic.android.utils.http;

import java.io.IOException;
import java.net.SocketTimeoutException;

import nl.coralic.android.utils.constants.Const;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.coralic.utils.R;

import android.content.Context;
import android.util.Log;

/**
 * @author "Armin Čoralić"
 */
public class HttpHandler
{
	public String send(String URL, Context context)
	{
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 28000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 28000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpGet httpget = new HttpGet(URL);
		Log.d(Const.TAG_HTTP, "URI: " + httpget.getURI());
		String responseBody = "";

		try
		{
			HttpResponse response = httpclient.execute(httpget);
			if(response.getStatusLine().getStatusCode() == 200)
			{
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				responseBody = responseHandler.handleResponse(response);
				
			}
			else
			{
				responseBody = context.getString(R.string.ERR_CODE)+ response.getStatusLine().getStatusCode();
			}
		}
		catch (SocketTimeoutException se)
		{
			responseBody = context.getString(R.string.ERR_TIMEOUT);
			se.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			responseBody = context.getString(R.string.ERR_PROT);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			responseBody = context.getString(R.string.ERR_IO);
			e.printStackTrace();
		}
		
		httpclient.getConnectionManager().shutdown();
		return responseBody;
	}
}
