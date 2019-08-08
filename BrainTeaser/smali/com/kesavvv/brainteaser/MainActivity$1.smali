.class Lcom/kesavvv/brainteaser/MainActivity$1;
.super Landroid/os/CountDownTimer;
.source "MainActivity.java"


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/kesavvv/brainteaser/MainActivity;->startGameClick(Landroid/view/View;)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/kesavvv/brainteaser/MainActivity;


# direct methods
.method constructor <init>(Lcom/kesavvv/brainteaser/MainActivity;JJ)V
    .locals 0
    .param p1, "this$0"    # Lcom/kesavvv/brainteaser/MainActivity;
    .param p2, "x0"    # J
    .param p4, "x1"    # J

    .line 167
    iput-object p1, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    invoke-direct {p0, p2, p3, p4, p5}, Landroid/os/CountDownTimer;-><init>(JJ)V

    return-void
.end method


# virtual methods
.method public onFinish()V
    .locals 2

    .line 178
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->questionTextView:Landroid/widget/TextView;

    const/4 v1, 0x4

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setVisibility(I)V

    .line 179
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->timerTextView:Landroid/widget/TextView;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setVisibility(I)V

    .line 180
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->introTextView:Landroid/widget/TextView;

    invoke-virtual {v0, v1}, Landroid/widget/TextView;->setVisibility(I)V

    .line 183
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 184
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 185
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 186
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 188
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->gameOverImageView:Landroid/widget/ImageView;

    const/4 v1, 0x0

    invoke-virtual {v0, v1}, Landroid/widget/ImageView;->setVisibility(I)V

    .line 189
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->restartGameButton:Landroid/widget/Button;

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setVisibility(I)V

    .line 191
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->option1:Landroid/widget/Button;

    const-string v1, ""

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 192
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->option2:Landroid/widget/Button;

    const-string v1, ""

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 193
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->option3:Landroid/widget/Button;

    const-string v1, ""

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 194
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    iget-object v0, v0, Lcom/kesavvv/brainteaser/MainActivity;->option4:Landroid/widget/Button;

    const-string v1, ""

    invoke-virtual {v0, v1}, Landroid/widget/Button;->setText(Ljava/lang/CharSequence;)V

    .line 196
    return-void
.end method

.method public onTick(J)V
    .locals 2
    .param p1, "millisUntilFinished"    # J

    .line 172
    iget-object v0, p0, Lcom/kesavvv/brainteaser/MainActivity$1;->this$0:Lcom/kesavvv/brainteaser/MainActivity;

    long-to-int v1, p1

    div-int/lit16 v1, v1, 0x3e8

    invoke-virtual {v0, v1}, Lcom/kesavvv/brainteaser/MainActivity;->updateTimer(I)V

    .line 173
    return-void
.end method
