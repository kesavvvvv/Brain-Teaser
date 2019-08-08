package com.kesavvv.brainteaser;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
  int ans = this.x + this.y;
  
  ImageView gameOverImageView;
  
  TextView introTextView;
  
  Button option1;
  
  Button option2;
  
  Button option3;
  
  Button option4;
  
  TextView questionTextView;
  
  Random r = new Random();
  
  Button restartGameButton;
  
  int score = 0;
  
  TextView scoreTextView;
  
  Button startGameButton;
  
  TextView timerTextView;
  
  TextView titleTextView;
  
  int x = this.r.nextInt(100);
  
  int y = this.r.nextInt(100);
  
  public void checkAnswer(View paramView) {
    switch (paramView.getId()) {
      default:
        return;
      case 2131165276:
        if (Integer.valueOf(this.option4.getText().toString()).intValue() == this.ans) {
          this.score++;
          generateQuestion();
        } else {
          generateQuestion();
        } 
        textView = this.scoreTextView;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Score: ");
        stringBuilder.append(Integer.toString(this.score));
        textView.setText(stringBuilder.toString());
        Toast.makeText(this, Integer.toString(this.score), 0).show();
        return;
      case 2131165275:
        if (Integer.valueOf(this.option3.getText().toString()).intValue() == this.ans) {
          this.score++;
          generateQuestion();
        } else {
          generateQuestion();
        } 
        textView = this.scoreTextView;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Score: ");
        stringBuilder.append(Integer.toString(this.score));
        textView.setText(stringBuilder.toString());
        Toast.makeText(this, Integer.toString(this.score), 0).show();
        return;
      case 2131165274:
        if (Integer.valueOf(this.option2.getText().toString()).intValue() == this.ans) {
          this.score++;
          generateQuestion();
        } else {
          generateQuestion();
        } 
        textView = this.scoreTextView;
        stringBuilder = new StringBuilder();
        stringBuilder.append("Score: ");
        stringBuilder.append(Integer.toString(this.score));
        textView.setText(stringBuilder.toString());
        Toast.makeText(this, Integer.toString(this.score), 0).show();
        return;
      case 2131165273:
        break;
    } 
    if (Integer.valueOf(this.option1.getText().toString()).intValue() == this.ans) {
      this.score++;
      generateQuestion();
    } else {
      generateQuestion();
    } 
    TextView textView = this.scoreTextView;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Score: ");
    stringBuilder.append(Integer.toString(this.score));
    textView.setText(stringBuilder.toString());
    Toast.makeText(this, Integer.toString(this.score), 0).show();
  }
  
  public void generateQuestion() {
    this.x = this.r.nextInt(100);
    this.y = this.r.nextInt(100);
    this.ans = this.x + this.y;
    TextView textView = this.questionTextView;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Integer.toString(this.x));
    stringBuilder.append(" + ");
    stringBuilder.append(Integer.toString(this.y));
    textView.setText(stringBuilder.toString());
    switch (this.r.nextInt(4)) {
      default:
        return;
      case 3:
        this.option2.setText(Integer.toString(this.r.nextInt(300)));
        this.option3.setText(Integer.toString(this.r.nextInt(300)));
        this.option1.setText(Integer.toString(this.r.nextInt(300)));
        this.option4.setText(Integer.toString(this.ans));
        return;
      case 2:
        this.option3.setText(Integer.toString(this.ans));
        this.option2.setText(Integer.toString(this.r.nextInt(300)));
        this.option1.setText(Integer.toString(this.r.nextInt(300)));
        this.option4.setText(Integer.toString(this.r.nextInt(300)));
        return;
      case 1:
        this.option2.setText(Integer.toString(this.ans));
        this.option1.setText(Integer.toString(this.r.nextInt(300)));
        this.option3.setText(Integer.toString(this.r.nextInt(300)));
        this.option4.setText(Integer.toString(this.r.nextInt(300)));
        return;
      case 0:
        break;
    } 
    this.option1.setText(Integer.toString(this.ans));
    this.option2.setText(Integer.toString(this.r.nextInt(300)));
    this.option3.setText(Integer.toString(this.r.nextInt(300)));
    this.option4.setText(Integer.toString(this.r.nextInt(300)));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131296284);
    this.startGameButton = (Button)findViewById(2131165318);
    this.restartGameButton = (Button)findViewById(2131165285);
    this.titleTextView = (TextView)findViewById(2131165333);
    this.questionTextView = (TextView)findViewById(2131165283);
    this.timerTextView = (TextView)findViewById(2131165330);
    this.introTextView = (TextView)findViewById(2131165256);
    this.scoreTextView = (TextView)findViewById(2131165289);
    this.gameOverImageView = (ImageView)findViewById(2131165245);
    this.option1 = (Button)findViewById(2131165273);
    this.option2 = (Button)findViewById(2131165274);
    this.option3 = (Button)findViewById(2131165275);
    this.option4 = (Button)findViewById(2131165276);
    this.option1.setVisibility(4);
    this.option2.setVisibility(4);
    this.option3.setVisibility(4);
    this.option4.setVisibility(4);
    this.restartGameButton.setVisibility(4);
    this.questionTextView.setVisibility(4);
    this.timerTextView.setVisibility(4);
    this.introTextView.setVisibility(4);
    this.scoreTextView.setVisibility(4);
    this.gameOverImageView.setVisibility(4);
  }
  
  public void startGameClick(View paramView) {
    this.score = 0;
    TextView textView = this.scoreTextView;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Score: ");
    stringBuilder.append(Integer.toString(this.score));
    textView.setText(stringBuilder.toString());
    this.startGameButton.setVisibility(4);
    this.restartGameButton.setVisibility(4);
    this.titleTextView.setVisibility(4);
    this.gameOverImageView.setVisibility(4);
    this.questionTextView.setVisibility(0);
    this.timerTextView.setVisibility(0);
    this.introTextView.setVisibility(0);
    this.scoreTextView.setVisibility(0);
    this.option1.setVisibility(0);
    this.option2.setVisibility(0);
    this.option3.setVisibility(0);
    this.option4.setVisibility(0);
    (new CountDownTimer(60000L, 1000L) {
        public void onFinish() {
          MainActivity.this.questionTextView.setVisibility(4);
          MainActivity.this.timerTextView.setVisibility(4);
          MainActivity.this.introTextView.setVisibility(4);
          MainActivity.this.option1.setVisibility(4);
          MainActivity.this.option2.setVisibility(4);
          MainActivity.this.option3.setVisibility(4);
          MainActivity.this.option4.setVisibility(4);
          MainActivity.this.gameOverImageView.setVisibility(0);
          MainActivity.this.restartGameButton.setVisibility(0);
          MainActivity.this.option1.setText("");
          MainActivity.this.option2.setText("");
          MainActivity.this.option3.setText("");
          MainActivity.this.option4.setText("");
        }
        
        public void onTick(long param1Long) { MainActivity.this.updateTimer((int)param1Long / 1000); }
      }).start();
    generateQuestion();
  }
  
  public void updateTimer(int paramInt) {
    if (paramInt < 10) {
      TextView textView1 = this.timerTextView;
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("00:0");
      stringBuilder1.append(Integer.toString(paramInt));
      textView1.setText(stringBuilder1.toString());
      return;
    } 
    TextView textView = this.timerTextView;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("00:");
    stringBuilder.append(Integer.toString(paramInt));
    textView.setText(stringBuilder.toString());
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\com\kesavvv\brainteaser\MainActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */