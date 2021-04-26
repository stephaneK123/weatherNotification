package stephane.katende.weathernotifications;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class TapSquare extends View {
    private Paint _myPaint;
    private Rect _myRect;
    private android.graphics.Canvas _myCanvas;
    private long _visibleTime, _numOfSquaresCounter;




    private final GameActivity _gameActivity = new GameActivity();
    private int _canvasHeight, _canvasWidth, _lastTop, _lastLeft, _numberOfSquares, _visibleTimeMultiplier = 20,
            _numOfSquaresMultiplier = 10, _numOfSquaresHit, firstTime;
    private ArrayList<String> _possiblePos; //all the posible possitions
    float xDown = 0, yDown = 0;
    private boolean _squareTouched = false, _changeRectToGreen = false, _showTitleScreen = true, _showCountDownTimer = true, _isGameOn = false, _reCalculateSquarePos = true;
    private double _score = 0;
    private static final String TAG = "TapSquare";
    String _currentLevel = null;
    CountDownLatch btnCounter = new CountDownLatch(1);


    public CountDownLatch getBtnCounter(){
        return btnCounter;
    }


    public long get_getButtonDisableTime() {

        return (_visibleTime * _numberOfSquares); //add 800 ms just for safe measures
    }

    public void startGame(String level) {
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
      //  _isGameOver.setValue(true);
        btnCounter.countDown();
        triggerRefresh();
        postInvalidate();
    }


    public void calculateScore() {
        _score = Double.valueOf(_numOfSquaresHit * ((_visibleTimeMultiplier * _visibleTime) + (_numOfSquaresMultiplier * _numberOfSquares)));
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
                _numberOfSquares = 3;
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

    public TapSquare(Context context) {
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

    public TapSquare(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TapSquare(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public TapSquare(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
}
