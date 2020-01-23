package nf.co.olle.nhlresults.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import nf.co.olle.nhlresults.R;
import nf.co.olle.nhlresults.io.HttpRequest;
import nf.co.olle.nhlresults.json.parser.NhlJsonParser;
import nf.co.olle.nhlresults.model.Game;
import nf.co.olle.nhlresults.xml.parser.NhlXmlParser;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private TextView textView;
    private static final NhlJsonParser parser = new NhlJsonParser();

    //TODO smazat
    //private static final String URL = "https://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
    private static final String URL = "https://statsapi.web.nhl.com/api/v1/schedule";


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment, container, false);
        textView = root.findViewById(R.id.message);
        textView.setText("Loading results");

        if(wifiConnected() || mobileDataConnected()) {
            new DownloadJsonTask().execute(URL);
        }
        return root;
    }


    /*
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    */


    private boolean mobileDataConnected() {
        //TODO dodelat mobileDataConnected
        return true;
    }

    private boolean wifiConnected() {
        //TODO dodelat wifiConnected
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    public class DownloadJsonTask extends AsyncTask<String, Void, String> {

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
            onTaskCompleted(result);
        }
    }

    public void onTaskCompleted(String response) {
        List<Game> games = parser.parse(response);
        StringBuilder builder = new StringBuilder(games.isEmpty() ? "No games for these days" : "");

        for(Game game : games) {
            builder.append(game.getHomeTeam().getName());
            builder.append(" vs ");
            builder.append(game.getAwayTeam().getName());
            builder.append(" - GOALS = ");
            builder.append((game.getHomeScore() + game.getAwayScore()));
            builder.append("\n\r");
            builder.append("\n\r");
        }

        textView.setText(builder.toString());
    }



    // Uploads XML from stackoverflow.com, parses it, and combines it with
// HTML markup. Returns HTML string.
    private String loadJsonFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        NhlXmlParser nhlXmlParser = new NhlXmlParser();
        List<NhlXmlParser.Entry> entries = null;
        String title = null;
        String url = null;
        String summary = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        // Checks whether the user set the preference to include summary text
        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //boolean pref = sharedPrefs.getBoolean("summaryPref", false);

        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
        htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
                formatter.format(rightNow.getTime()) + "</em>\n\r\n\r\n\r");

        try {
            stream = downloadUrl(urlString);
            entries = nhlXmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (NhlXmlParser.Entry entry : entries) {


            /*
            *
            * UDELAT VIEW
            *
            */



            htmlString.append("<p>\n\r\t<a href='");
            htmlString.append(entry.link);
            htmlString.append("'>" + entry.title + "</a></p>\n\r");
            // If the user set the preference to include summary text,
            // adds it to the display.
            //if (pref) {
             //   htmlString.append(entry.summary);
            //}
        }
        return htmlString.toString();
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

}
