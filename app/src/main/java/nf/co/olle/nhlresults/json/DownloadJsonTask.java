package nf.co.olle.nhlresults.json;

import android.os.AsyncTask;

import java.util.HashMap;

import nf.co.olle.nhlresults.io.HttpRequest;

public class DownloadJsonTask extends AsyncTask<String, Void, String> {

    private final JsonTaskCallback callback;

    public DownloadJsonTask(JsonTaskCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... urls) {
        HashMap<String, String> map=new HashMap<>();
        String response="";
        try {
            HttpRequest req = new HttpRequest(urls[0]);
            response = req.prepare(HttpRequest.Method.GET).sendAndReadString();
        } catch (Exception e) {
            response=e.getMessage();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        //setContentView(R.layout.main);
        // Displays the HTML string in the UI via a WebView
        //WebView myWebView = (WebView) findViewById(R.id.webview);
        //myWebView.loadData(result, "text/html", null);

        //TODO vlozeni vysledku do view
        //textView.setText(result);
        callback.onTaskCompleted(result);
    }
}
