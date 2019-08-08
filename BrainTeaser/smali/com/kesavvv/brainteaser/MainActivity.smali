.class public Lcom/kesavvv/brainteaser/MainActivity;
.super Landroid/support/v7/app/AppCompatActivity;
.source "MainActivity.java"


# instance fields
.field ans:I

.field gameOverImageView:Landroid/widget/ImageView;

.field introTextView:Landroid/widget/TextView;

.field option1:Landroid/widget/Button;

.field option2:Landroid/widget/Button;

.field option3:Landroid/widget/Button;

.field option4:Landroid/widget/Button;

.field questionTextView:Landroid/widget/TextView;

.field r:Ljava/util/Random;

.field restartGameButton:Landroid/widget/Button;

.field score:I

.field scoreTextView:Landroid/widget/TextView;

.field startGameButton:Landroid/widget/Button;

.field timerTextView:Landroid/widget/TextView;

.field titleTextView:Landroid/widget/TextView;

.field x:I

.field y:I


# direct methods
.method public constructor <init>()V
    .locals 2

    .line 16
    invoke-direct {p0}, Landroid/support/v7/app/AppCompatActivity;-><init>()V

    .line 34
    new-instance v0, Ljava/util/Random;

    invoke-direct {v0}, Ljava/util/Random;-><init>()V

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    .line 36
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    const/16 v1, 0x64

    invoke-virtual {v0, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v0

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->x:I

    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v0, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v0

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->y:I

    .line 37
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->x:I

    iget v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->y:I

    add-int/2addr v0, v1

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    .line 39
    const/4 v0, 0x0

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    return-void
.end method


# virtual methods
.method public checkAnswer(Landroid/view/View;)V
    .locals 4
    .param p1, "view"    # Landroid/view/View;

    .line 96
    invoke-virtual {p1}, Landroid/view/View;->getId()I

    move-result v0

    const/4 v1, 0x0

    packed-switch v0, :pswitch_data_0

    goto/16 :goto_4

    .line 132
    :pswitch_0
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    invoke-virtual {v0}, Landroid/widget/Button;->getText()Ljava/lang/CharSequence;

    move-result-object v0

    invoke-interface {v0}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    iget v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    if-ne v0, v2, :cond_0

    .line 134
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    .line 135
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    goto :goto_0

    .line 138
    :cond_0
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    .line 139
    :goto_0
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->scoreTextView:Landroid/widget/TextView;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Score: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 140
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v0}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object v0

    invoke-virtual {v0}, Landroid/widget/Toast;->show()V

    goto/16 :goto_4

    .line 121
    :pswitch_1
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    invoke-virtual {v0}, Landroid/widget/Button;->getText()Ljava/lang/CharSequence;

    move-result-object v0

    invoke-interface {v0}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    iget v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    if-ne v0, v2, :cond_1

    .line 123
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    .line 124
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    goto :goto_1

    .line 127
    :cond_1
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    .line 128
    :goto_1
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->scoreTextView:Landroid/widget/TextView;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Score: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 129
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v0}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object v0

    invoke-virtual {v0}, Landroid/widget/Toast;->show()V

    .line 130
    goto/16 :goto_4

    .line 110
    :pswitch_2
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    invoke-virtual {v0}, Landroid/widget/Button;->getText()Ljava/lang/CharSequence;

    move-result-object v0

    invoke-interface {v0}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    iget v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    if-ne v0, v2, :cond_2

    .line 112
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    .line 113
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    goto :goto_2

    .line 116
    :cond_2
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    .line 117
    :goto_2
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->scoreTextView:Landroid/widget/TextView;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Score: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 118
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v0}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object v0

    invoke-virtual {v0}, Landroid/widget/Toast;->show()V

    .line 119
    goto :goto_4

    .line 99
    :pswitch_3
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    invoke-virtual {v0}, Landroid/widget/Button;->getText()Ljava/lang/CharSequence;

    move-result-object v0

    invoke-interface {v0}, Ljava/lang/CharSequence;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v0}, Ljava/lang/Integer;->valueOf(Ljava/lang/String;)Ljava/lang/Integer;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/Integer;->intValue()I

    move-result v0

    iget v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    if-ne v0, v2, :cond_3

    .line 101
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    add-int/lit8 v0, v0, 0x1

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    .line 102
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    goto :goto_3

    .line 105
    :cond_3
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    .line 106
    :goto_3
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->scoreTextView:Landroid/widget/TextView;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Score: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v0, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 107
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v0}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v0

    invoke-static {p0, v0, v1}, Landroid/widget/Toast;->makeText(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;

    move-result-object v0

    invoke-virtual {v0}, Landroid/widget/Toast;->show()V

    .line 108
    nop

    .line 145
    :goto_4
    return-void

    nop

    :pswitch_data_0
    .packed-switch 0x7f070059
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method public generateQuestion()V
    .locals 4

    .line 53
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    const/16 v1, 0x64

    invoke-virtual {v0, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v0

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->x:I

    .line 54
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v0, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v0

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->y:I

    .line 55
    iget v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->x:I

    iget v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->y:I

    add-int/2addr v0, v1

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    .line 57
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->questionTextView:Landroid/widget/TextView;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    iget v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->x:I

    invoke-static {v2}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    const-string v2, " + "

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->y:I

    invoke-static {v2}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 59
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    const/4 v1, 0x4

    invoke-virtual {v0, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v0

    .line 63
    .local v0, "position":I
    const/16 v1, 0x12c

    packed-switch v0, :pswitch_data_0

    goto/16 :goto_0

    .line 84
    :pswitch_0
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 85
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 86
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v1

    invoke-static {v1}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 87
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    iget v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    invoke-static {v2}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    goto/16 :goto_0

    .line 78
    :pswitch_1
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    iget v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 79
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 80
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 81
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v1

    invoke-static {v1}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 82
    goto :goto_0

    .line 72
    :pswitch_2
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    iget v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 73
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 74
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 75
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v1

    invoke-static {v1}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 76
    goto :goto_0

    .line 66
    :pswitch_3
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    iget v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->ans:I

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 67
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 68
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v3

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 69
    iget-object v2, p0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    iget-object v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->r:Ljava/util/Random;

    invoke-virtual {v3, v1}, Ljava/util/Random;->nextInt(I)I

    move-result v1

    invoke-static {v1}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v2, v1}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 70
    nop

    .line 92
    :goto_0
    return-void

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method

.method protected onCreate(Landroid/os/Bundle;)V
    .locals 2
    .param p1, "savedInstanceState"    # Landroid/os/Bundle;

    .line 205
    invoke-super {p0, p1}, Landroid/support/v7/app/AppCompatActivity;->onCreate(Landroid/os/Bundle;)V

    .line 206
    const v0, 0x7f09001c

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->setContentView(I)V

    .line 208
    const v0, 0x7f070086

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->startGameButton:Landroid/widget/Button;

    .line 209
    const v0, 0x7f070065

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->restartGameButton:Landroid/widget/Button;

    .line 211
    const v0, 0x7f070095

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/TextView;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->titleTextView:Landroid/widget/TextView;

    .line 212
    const v0, 0x7f070063

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/TextView;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->questionTextView:Landroid/widget/TextView;

    .line 213
    const v0, 0x7f070092

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/TextView;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->timerTextView:Landroid/widget/TextView;

    .line 214
    const v0, 0x7f070048

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/TextView;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->introTextView:Landroid/widget/TextView;

    .line 215
    const v0, 0x7f070069

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/TextView;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->scoreTextView:Landroid/widget/TextView;

    .line 217
    const v0, 0x7f07003d

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/ImageView;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->gameOverImageView:Landroid/widget/ImageView;

    .line 219
    const v0, 0x7f070059

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    .line 220
    const v0, 0x7f07005a

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    .line 221
    const v0, 0x7f07005b

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    .line 222
    const v0, 0x7f07005c

    invoke-virtual {p0, v0}, Lcom/kesavvv/brainteaser/MainActivity;->findViewById(I)Landroid/view/View;

    move-result-object v0

    check-cast v0, Landroid/widget/Button;

    iput-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    .line 224
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    const/4 v1, 0x4

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 225
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 226
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 227
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 228
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->restartGameButton:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 230
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->questionTextView:Landroid/widget/TextView;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setVisibility(I)V

    .line 231
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->timerTextView:Landroid/widget/TextView;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setVisibility(I)V

    .line 232
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->introTextView:Landroid/widget/TextView;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setVisibility(I)V

    .line 233
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->scoreTextView:Landroid/widget/TextView;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setVisibility(I)V

    .line 235
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->gameOverImageView:Landroid/widget/ImageView;

    invoke-virtual {v0, v1}, Landroid/widget/ImageView;->setVisibility(I)V

    .line 238
    return-void
.end method

.method public startGameClick(Landroid/view/View;)V
    .locals 8
    .param p1, "view"    # Landroid/view/View;

    .line 148
    const/4 v0, 0x0

    iput v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    .line 149
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->scoreTextView:Landroid/widget/TextView;

    new-instance v2, Ljava/lang/StringBuilder;

    invoke-direct {v2}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Score: "

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    iget v3, p0, Lcom/kesavvv/brainteaser/MainActivity;->score:I

    invoke-static {v3}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v3

    invoke-virtual {v2, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 150
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->startGameButton:Landroid/widget/Button;

    const/4 v2, 0x4

    invoke-virtual {v1, v2}, Landroid/widget/Button;->setVisibility(I)V

    .line 151
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->restartGameButton:Landroid/widget/Button;

    invoke-virtual {v1, v2}, Landroid/widget/Button;->setVisibility(I)V

    .line 153
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->titleTextView:Landroid/widget/TextView;

    invoke-virtual {v1, v2}, Landroid/widget/TextView;->setVisibility(I)V

    .line 154
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->gameOverImageView:Landroid/widget/ImageView;

    invoke-virtual {v1, v2}, Landroid/widget/ImageView;->setVisibility(I)V

    .line 156
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->questionTextView:Landroid/widget/TextView;

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setVisibility(I)V

    .line 157
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->timerTextView:Landroid/widget/TextView;

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setVisibility(I)V

    .line 158
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->introTextView:Landroid/widget/TextView;

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setVisibility(I)V

    .line 159
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->scoreTextView:Landroid/widget/TextView;

    invoke-virtual {v1, v0}, Landroid/widget/TextView;->setVisibility(I)V

    .line 161
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    invoke-virtual {v1, v0}, Landroid/widget/Button;->setVisibility(I)V

    .line 162
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    invoke-virtual {v1, v0}, Landroid/widget/Button;->setVisibility(I)V

    .line 163
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    invoke-virtual {v1, v0}, Landroid/widget/Button;->setVisibility(I)V

    .line 164
    iget-object v1, p0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    invoke-virtual {v1, v0}, Landroid/widget/Button;->setVisibility(I)V

    .line 166
    new-instance v0, Lcom/kesavvv/brainteaser/MainActivity$1;

    const-wide/32 v4, 0xea60

    const-wide/16 v6, 0x3e8

    move-object v2, v0

    move-object v3, p0

    invoke-direct/range {v2 .. v7}, Lcom/kesavvv/brainteaser/MainActivity$1;-><init>(Lcom/kesavvv/brainteaser/MainActivity;JJ)V

    .line 197
    invoke-virtual {v0}, Lcom/kesavvv/brainteaser/MainActivity$1;->start()Landroid/os/CountDownTimer;

    .line 199
    invoke-virtual {p0}, Lcom/kesavvv/brainteaser/MainActivity;->generateQuestion()V

    .line 202
    return-void
.end method

.method public updateTimer(I)V
    .locals 3
    .param p1, "secondsLeft"    # I

    .line 43
    const/16 v0, 0xa

    if-ge p1, v0, :cond_0

    .line 44
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->timerTextView:Landroid/widget/TextView;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "00:0"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {p1}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    goto :goto_0

    .line 46
    :cond_0
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity;->timerTextView:Landroid/widget/TextView;

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v2, "00:"

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-static {p1}, Ljava/lang/Integer;->toString(I)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    invoke-virtual {v1}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v1

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setText(Ljava/lang/CharSequence;)V

    .line 48
    :goto_0
    return-void
.end method
