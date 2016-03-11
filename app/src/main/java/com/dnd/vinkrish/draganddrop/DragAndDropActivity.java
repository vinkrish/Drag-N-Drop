package com.dnd.vinkrish.draganddrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileReader;
import java.util.Random;

public class DragAndDropActivity extends AppCompatActivity {
    private RelativeLayout bottomLayout;
    private FrameLayout podium;
    private TextView queueView;
    private int score, wrongScore;
    private TextView yourScore, yourWrongScore, oddPlusOne, evenPlusOne;
    private ImageView oddTick, oddWrong, evenTick, evenWrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop);

        initView();

        bottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
        bottomLayout.setOnDragListener(new DragListener());

        bottomLayout.post(new Runnable() {
            @Override
            public void run() {
                generateAndAnimate();
            }
        });
    }

    private void initView(){
        podium = (FrameLayout) findViewById(R.id.podium);
        yourScore = (TextView) findViewById(R.id.your_score);
        yourWrongScore = (TextView) findViewById(R.id.your_wrong_score);

        oddPlusOne = (TextView) findViewById(R.id.odd_plus_one);
        evenPlusOne = (TextView) findViewById(R.id.even_plus_one);

        oddTick = (ImageView) findViewById(R.id.odd_tick);
        oddWrong = (ImageView) findViewById(R.id.odd_wrong);
        evenTick = (ImageView) findViewById(R.id.even_tick);
        evenWrong = (ImageView) findViewById(R.id.even_wrong);

        findViewById(R.id.oddLayout).setOnDragListener(new DragListener());
        findViewById(R.id.evenLayout).setOnDragListener(new DragListener());
    }

    private void generateAndAnimate(){

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ABOVE, podium.getId());
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);

        queueView = new TextView(getApplicationContext());
        queueView.setTag("dnd");
        queueView.setLayoutParams(lp);
        queueView.setText(generateNumber() + "");
        queueView.setTextSize(50);
        queueView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lightBlack));
        bottomLayout.addView(queueView);
        queueView.setVisibility(View.INVISIBLE);

        queueView.post(new Runnable() {
            @Override
            public void run() {
                AnimationUtil.alphaTranslate(queueView, getApplicationContext());
            }
        });

        queueView.setOnLongClickListener(new LongClickListener());

        queueView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

                String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
                ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                view.startDrag(data, shadowBuilder, view, 0);

                view.setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }

    private int generateNumber(){
        Random r = new Random();
        int Low = 1;
        int High = 100;
        return r.nextInt(High-Low) + Low;
    }

    private boolean isItOdd(){
        try{
            int number = Integer.parseInt(queueView.getText().toString());
            if(number % 2 == 0) {
                return false;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean isItEven(){
        try{
            int number = Integer.parseInt(queueView.getText().toString());
            if(number % 2 == 0) {
                return true;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateYourScore() {
        yourScore.setText(score+"");
    }

    private void updateYourWrongScore() {
        yourWrongScore.setText(wrongScore+"");
    }


    private final class LongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {
            ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

            String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag(data, shadowBuilder, view, 0);

            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class DragListener implements View.OnDragListener {
        Drawable dropArea = ContextCompat.getDrawable(getApplicationContext(), R.drawable.drop_area);
        Drawable defaultArea = ContextCompat.getDrawable(getApplicationContext(), R.drawable.default_area);

        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction()) {

                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    if(v == findViewById(R.id.oddLayout) || v == findViewById(R.id.evenLayout)) {
                        v.setBackgroundResource(R.drawable.drop_area);
                    }
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackground(defaultArea);
                    if(v == findViewById(R.id.oddLayout) || v == findViewById(R.id.evenLayout)) {
                        v.setBackgroundResource(R.drawable.default_area);
                    }
                    break;

                case DragEvent.ACTION_DROP:
                    if(v == findViewById(R.id.oddLayout)) {

                        View view = (View) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view.getParent();
                        viewgroup.removeView(view);

                        FrameLayout containView = (FrameLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.INVISIBLE);

                        if (isItOdd()) {
                            AnimationUtil.alphaScaleInOut(oddTick, getApplicationContext());
                            AnimationUtil.alphaInOut(oddPlusOne, getApplicationContext());
                            score++;
                            updateYourScore();
                        } else {
                            AnimationUtil.alphaScaleInOut(oddWrong, getApplicationContext());
                            wrongScore++;
                            updateYourWrongScore();
                        }

                        generateAndAnimate();

                    } else if (v == findViewById(R.id.evenLayout)){

                        View view = (View) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view.getParent();
                        viewgroup.removeView(view);

                        FrameLayout containView = (FrameLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.INVISIBLE);

                        if (isItEven()) {
                            AnimationUtil.alphaScaleInOut(evenTick, getApplicationContext());
                            AnimationUtil.alphaInOut(evenPlusOne, getApplicationContext());
                            score++;
                            updateYourScore();
                        } else {
                            AnimationUtil.alphaScaleInOut(evenWrong, getApplicationContext());
                            wrongScore++;
                            updateYourWrongScore();
                        }

                        generateAndAnimate();

                    } else {
                        queueView.setVisibility(View.VISIBLE);
                    }
                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackground(defaultArea);
                    if(v == findViewById(R.id.oddLayout) || v == findViewById(R.id.evenLayout)) {
                        v.setBackgroundResource(R.drawable.default_area);
                    }

                default:
                    break;
            }
            return true;
        }
    }

}
