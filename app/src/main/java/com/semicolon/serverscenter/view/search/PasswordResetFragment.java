package com.semicolon.serverscenter.view.search;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.semicolon.serverscenter.R;
import com.semicolon.serverscenter.util.GMailSender;
import com.semicolon.serverscenter.view.signUp.SignUpFragment;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.common.base.Preconditions.checkNotNull;

public class PasswordResetFragment extends Fragment implements PasswordResetContract.View {

    @BindView(R.id.editPasswordResetEmail)
    EditText editPasswordResetEmail;
    @BindView(R.id.btnPasswordResetAuthCodeSend)
    Button btnPasswordResetAuthCodeSend;
    @BindView(R.id.btnPasswordResetNext)
    Button btnPasswordResetNext;

    /* Dialog Field */
    private Dialog authDialog;
    private LayoutInflater dialog;
    private View dialogLayout;

    /* Dialog Timer Field */
    private TextView time_counter;
    private EditText emailAuth_number;
    private Button emailAuth_btn;
    private CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 300 * 1000; // 300 seconds
    final int COUNT_DOWN_INTERVAL = 1000; // onTick Method interval. (1 second)

    private boolean emailOkSwitch;
    private String emailCode;

    private PasswordResetContract.Presenter mPresenter;

    public static PasswordResetFragment newInstance() {
        PasswordResetFragment fragment = new PasswordResetFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.password_reset_frag, container, false);
        setHasOptionsMenu(true);

        ButterKnife.bind(this, root);

        return root;
    }

    @OnClick(R.id.btnPasswordResetAuthCodeSend)
    public void onClickSendEmail() {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
        try {
            GMailSender gMailSender = new GMailSender("shady952012@gmail.com", "Vpxhf9483!");
            //GMailSender.sendMail(제목, 본문내용, 받는사람);
            emailCode=gMailSender.getEmailCode();
            gMailSender.sendMail("ServersCenter 이메일 인증입니다.","인증코드 : "+emailCode,editPasswordResetEmail.getText().toString());
            Toast.makeText(getContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(getContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(getContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        dialog = LayoutInflater.from(getContext());
        dialogLayout = dialog.inflate(R.layout.sign_up_auth_dialog, null);
        authDialog = new Dialog(getContext());
        authDialog.setContentView(dialogLayout);
        authDialog.setCanceledOnTouchOutside(false);
        authDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // Dialog Cancel Event...
                countDownTimer.cancel();
            }
        });
        authDialog.show();
        countDownTimer();
    }

    @OnClick(R.id.btnPasswordResetNext)
    public void onClickResetNext() {
        if (editPasswordResetEmail.getText().toString().equals("")) {

            if (!emailOkSwitch) {
                Toast.makeText(getContext(), "이메일 인증을 확인해주세요.", Toast.LENGTH_SHORT).show();
                btnPasswordResetNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darkPurple1));
                btnPasswordResetNext.setEnabled(true);
                return;
            }
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = editPasswordResetEmail.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "비밀번호변경 메세지를 보냈습니다.", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }
                });

    }

    @Override
    public void setPresenter(PasswordResetContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    // Count Down Method
    public void countDownTimer() {
        time_counter = (TextView) dialogLayout.findViewById(R.id.emailAuth_time_counter);
        emailAuth_number = (EditText) dialogLayout.findViewById(R.id.emailAuth_number);
        emailAuth_btn = (Button) dialogLayout.findViewById(R.id.emailAuth_btn);

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) {
                    time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else {
                    time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
            }

            @Override
            public void onFinish() {
                authDialog.cancel();
            }
        }.start();

        emailAuth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_answer;
                if (emailAuth_number.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "받은 보안 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    user_answer = emailAuth_number.getText().toString();

                    if (user_answer.equals(emailCode)) {
                        Toast.makeText(getContext(), "이메일 인증 성공", Toast.LENGTH_SHORT).show();
                        emailOkSwitch = true;
                        authDialog.cancel();
                    } else {
                        Toast.makeText(getContext(), "이메일 인증 실패", Toast.LENGTH_SHORT).show();
                        emailOkSwitch = false;
                    }
                }
            }
        });
    }
}