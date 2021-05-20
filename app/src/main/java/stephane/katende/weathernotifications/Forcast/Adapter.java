package stephane.katende.weathernotifications.Forcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import stephane.katende.weathernotifications.R;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context _myContext;
    String[] _headers, _row, _row1, _row2, _row3, _row4, _row5,
            _row6, _row7, _row8, _row9, _row10, _row11, _row12;
    ArrayList<ForecastViewHelper> _forecastViewHelpers;
    ForecastViewHelper _currentForecastViewHelper;


    public Adapter(Context context, ArrayList<ForecastViewHelper> forecastViewHelper) {
        _myContext = context;
        _forecastViewHelpers = forecastViewHelper;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(_myContext);
        View view = layoutInflater.inflate(R.layout.forcast_view2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //current helper
        _currentForecastViewHelper = _forecastViewHelpers.get(position);
        //setups
        _headers = _currentForecastViewHelper.getHeaders();
        _row = _currentForecastViewHelper.getRow();
        _row1 = _currentForecastViewHelper.getRow1();
        _row2 = _currentForecastViewHelper.getRow2();
        _row3 = _currentForecastViewHelper.getRow3();
        _row4 = _currentForecastViewHelper.getRow4();
        _row5 = _currentForecastViewHelper.getRow5();
        _row6 = _currentForecastViewHelper.getRow6();
        _row7 = _currentForecastViewHelper.getRow7();
        _row8 = _currentForecastViewHelper.getRow8();
        _row9 = _currentForecastViewHelper.getRow9();
        _row10 = _currentForecastViewHelper.getRow10();
        _row11 = _currentForecastViewHelper.getRow11();
        _row12 = _currentForecastViewHelper.getRow12();

        //headers
        holder.headers[0].setText(_headers[0]);
        holder.headers[1].setText(_headers[1]);
        holder.headers[2].setText(_headers[2]);

        //rows
        holder.row[0].setText(_row[0]);
        holder.row[1].setText(_row[1]);
        holder.row[2].setText(_row[2]);
        holder.row[3].setText(_row[3]);
        holder.row[4].setText(_row[4]);
        holder.row[5].setText(_row[5]);

        holder.row1[0].setText(_row1[0]);
        holder.row1[1].setText(_row1[1]);
        holder.row1[2].setText(_row1[2]);
        holder.row1[3].setText(_row1[3]);
        holder.row1[4].setText(_row1[4]);
        holder.row1[5].setText(_row1[5]);

        holder.row2[0].setText(_row2[0]);
        holder.row2[1].setText(_row2[1]);
        holder.row2[2].setText(_row2[2]);
        holder.row2[3].setText(_row2[3]);
        holder.row2[4].setText(_row2[4]);
        holder.row2[5].setText(_row2[5]);

        holder.row3[0].setText(_row3[0]);
        holder.row3[1].setText(_row3[1]);
        holder.row3[2].setText(_row3[2]);
        holder.row3[3].setText(_row3[3]);
        holder.row3[4].setText(_row3[4]);
        holder.row3[5].setText(_row3[5]);

        holder.row4[0].setText(_row4[0]);
        holder.row4[1].setText(_row4[1]);
        holder.row4[2].setText(_row4[2]);
        holder.row4[3].setText(_row4[3]);
        holder.row4[4].setText(_row4[4]);
        holder.row4[5].setText(_row4[5]);

        holder.row5[0].setText(_row5[0]);
        holder.row5[1].setText(_row5[1]);
        holder.row5[2].setText(_row5[2]);
        holder.row5[3].setText(_row5[3]);
        holder.row5[4].setText(_row5[4]);
        holder.row5[5].setText(_row5[5]);

        holder.row6[0].setText(_row6[0]);
        holder.row6[1].setText(_row6[1]);
        holder.row6[2].setText(_row6[2]);
        holder.row6[3].setText(_row6[3]);
        holder.row6[4].setText(_row6[4]);
        holder.row6[5].setText(_row6[5]);

        holder.row7[0].setText(_row7[0]);
        holder.row7[1].setText(_row7[1]);
        holder.row7[2].setText(_row7[2]);
        holder.row7[3].setText(_row7[3]);
        holder.row7[4].setText(_row7[4]);
        holder.row7[5].setText(_row7[5]);

        holder.row8[0].setText(_row8[0]);
        holder.row8[1].setText(_row8[1]);
        holder.row8[2].setText(_row8[2]);
        holder.row8[3].setText(_row8[3]);
        holder.row8[4].setText(_row8[4]);
        holder.row8[5].setText(_row8[5]);

        holder.row9[0].setText(_row9[0]);
        holder.row9[1].setText(_row9[1]);
        holder.row9[2].setText(_row9[2]);
        holder.row9[3].setText(_row9[3]);
        holder.row9[4].setText(_row9[4]);
        holder.row9[5].setText(_row9[5]);

        holder.row10[0].setText(_row10[0]);
        holder.row10[1].setText(_row10[1]);
        holder.row10[2].setText(_row10[2]);
        holder.row10[3].setText(_row10[3]);
        holder.row10[4].setText(_row10[4]);
        holder.row10[5].setText(_row10[5]);

        holder.row11[0].setText(_row11[0]);
        holder.row11[1].setText(_row11[1]);
        holder.row11[2].setText(_row11[2]);
        holder.row11[3].setText(_row11[3]);
        holder.row11[4].setText(_row11[4]);
        holder.row11[5].setText(_row11[5]);

        holder.row12[0].setText(_row12[0]);
        holder.row12[1].setText(_row12[1]);
        holder.row12[2].setText(_row12[2]);
        holder.row12[3].setText(_row12[3]);
        holder.row12[4].setText(_row12[4]);
        holder.row12[5].setText(_row12[5]);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //vars
        TextView[] headers = new TextView[3];
        TextView[] row = new TextView[6];
        TextView[] row1 = new TextView[6];
        TextView[] row2 = new TextView[6];
        TextView[] row3 = new TextView[6];
        TextView[] row4 = new TextView[6];
        TextView[] row5 = new TextView[6];
        TextView[] row6 = new TextView[6];
        TextView[] row7 = new TextView[6];
        TextView[] row8 = new TextView[6];
        TextView[] row9 = new TextView[6];
        TextView[] row10 = new TextView[6];
        TextView[] row11 = new TextView[6];
        TextView[] row12 = new TextView[6];

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headers[0] = itemView.findViewById(R.id._tvCurrentTemp);
            headers[1] = itemView.findViewById(R.id._tvDate);
            headers[2] = itemView.findViewById(R.id._tvHighLow);

            row[0] = itemView.findViewById(R.id._time);
            row[1] = itemView.findViewById(R.id._temp);
            row[2] = itemView.findViewById(R.id._feels);
            row[3] = itemView.findViewById(R.id._condition);
            row[4] = itemView.findViewById(R.id._wind);
            row[5] = itemView.findViewById(R.id._humidity);

            row1[0] = itemView.findViewById(R.id._time4);
            row1[1] = itemView.findViewById(R.id._temp4);
            row1[2] = itemView.findViewById(R.id._feels4);
            row1[3] = itemView.findViewById(R.id._condition4);
            row1[4] = itemView.findViewById(R.id._wind4);
            row1[5] = itemView.findViewById(R.id._humidity4);

            row2[0] = itemView.findViewById(R.id._time5);
            row2[1] = itemView.findViewById(R.id._temp5);
            row2[2] = itemView.findViewById(R.id._feels5);
            row2[3] = itemView.findViewById(R.id._condition5);
            row2[4] = itemView.findViewById(R.id._wind5);
            row2[5] = itemView.findViewById(R.id._humidity5);

            row3[0] = itemView.findViewById(R.id._time6);
            row3[1] = itemView.findViewById(R.id._temp6);
            row3[2] = itemView.findViewById(R.id._feels6);
            row3[3] = itemView.findViewById(R.id._condition6);
            row3[4] = itemView.findViewById(R.id._wind6);
            row3[5] = itemView.findViewById(R.id._humidity6);

            row4[0] = itemView.findViewById(R.id._time7);
            row4[1] = itemView.findViewById(R.id._temp7);
            row4[2] = itemView.findViewById(R.id._feels7);
            row4[3] = itemView.findViewById(R.id._condition7);
            row4[4] = itemView.findViewById(R.id._wind7);
            row4[5] = itemView.findViewById(R.id._humidity7);

            row5[0] = itemView.findViewById(R.id._time8);
            row5[1] = itemView.findViewById(R.id._temp8);
            row5[2] = itemView.findViewById(R.id._feels8);
            row5[3] = itemView.findViewById(R.id._condition8);
            row5[4] = itemView.findViewById(R.id._wind8);
            row5[5] = itemView.findViewById(R.id._humidity8);

            row6[0] = itemView.findViewById(R.id._time9);
            row6[1] = itemView.findViewById(R.id._temp9);
            row6[2] = itemView.findViewById(R.id._feels9);
            row6[3] = itemView.findViewById(R.id._condition9);
            row6[4] = itemView.findViewById(R.id._wind9);
            row6[5] = itemView.findViewById(R.id._humidity9);

            row7[0] = itemView.findViewById(R.id._time10);
            row7[1] = itemView.findViewById(R.id._temp10);
            row7[2] = itemView.findViewById(R.id._feels10);
            row7[3] = itemView.findViewById(R.id._condition10);
            row7[4] = itemView.findViewById(R.id._wind10);
            row7[5] = itemView.findViewById(R.id._humidity10);

            row8[0] = itemView.findViewById(R.id._time11);
            row8[1] = itemView.findViewById(R.id._temp11);
            row8[2] = itemView.findViewById(R.id._feels11);
            row8[3] = itemView.findViewById(R.id._condition11);
            row8[4] = itemView.findViewById(R.id._wind11);
            row8[5] = itemView.findViewById(R.id._humidity11);

            row9[0] = itemView.findViewById(R.id._time12);
            row9[1] = itemView.findViewById(R.id._temp12);
            row9[2] = itemView.findViewById(R.id._feels12);
            row9[3] = itemView.findViewById(R.id._condition12);
            row9[4] = itemView.findViewById(R.id._wind12);
            row9[5] = itemView.findViewById(R.id._humidity12);

            row10[0] = itemView.findViewById(R.id._time13);
            row10[1] = itemView.findViewById(R.id._temp13);
            row10[2] = itemView.findViewById(R.id._feels13);
            row10[3] = itemView.findViewById(R.id._condition13);
            row10[4] = itemView.findViewById(R.id._wind13);
            row10[5] = itemView.findViewById(R.id._humidity13);

            row11[0] = itemView.findViewById(R.id._time14);
            row11[1] = itemView.findViewById(R.id._temp14);
            row11[2] = itemView.findViewById(R.id._feels14);
            row11[3] = itemView.findViewById(R.id._condition14);
            row11[4] = itemView.findViewById(R.id._wind14);
            row11[5] = itemView.findViewById(R.id._humidity14);

            row12[0] = itemView.findViewById(R.id._time15);
            row12[1] = itemView.findViewById(R.id._temp15);
            row12[2] = itemView.findViewById(R.id._feels15);
            row12[3] = itemView.findViewById(R.id._condition15);
            row12[4] = itemView.findViewById(R.id._wind15);
            row12[5] = itemView.findViewById(R.id._humidity15);
        }
    }


}
