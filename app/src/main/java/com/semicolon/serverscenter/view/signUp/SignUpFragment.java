package com.semicolon.serverscenter.view.signUp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.semicolon.serverscenter.R;
import com.semicolon.serverscenter.data.model.UserModel;
import com.semicolon.serverscenter.util.GMailSender;
import com.semicolon.serverscenter.view.signIn.SignInActivity;

import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends Fragment implements SignUpContract.View {

    private static final int PICK_FROM_ALBUM = 10;
    private static final int RESULT_OK = -1;

    private SignUpContract.Presenter mPresenter;

    private ImageView mImgSignUpProfile;
    private Uri profileImageUri;

    private EditText mEditSignUpEmail;
    private EditText mEditSignUpPwFirst;
    private EditText mEditSignUpPwSecond;
    private EditText mEditSignUpName;
    private EditText mEditSignUpBirth;

    private RadioButton mRadioButtonGenderMale;
    private RadioButton mRadioButtonGenderFemale;

    private Button mBtnSignUpAuthCodeSend;
    private Button mBtnSignUpNext;

    /* Dialog Field */
    private LayoutInflater dialog;
    private View dialogLayout;
    private Dialog authDialog;


    /* Dialog Timer Field */
    private TextView time_counter;
    private EditText emailAuth_number;
    private Button emailAuth_btn;
    private CountDownTimer countDownTimer;
    final int MILLISINFUTURE = 300 * 1000; // 300 seconds
    final int COUNT_DOWN_INTERVAL = 1000; // onTick Method interval. (1 second)

    private boolean emailOkSwitch;
    private String emailCode;

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
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
        final View root = inflater.inflate(R.layout.sign_up_frag, container, false);
        setHasOptionsMenu(true);

        mImgSignUpProfile = (ImageView) root.findViewById(R.id.imgSignUpProfile);

        mImgSignUpProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        mEditSignUpEmail = (EditText) root.findViewById(R.id.editSignUpEmail);
        mEditSignUpPwFirst = (EditText) root.findViewById(R.id.editSignUpPwFirst);
        mEditSignUpPwSecond = (EditText) root.findViewById(R.id.editSignUpPwSecond);
        mEditSignUpName = (EditText) root.findViewById(R.id.editSignUpName);
        mEditSignUpBirth = (EditText) root.findViewById(R.id.editSignUpBirth);

        mEditSignUpBirth.addTextChangedListener(new TextWatcher() {
            public static final int MAX_FORMAT_LENGTH = 8;
            public static final int MIN_FORMAT_LENGTH = 5;

            private String updatedText;
            private boolean editing;


            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.toString().equals(updatedText) || editing) return;

                String digitsOnly = text.toString().replaceAll("\\D", "");
                int digitLen = digitsOnly.length();

                if (digitLen < MIN_FORMAT_LENGTH || digitLen > MAX_FORMAT_LENGTH) {
                    updatedText = digitsOnly;
                    return;
                }

                if (digitLen <= 6) {
                    String year = digitsOnly.substring(0, 4);
                    String month = digitsOnly.substring(4);

                    updatedText = String.format(Locale.US, "%s-%s", year, month);
                } else {
                    String year = digitsOnly.substring(0, 4);
                    String month = digitsOnly.substring(4, 6);
                    String day = digitsOnly.substring(6);

                    updatedText = String.format(Locale.US, "%s-%s-%s", year, month, day);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editing) return;

                editing = true;

                editable.clear();
                editable.insert(0, updatedText);

                editing = false;
            }
        });

        mRadioButtonGenderMale = (RadioButton) root.findViewById(R.id.radioGenderMale);
        mRadioButtonGenderFemale = (RadioButton) root.findViewById(R.id.radioGenderFemale);

        mBtnSignUpAuthCodeSend = (Button) root.findViewById(R.id.btnSignUpAuthCodeSend);
        mBtnSignUpNext = (Button) root.findViewById(R.id.btnSignUpNext);

        mBtnSignUpAuthCodeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .permitDiskReads()
                        .permitDiskWrites()
                        .permitNetwork().build());

                try {
                    GMailSender gMailSender = new GMailSender("shady952012@gmail.com", "Vpxhf9483!");
                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
                    emailCode=gMailSender.getEmailCode();
                    gMailSender.sendMail("ServersCenter 이메일 인증입니다.","인증코드 : "+emailCode,mEditSignUpEmail.getText().toString());
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
        });

        mBtnSignUpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBtnSignUpNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey2));
                mBtnSignUpNext.setEnabled(false);

                if (mEditSignUpEmail.getText().toString().equals("")
                        || mEditSignUpPwFirst.getText().toString().equals("")
                        || mEditSignUpPwSecond.getText().toString().equals("")
                        || mEditSignUpName.getText().toString().equals("")
                        || mEditSignUpBirth.getText().toString().equals("")) {

                    if (!emailOkSwitch) {
                        Toast.makeText(getContext(), "이메일 인증을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        mBtnSignUpNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darkPurple1));
                        mBtnSignUpNext.setEnabled(true);
                        return;
                    }
                    else {
                        Toast.makeText(getContext(), "잘못된 정보입니다 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                        mBtnSignUpNext.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.darkPurple1));
                        mBtnSignUpNext.setEnabled(true);
                        return;
                    }
                }


                if (profileImageUri == null) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            mEditSignUpEmail.getText().toString(),
                            mEditSignUpPwFirst.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    final String uid = task.getResult().getUser().getUid();

                                    // No Profile This.
                                    String imageUrl = "https://firebasestorage.googleapis.com/v0/b/serverscenter-android.appspot.com/o/userImages%2Fic_person.png?alt=media&token=d7d9791a-ef92-4ebe-acfb-50957d95357d";

                                    UserModel userModel = new UserModel();
                                    userModel.userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    userModel.userName = mEditSignUpName.getText().toString();
                                    userModel.userBirth = mEditSignUpBirth.getText().toString();
                                    userModel.userProfileImage = imageUrl;

                                    if (mRadioButtonGenderMale.isChecked())
                                        userModel.userGender = "남";
                                    else if (mRadioButtonGenderFemale.isChecked())
                                        userModel.userGender = "여";

                                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    getActivity().finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "DB 입력 오류", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            });

                    return;
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            mEditSignUpEmail.getText().toString(),
                            mEditSignUpPwFirst.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    final String uid = task.getResult().getUser().getUid();
                                    final StorageReference ref = FirebaseStorage.getInstance().getReference().child("userImages").child(uid);
                                    UploadTask uploadTask = ref.putFile(profileImageUri);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle unsuccessful uploads
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                            // ...
                                        }
                                    });

                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }

                                            // Continue with the task to get the download URL
                                            return ref.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                String imageUrl = task.getResult().toString();

                                                UserModel userModel = new UserModel();
                                                userModel.userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                userModel.userName = mEditSignUpName.getText().toString();
                                                userModel.userBirth = mEditSignUpBirth.getText().toString();
                                                userModel.userProfileImage = imageUrl;

                                                if (mRadioButtonGenderMale.isChecked())
                                                    userModel.userGender = "남";
                                                else if (mRadioButtonGenderFemale.isChecked())
                                                    userModel.userGender = "여";

                                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                getActivity().finish();
                                                            }
                                                        });
                                            } else {
                                                // Handle failures
                                                // ...
                                            }
                                        }
                                    });
                                }
                            });
                }
            }
        });
        return root;
    }


    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SignUpFragment.PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            mImgSignUpProfile.setImageURI(data.getData());
            profileImageUri = data.getData();
        }
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
