package com.project.jemberliburan.connection;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<NetworkResponse> {

    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;

    private Response.Listener<NetworkResponse> mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public VolleyMultipartRequest(String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            Map<String, String> params = getParams();
            if(params != null && params.size() > 0){
                textParse(bos, params, getParamsEncoding());
            }

            Map<String, DataPart> data = getByteData();
            if(data != null && data.size() > 0){
                dataParse(bos, data);
            }

            bos.write(("--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }

        return bos.toByteArray();
    }

    private void textParse(ByteArrayOutputStream bos, Map<String, String> params, String encoding) throws IOException {
        try {
            for(Map.Entry<String, String> entry : params.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();

                bos.write(("--" + boundary + "\r\n").getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n").getBytes());
                bos.write(("Content-Type: text/plain; charset=" + encoding + "\r\n\r\n").getBytes());
                bos.write((value + "\r\n").getBytes());
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported: " + encoding, e);
        }
    }

    private void dataParse(ByteArrayOutputStream bos, Map<String, DataPart> data) throws IOException {
        for(Map.Entry<String, DataPart> entry : data.entrySet()){
            String key = entry.getKey();
            DataPart dataPart = entry.getValue();

            bos.write(("--" + boundary + "\r\n").getBytes());
            bos.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + dataPart.getFileName() + "\"\r\n").getBytes());
            bos.write(("Content-Type: " + dataPart.getType() + "\r\n\r\n").getBytes());
            bos.write(dataPart.getContent());
            bos.write("\r\n".getBytes());
        }
    }

    protected abstract Map<String, DataPart> getByteData() throws AuthFailureError;

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    public void setHeaders(Map<String, String> headers){
        this.mHeaders = headers;
    }

    public class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart(){
        }

        public DataPart(String name, byte[] data) {
            fileName = name;
            content = data;
        }

        public DataPart(String name, byte[] data, String type) {
            fileName = name;
            content = data;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName){
            this.fileName = fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public void setContent(byte[] content){
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type){
            this.type = type;
        }
    }
}