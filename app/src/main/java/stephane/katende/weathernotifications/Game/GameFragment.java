package stephane.katende.weathernotifications.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import stephane.katende.weathernotifications.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1", ARG_PARAM2 = "param2";
    private String mParam1, mParam2;
    ConstraintLayout constraintLayout;
    TapSquare2 tapSquare2;
    Button _btbStarGame;
    SharedPreferences sharedPreferences;
    private String scoreTAG = "scoreTAG";
    TextView scoreView;


    private class TapSquare2 extends View {
        private Paint _myPaint;
        private Rect _myRect;
        private android.graphics.Canvas _myCanvas;
        private long _visibleTime, _numOfSquaresCounter;


        private int _canvasHeight, _canvasWidth, _lastTop, _lastLeft, _numberOfSquares, _visibleTimeMultiplier = 20,
                _numOfSquaresMultiplier = 10, _numOfSquaresHit, firstTime, _btnStatus = -1; //0 means game in session
        private ArrayList<String> _possiblePos; //all the posible possitions
        float xDown = 0, yDown = 0;
        private boolean _squareTouched = false, _changeRectToGreen = false, _showTitleScreen = true, _showCountDownTimer = true, _isGameOn = false, _reCalculateSquarePos = true;
        private double _score = 0;
        private static final String TAG = "TapSquare";
        String _currentLevel = "???";
        CountDownLatch btnCounter = new CountDownLatch(1);


        public long get_getButtonDisableTime() {

            return (_visibleTime * _numberOfSquares); //add 800 ms just for safe measures
        }

        public void startGame(String level) {
            _btbStarGame.setEnabled(false);
            _btnStatus = 0;
            setLevel(level);
            _numOfSquaresCounter = _numberOfSquares;
            _showTitleScreen = false;
            triggerRefresh();
            postInvalidate();
            btnCounter = new CountDownLatch(1);
        }

        public void endGame() {
            calculateScore();
            _showTitleScreen = true;
            _isGameOn = false;
            _numOfSquaresCounter = 0;
            _numOfSquaresHit = 0;
            _reCalculateSquarePos = true;
            _changeRectToGreen = false;
            _possiblePos.clear();
            firstTime = 0;
            setLevel("reset");
            _btnStatus = 1;
            _btbStarGame.setEnabled(true);
            calculateHighScore();
            scoreView.setText("HighScore : " + sharedPreferences.getString(scoreTAG, "0"));
            triggerRefresh();
            postInvalidate();
        }

        public void calculateHighScore() {
            String lastScore = sharedPreferences.getString(scoreTAG, "0");
            if (Double.valueOf(lastScore) < _score)
                sharedPreferences.edit().putString(scoreTAG, String.valueOf(_score)).apply();

        }


        public void calculateScore() {
            _score = Double.valueOf(_numOfSquaresHit * ((_visibleTimeMultiplier * _visibleTime) + (_numOfSquaresMultiplier * _numberOfSquares)));
        }

        public int getBtnStatus() {
            return _btnStatus;
        }

        /**
         * Get a level, "easy" "med" "hard"
         *
         * @param level
         * @return
         */
        public void setLevel(String level) {
            switch (level) {
                case "test":
                    _visibleTime = 5500;
                    _numberOfSquares = 6;
                    _currentLevel = "test";
                    break;
                case "easy":
                    _visibleTime = 3000;
                    _numberOfSquares = 5;
                    _currentLevel = "easy";
                    break;
                case "med":
                    _visibleTime = 1500;
                    _numberOfSquares = 10;
                    _currentLevel = "med";
                    break;
                case "hard":
                    _visibleTime = 600;
                    _numberOfSquares = 15;
                    _currentLevel = "hard";
                    break;

                case "impossible":
                    _visibleTime = 100;
                    _numberOfSquares = 30;
                    _currentLevel = "impossible";
                    break;
                case "reset":
                    _visibleTime = 0;
                    _numberOfSquares = 0;
                    break;
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    xDown = event.getX();
                    yDown = event.getY();
                    if (_myRect.left < xDown && _myRect.right > xDown && _myRect.top < yDown && _myRect.bottom > yDown) { //hit it :)
                        _numOfSquaresHit++;
                        _myPaint.setColor(getResources().getColor(R.color.GREEN));
                        _changeRectToGreen = true;
                        Log.d(TAG, "About to draw a new GREEN square");
                        postInvalidate();
                    }
                    return true;
            }
            return super.onTouchEvent(event);
        }

        @Override
        protected void onDraw(android.graphics.Canvas canvas) {
            _myCanvas = canvas;


            //show title screen
            if (_showTitleScreen) {
                canvasSize();
                allAvailableSquares();
                _reCalculateSquarePos = false;
                _isGameOn = false;
                _myPaint.setTextSize(45);
                _myPaint.setColor(getResources().getColor(R.color.GREEN));
                _myPaint.setTextAlign(Paint.Align.CENTER);
                _myPaint.setTypeface(ResourcesCompat.getFont(getContext(), R.font.zendots_regular));
                canvas.drawText("Last Score : " + _score, _canvasWidth / 2, (_canvasHeight / 2) + 45, _myPaint);
                canvas.drawText("Click start to play, Level : " + _currentLevel, _canvasWidth / 2, _canvasHeight / 2, _myPaint);
            } else if (_numOfSquaresCounter >= 1) { //play the game
                _isGameOn = true;
                //set some properties
                if (!_changeRectToGreen) {
                    _numOfSquaresCounter--;
                    _myPaint.setColor(getResources().getColor(R.color.RED));
                    Log.d(TAG, "About to draw a new RED square");
                    setCoordinateForRandomSquare(); //new location
                }
                //draw the square
                _myRect.top = _lastTop;
                _myRect.left = _lastLeft;
                _myRect.right = _myRect.left + 60;
                _myRect.bottom = _myRect.top + 60;
                canvas.drawRect(_myRect, _myPaint); //drawing it

                if (_changeRectToGreen) {
                    _changeRectToGreen = false;
                    postInvalidateDelayed(150);
                    Log.d(TAG, "About to wait 3sec then Refresh");
                }
                Log.d(TAG, "About to wait 5.5sec then Refresh");
                postInvalidateDelayed(_visibleTime);


            } else {
                endGame();
            }

        }

        /**
         * Call this before postinvalid to trigger the little thing :)
         */
        public void triggerRefresh() {
            _myCanvas.drawRect(new Rect(0, 0, 0 + 2, 0 + 2), _myPaint);
        }

        /**
         * Sets up the size of the canvas
         */
        private void canvasSize() {
            _canvasHeight = _myCanvas.getHeight() - getResources().getDimensionPixelOffset(R.dimen.margin); //don't wanna draw too far down where they are buttons
            _canvasWidth = _myCanvas.getWidth() - 60;
        }

        /**
         * Draw a 60pixel rect using a random coordinate from tempList
         */
        public void setCoordinateForRandomSquare() {
            Scanner read;
            String x, y;
            int i = new Random().nextInt(_possiblePos.size());
            read = new Scanner(_possiblePos.get(i));
            x = read.next();
            y = read.next();
            _lastTop = Integer.valueOf(y);
            _lastLeft = Integer.valueOf(x);
        }

        /**
         * Based on the width and height of the canvas computer all available 60pixel rectangles and store their top, left values (x,y)
         * A screen width and height of 120 pixel can fit 4 60 pixel rectangles, the first two elements would be "0 60", "0 120" and last two "60 60" "120 120" and this is all
         * the 60pixel squares we can get with those dimensions
         */
        private void allAvailableSquares() {
            if (_reCalculateSquarePos) {
                String tempX;
                String tempY;

                for (int yPos = 0; yPos <= _canvasHeight; yPos += 60) { // x is left, y is top //printing y x
                    for (int xPos = 0; xPos <= _canvasWidth; xPos += 60) {
                        tempX = String.valueOf(xPos);
                        tempY = String.valueOf(yPos);
                        _possiblePos.add(tempX + " " + tempY);
                    }
                }
            }
        }

        public void init(@Nullable AttributeSet set) {
            _myRect = new Rect();
            _myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            _myPaint.setStyle(Paint.Style.FILL);
            _possiblePos = new ArrayList<>();

        }

        public TapSquare2(Context context) {
            super(context);
            init(null);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            _reCalculateSquarePos = true;
            firstTime++;
            _canvasHeight = h;
            _canvasWidth = w;

            if (firstTime > 1) {//don't call the first time it triggers!
                allAvailableSquares();
                _reCalculateSquarePos = false;
            }
        }

        public TapSquare2(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(attrs);
        }

        public TapSquare2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(attrs);
        }

        public TapSquare2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init(attrs);
        }
    }


    public GameFragment() {
        // Required empty public constructor
    }

    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        constraintLayout = view.findViewById(R.id.ConstraitLayout);
        tapSquare2 = new TapSquare2(getActivity());
        constraintLayout.addView(tapSquare2, ConstraintLayout.LayoutParams.MATCH_PARENT);
        _btbStarGame = view.findViewById(R.id._btnStartGame);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        scoreView = view.findViewById(R.id._tvHighScore);
        return view;
    }

    @Override
    public void onResume() {

        scoreView.setText("HighScore : " + sharedPreferences.getString(scoreTAG, "0"));
        super.onResume();

        _btbStarGame.setOnClickListener(v -> {
            playGame();

        });
    }

    private void playGame() {
        String userLevel = sharedPreferences.getString("list_preference_1", "test");
        tapSquare2.startGame(userLevel);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getActivity().findViewById(R.id._tvBackground).setBackground(getResources().getDrawable(R.color.white));
        TextView x = getActivity().findViewById(R.id._tvBackground);
        x.setText(" Try to tap the square before it disappears! The game will end after certain numbers of squares have appeared. You get less time as the level increases. " +
                "Try to beat your last score!");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().findViewById(R.id._tvBackground).setVisibility(View.VISIBLE);
        TextView x = getActivity().findViewById(R.id._tvBackground);
        getActivity().findViewById(R.id._tvBackground).setBackground(getResources().getDrawable(R.color.ic_launcher_background));
        x.setText("");
    }
}