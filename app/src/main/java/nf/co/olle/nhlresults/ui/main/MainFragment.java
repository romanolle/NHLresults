package nf.co.olle.nhlresults.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nf.co.olle.nhlresults.R;
import nf.co.olle.nhlresults.dialog.DatePickerFragment;
import nf.co.olle.nhlresults.json.DownloadJsonTask;
import nf.co.olle.nhlresults.json.InternetConnectionLost;
import nf.co.olle.nhlresults.json.JsonTaskCallback;
import nf.co.olle.nhlresults.json.parser.NhlJsonParser;
import nf.co.olle.nhlresults.model.Game;
import nf.co.olle.nhlresults.utils.Network;

public class MainFragment extends Fragment implements JsonTaskCallback {

    private MainViewModel mViewModel;
    private TextView textView;
    private static final NhlJsonParser parser = new NhlJsonParser();

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    //TODO smazat
    //private static final String URL = "https://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
    private static final String URL = "https://statsapi.web.nhl.com/api/v1/schedule";
    private TextView fromTextFiled;
    private TextView toTextFiled;

    private int fromYear;
    private int fromMonth;
    private int fromDay;

    private int toYear;
    private int toMonth;
    private int toDay;
    private Button reloadButton;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment, container, false);
        textView = root.findViewById(R.id.message);
        textView.setText("");

        Calendar cal = Calendar.getInstance();
        setToDate(cal);

        cal.setTimeInMillis(cal.getTimeInMillis() - (24 * 60 * 60 * 1000));
        setFromDate(cal);

        View fromButton = root.findViewById(R.id.fromButton);
        fromButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(fromDay, fromMonth, fromYear, fromDateListener);
            }
        });


        View toButton = root.findViewById(R.id.toButton);
        toButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(toDay, toMonth, toYear, toDateListener);
            }
        });


        fromTextFiled = root.findViewById(R.id.fromText);
        toTextFiled = root.findViewById(R.id.toText);

        setDateToTextField(fromTextFiled, fromDay, fromMonth, fromYear);
        setDateToTextField(toTextFiled, toDay, toMonth, toYear);

        reloadButton = root.findViewById(R.id.reloadButton);
        reloadButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reload();
                    }
                }
        );


        if(Network.isNetworkAvailable(root.getContext())) {
            reload();
        } else {
            noInternetAccess();
        }
        return root;
    }

    private void setToDate(Calendar cal) {
        toDay = cal.get(Calendar.DAY_OF_MONTH);
        toMonth = cal.get(Calendar.MONTH);
        toYear = cal.get(Calendar.YEAR);
    }

    private void setFromDate(Calendar cal) {
        fromDay = cal.get(Calendar.DAY_OF_MONTH);
        fromMonth = cal.get(Calendar.MONTH);
        fromYear = cal.get(Calendar.YEAR);
    }


    private void reload() {
        textView.setText(R.string.loading);
        reloadButton.setEnabled(false);
        StringBuilder url = new StringBuilder(URL);
        url.append("?")
                .append("startDate=")
                .append(fromYear).append("-").append(fromMonth+1).append("-").append(fromDay)
                .append("&")
                .append("endDate=")
                .append(toYear).append("-").append(toMonth+1).append("-").append(toDay);
        //?startDate=2018-01-09 Start date for the search
        //?endDate=2018-01-12 End date for the search
        new DownloadJsonTask(this).execute(url.toString());
    }



    private void noInternetAccess() {
        textView.setText(R.string.connection_lost);
        reloadButton.setEnabled(true);
    }

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


    @Override
    public void onTaskCompleted(String response) {
        List<Game> games = null;
        try {
            games = parser.parse(response);
        } catch (InternetConnectionLost internetConnectionLost) {
            noInternetAccess();
            return;
        }
        StringBuilder builder = new StringBuilder(games.isEmpty() ? "No games for these days" : "");

        for(Game game : games) {


            /*builder.append(game.getStartGameTime());
            builder.append(" - ");
            builder.append(new Date(game.getStartGameTime()));
            builder.append(" - ");
            builder.append(ZonedDateTime.ofInstant(Instant.ofEpochSecond(game.getStartGameTime()), ZoneId.systemDefault()));
            builder.append(" - ");
            */
            if(game.getStartGameTime() != null) {
                builder.append(dateFormatter.format(game.getStartGameTime().withZoneSameInstant(ZoneId.systemDefault())))
                        .append(" - ");
            }
            builder.append(game.getHomeTeam().getName());
            builder.append(" vs ");
            builder.append(game.getAwayTeam().getName());
            builder.append(" ");
            builder.append((game.getHomeScore() + game.getAwayScore()));
            builder.append(" goals");
            builder.append("\n\r");
            builder.append("\n\r");
        }

        textView.setText(builder.toString());
        reloadButton.setEnabled(true);
    }

    private void showDatePicker(int day, int month, int year, DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment date = new DatePickerFragment();

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        date.setArguments(args);

        date.setCallBack(listener);
        date.show(getFragmentManager(), "Date Picker");
    }

    private void setDateToTextField(TextView textField, int dayOfMonth, int monthOfYear, int year) {
        textField.setText(String.valueOf(dayOfMonth) + "." + String.valueOf(monthOfYear+1)
                + "." + String.valueOf(year));
    }

    private final DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fromDay = dayOfMonth;
            fromMonth = monthOfYear;
            fromYear = year;
            setDateToTextField(fromTextFiled, dayOfMonth, monthOfYear, year);
        }
    };

    private final DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            toDay = dayOfMonth;
            toMonth = monthOfYear;
            toYear = year;
            setDateToTextField(toTextFiled, dayOfMonth, monthOfYear, year);
        }
    };

}
