package stephane.katende.weathernotifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ToggleButton;

import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity {
    private Button _btnResetScore;
    private ToggleButton _btnStartGame;
    public TapSquare _tapSquare;
    private static final String TAG = "MainActivity";
    private Handler handler;
    private Runnable runnable;
    private MainViewModel mainViewModel;
    Thread btnThread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        _btnResetScore = findViewById(R.id._btnLeaveGame);
        _btnStartGame = findViewById(R.id._btnStartGame);
        _tapSquare = findViewById(R.id._tapSquareCanvas);
        handler = new Handler(getMainLooper());
        btnThread = new Thread(runnable);



        _btnStartGame.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnThread.start();
            playGame("test");
        });


        runnable = () -> {
            try {
                _tapSquare.getBtnCounter().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            _btnStartGame.setEnabled(true);
         };



    }

    private void playGame(String level) {
        _btnStartGame.setEnabled(false);
        _tapSquare.startGame(level);
       // disableButton(_tapSquare.get_getButtonDisableTime());
    }

    private void disableButton(long delay){
        handler.postDelayed(runnable, delay);
    }

    public void enableButton(){
        _btnStartGame.setEnabled(true);
    }
}