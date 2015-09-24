package com.thestore.microstore.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
 
public class TheStoreHttpClient extends DefaultHttpClient { 
	
	public static final int SET_CONNECTION_TIMEOUT = 20 * 1000;
	public static final int SET_SOCKET_TIMEOUT = 20 * 1000;
	
    public TheStoreHttpClient() { 
        super(); 
        registerTrustAllScheme();
        setCredentials(); 
        HttpConnectionParams.setConnectionTimeout(this.getParams(), SET_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(this.getParams(), SET_SOCKET_TIMEOUT);
        HttpProtocolParams.setContentCharset(this.getParams(), HTTP.UTF_8);
    } 
    
    private void registerTrustAllScheme() {
    	Scheme httpSch = new Scheme("http", PlainSocketFactory.getSocketFactory(), 80);
    	getConnectionManager().getSchemeRegistry().register(httpSch); 
    	TrustAllSSLSocketFactory tasslf = null;
		try {
			tasslf = new TrustAllSSLSocketFactory();
		} catch (KeyManagementException e) {
		} catch (NoSuchAlgorithmException e) {
		} catch (KeyStoreException e) {
		} catch (UnrecoverableKeyException e) {
		} 
		if (tasslf != null) {
			Scheme httpSSch = new Scheme("https", tasslf, 443); 
			getConnectionManager().getSchemeRegistry().register(httpSSch);			
		}
    }
      
    private void setCredentials() {
        BasicCredentialsProvider cP = new BasicCredentialsProvider(); 
        // username password not needed
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("", "");
        cP.setCredentials(AuthScope.ANY, creds); 
        setCredentialsProvider(cP); 
    } 
    
    public static class TrustAllSSLSocketFactory extends SSLSocketFactory { 
        private javax.net.ssl.SSLSocketFactory factory; 

        public TrustAllSSLSocketFactory() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException { 
            super(null); 
            try { 
                SSLContext sslcontext = SSLContext.getInstance("TLS"); 
                sslcontext.init(null, new TrustManager[] { new TrustAllManager() }, null);
                factory = sslcontext.getSocketFactory();
                setHostnameVerifier(new AllowAllHostnameVerifier());             
            } catch(Exception ex) { } 
        } 
        
        public static SocketFactory getDefault() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException { return new TrustAllSSLSocketFactory(); } 
        public Socket createSocket() throws IOException { return factory.createSocket(); } 
        public Socket createSocket(Socket socket, String s, int i, boolean flag) throws IOException { return factory.createSocket(socket, s, i, flag); } 
        public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr1, int j) throws IOException { return factory.createSocket(inaddr, i, inaddr1, j); } 
        public Socket createSocket(InetAddress inaddr, int i) throws IOException { return factory.createSocket(inaddr, i); } 
        public Socket createSocket(String s, int i, InetAddress inaddr, int j) throws IOException { return factory.createSocket(s, i, inaddr, j); } 
        public Socket createSocket(String s, int i) throws IOException { return factory.createSocket(s, i); } 
        public String[] getDefaultCipherSuites() { return factory.getDefaultCipherSuites(); } 
        public String[] getSupportedCipherSuites() { return factory.getSupportedCipherSuites(); } 
    }
    
    public static class TrustAllManager implements X509TrustManager { 
        public void checkClientTrusted(X509Certificate[] cert, String authType) throws CertificateException { } 
        public void checkServerTrusted(X509Certificate[] cert, String authType) throws CertificateException { } 
        public X509Certificate[] getAcceptedIssuers() { return null; } 
    } 
}