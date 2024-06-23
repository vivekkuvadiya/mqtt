package com.project.mqtt.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.log4j.varia.ExternallyRolledFileAppender;
import org.eclipse.moquette.commons.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;

import io.netty.handler.codec.rtsp.RtspHeaders;
import model.BrokerConfig;
import server.com.mqtt.MqttAppService;
import server.com.mqtt.R;
import util.DBHelper;
import util.Util;

public class BrokerFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    /* access modifiers changed from: private */
    public RadioButton authRadio;
    private RadioGroup authRadioGroup;
    /* access modifiers changed from: private */
    public EditText brokerPassword;
    /* access modifiers changed from: private */
    public EditText brokerPort;
    /* access modifiers changed from: private */
    public EditText brokerUsername;
    private Button delBrokerConfig;
    private View.OnClickListener delBrokerConfigClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            final AlertDialog alertDialogLoad = new AlertDialog.Builder(BrokerFragment.this.getContext()).create();
            alertDialogLoad.setTitle("Delete Config");
            View dialogView = BrokerFragment.this.getLayoutInflater().inflate(R.layout.brokerconfig_del, (ViewGroup) null);
            final ListView delloadConfigList = (ListView) dialogView.findViewById(R.id.delBrokerConfigList);
            final List<BrokerConfig> brokerConfigList = DBHelper.loadBrokerConfig(BrokerFragment.this.getContext());
            final ArrayAdapter<BrokerConfig> arrayAdapter = new ArrayAdapter<>(BrokerFragment.this.getContext(), 17367058);
            arrayAdapter.addAll(brokerConfigList);
            delloadConfigList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            alertDialogLoad.setView(dialogView);
            if (brokerConfigList == null || !brokerConfigList.isEmpty()) {
                alertDialogLoad.show();
            } else {
                new AlertDialog.Builder(BrokerFragment.this.getContext()).setMessage((CharSequence) "No Saved Configs to Delete").setPositiveButton((CharSequence) ExternallyRolledFileAppender.OK, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
            delloadConfigList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final BrokerConfig brokerConfig = (BrokerConfig) adapterView.getItemAtPosition(i);
                    AlertDialog alertDialog = new AlertDialog.Builder(BrokerFragment.this.getContext()).setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DBHelper.delBrokerConfig(BrokerFragment.this.getContext(), brokerConfig);
                            brokerConfigList.remove(brokerConfig);
                            delloadConfigList.postInvalidate();
                            List<BrokerConfig> brokerConfigListupdated = DBHelper.loadBrokerConfig(BrokerFragment.this.getContext());
                            arrayAdapter.clear();
                            arrayAdapter.addAll(brokerConfigListupdated);
                            arrayAdapter.notifyDataSetChanged();
                            Toast.makeText(BrokerFragment.this.getContext(), "Delete Successful", 0);
                            if (brokerConfigListupdated != null && brokerConfigListupdated.isEmpty()) {
                                alertDialogLoad.dismiss();
                            }
                        }
                    }).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
                    alertDialog.setTitle("Confirm Delete");
                    alertDialog.show();
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public InputMethodManager imm;
    /* access modifiers changed from: private */
    public TextInputLayout inputbrokerPassword;
    /* access modifiers changed from: private */
    public TextInputLayout inputbrokerUsername;
    private Button loadBrokerConfig;
    private View.OnClickListener loadBrokerConfigClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            final AlertDialog alertDialogLoad = new AlertDialog.Builder(BrokerFragment.this.getContext()).create();
            alertDialogLoad.setTitle("Load Config");
            View dialogView = BrokerFragment.this.getLayoutInflater().inflate(R.layout.broker_load, (ViewGroup) null);
            ListView loadConfigList = (ListView) dialogView.findViewById(R.id.loadConfigList);
            List<BrokerConfig> brokerConfigList = DBHelper.loadBrokerConfig(BrokerFragment.this.getContext());
            ArrayAdapter<BrokerConfig> arrayAdapter = new ArrayAdapter<>(BrokerFragment.this.getContext(), 17367058);
            arrayAdapter.addAll(brokerConfigList);
            loadConfigList.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            alertDialogLoad.setView(dialogView);
            if (brokerConfigList == null || !brokerConfigList.isEmpty()) {
                alertDialogLoad.show();
            } else {
                new AlertDialog.Builder(BrokerFragment.this.getContext()).setMessage((CharSequence) "No Saved Configs to Load").setPositiveButton((CharSequence) ExternallyRolledFileAppender.OK, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
            loadConfigList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(BrokerFragment.this.getContext()).create();
                    alertDialog.setTitle("Load Config");
                    View dialogView = BrokerFragment.this.getLayoutInflater().inflate(R.layout.brokerconfig_info, (ViewGroup) null);
                    TextView loadauthInfo = (TextView) dialogView.findViewById(R.id.loadauthInfo);
                    TextView loadusernameInfo = (TextView) dialogView.findViewById(R.id.loadusernameInfo);
                    TextView loadpasswordInfo = (TextView) dialogView.findViewById(R.id.loadpasswordInfo);
                    Button loadBrokerConfig = (Button) dialogView.findViewById(R.id.loadBrokerConfig);
                    Button loadcancelBrokerConfig = (Button) dialogView.findViewById(R.id.cancelload);
                    final BrokerConfig brokerConfig = (BrokerConfig) adapterView.getItemAtPosition(i);
                    ((TextView) dialogView.findViewById(R.id.loadportInfo)).append(" : " + brokerConfig.getBrokerPort());
                    if (brokerConfig.getAuth() == null || !brokerConfig.getAuth().booleanValue()) {
                        loadauthInfo.append(" : NO");
                    } else {
                        loadauthInfo.append(" : YES");
                        loadusernameInfo.append(" : " + brokerConfig.getBrokerUsernameName());
                        loadpasswordInfo.append(" : " + brokerConfig.getBrokerPassword());
                    }
                    loadBrokerConfig.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            BrokerFragment.this.brokerPort.setText(brokerConfig.getBrokerPort());
                            if (brokerConfig.getAuth() == null || !brokerConfig.getAuth().booleanValue()) {
                                BrokerFragment.this.noAuthRadio.setChecked(true);
                                BrokerFragment.this.brokerUsername.setText("");
                                BrokerFragment.this.brokerPassword.setText("");
                                BrokerFragment.this.inputbrokerUsername.setVisibility(8);
                                BrokerFragment.this.inputbrokerPassword.setVisibility(8);
                                BrokerFragment.this.authRadio.setChecked(false);
                            } else {
                                BrokerFragment.this.authRadio.setChecked(true);
                                BrokerFragment.this.brokerUsername.setText(brokerConfig.getBrokerUsernameName());
                                BrokerFragment.this.brokerPassword.setText(brokerConfig.getBrokerPassword());
                                BrokerFragment.this.inputbrokerUsername.setVisibility(0);
                                BrokerFragment.this.inputbrokerPassword.setVisibility(0);
                                BrokerFragment.this.noAuthRadio.setChecked(false);
                            }
                            alertDialog.dismiss();
                            alertDialogLoad.dismiss();
                            Toast.makeText(BrokerFragment.this.getContext(), "Load Successful", 0);
                        }
                    });
                    loadcancelBrokerConfig.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            MqttAppService unused = BrokerFragment.this.mService = ((MqttAppService.LocalBinder) service).getService();
            boolean unused2 = BrokerFragment.this.mBound = true;
            BrokerFragment.this.updateStartedStatus();
        }

        public void onServiceDisconnected(ComponentName arg0) {
            boolean unused = BrokerFragment.this.mBound = false;
        }
    };
    /* access modifiers changed from: private */
    public MqttAppService mService;
    /* access modifiers changed from: private */
    public RadioButton noAuthRadio;
    private Properties props = new Properties();
    private Button saveBrokerConfig;
    private View.OnClickListener saveBrokerConfigClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            final AlertDialog alertDialog = new AlertDialog.Builder(BrokerFragment.this.getContext()).create();
            alertDialog.setTitle("Save Config");
            alertDialog.setContentView((int) R.layout.broker_save);
            View dialogView = BrokerFragment.this.getLayoutInflater().inflate(R.layout.broker_save, (ViewGroup) null);
            final TextView portInfo = (TextView) dialogView.findViewById(R.id.portInfo);
            final EditText configName = (EditText) dialogView.findViewById(R.id.configName);
            TextView authInfo = (TextView) dialogView.findViewById(R.id.authInfo);
            TextView usernameInfo = (TextView) dialogView.findViewById(R.id.usernameInfo);
            TextView passwordInfo = (TextView) dialogView.findViewById(R.id.passwordInfo);
            Button saveButton = (Button) dialogView.findViewById(R.id.saveBrokerConfig);
            if (!BrokerFragment.this.brokerPort.getText().toString().isEmpty()) {
                portInfo.append(" : " + BrokerFragment.this.brokerPort.getText().toString());
            }
            if (BrokerFragment.this.brokerUsername.getText().toString().isEmpty() || BrokerFragment.this.brokerPassword.getText().toString().isEmpty() || !BrokerFragment.this.authRadio.isChecked()) {
                authInfo.append(" : No");
            } else {
                authInfo.append(" : Yes");
                usernameInfo.append(" : " + BrokerFragment.this.brokerUsername.getText());
                passwordInfo.append(" : " + BrokerFragment.this.brokerPassword.getText());
            }
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ((InputMethodManager) BrokerFragment.this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                    if (portInfo.getText().toString().isEmpty()) {
                        new AlertDialog.Builder(BrokerFragment.this.getContext()).setTitle((CharSequence) "Error").setMessage((CharSequence) "Port is Empty...config cannot be saved").setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                    } else if (configName.getText().toString().isEmpty()) {
                        new AlertDialog.Builder(BrokerFragment.this.getContext()).setTitle((CharSequence) "Error").setMessage((CharSequence) "Config Name is Empty...config cannot be saved").setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                    } else {
                        BrokerConfig brokerConfig = new BrokerConfig();
                        brokerConfig.setConfigName(configName.getText().toString());
                        brokerConfig.setBrokerPort(BrokerFragment.this.brokerPort.getText().toString());
                        if (!BrokerFragment.this.brokerUsername.getText().toString().isEmpty() && !BrokerFragment.this.brokerPassword.getText().toString().isEmpty() && BrokerFragment.this.authRadio.isChecked()) {
                            brokerConfig.setAuth(true);
                            brokerConfig.setBrokerUsernameName(BrokerFragment.this.brokerUsername.getText().toString());
                            brokerConfig.setBrokerPassword(BrokerFragment.this.brokerPassword.getText().toString());
                        }
                        DBHelper.saveBrokerConfig(BrokerFragment.this.getContext(), brokerConfig);
                        Toast.makeText(BrokerFragment.this.getContext(), "Config Save Successfull", 0).show();
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        }
    };
    private TextView serverStatusTextView;
    private Button startButton;
    private View.OnClickListener startButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (BrokerFragment.this.imm.isActive()) {
                BrokerFragment.this.imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            if (BrokerFragment.this.validateBrokerProperties()) {
                BrokerFragment.this.startMqttService();
            }
        }
    };
    private String statusCommandLine;
    /* access modifiers changed from: private */
    public Button stopButton;
    private View.OnClickListener stopButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (BrokerFragment.this.imm.isActive()) {
                BrokerFragment.this.imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            BrokerFragment.this.stopMqttService();
        }
    };

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.broker_fragment, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initResources(view);
        setEvenListeners();
        askforPermission();
        this.imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void onStart() {
        super.onStart();
        updateCheckingStatus();
        updateStoppedStatus();
        getActivity().bindService(new Intent(getActivity(), MqttAppService.class), this.mConnection, 64);
    }

    public void onResume() {
        super.onResume();
        updateCheckingStatus();
        if (!this.mBound || this.mService == null) {
            updateStoppedStatus();
        } else if (this.mService.getServerStatus()) {
            updateStartedStatus();
        }
    }

    private void setEvenListeners() {
        this.startButton.setOnClickListener(this.startButtonClickListener);
        this.stopButton.setOnClickListener(this.stopButtonClickListener);
        this.authRadioGroup.setOnCheckedChangeListener(this);
        this.loadBrokerConfig.setOnClickListener(this.loadBrokerConfigClickListener);
        this.saveBrokerConfig.setOnClickListener(this.saveBrokerConfigClickListener);
        this.delBrokerConfig.setOnClickListener(this.delBrokerConfigClickListener);
    }

    private void initResources(View view) {
        this.inputbrokerUsername = (TextInputLayout) view.findViewById(R.id.inputbrokerUsername);
        this.inputbrokerPassword = (TextInputLayout) view.findViewById(R.id.inputbrokerPassword);
        this.startButton = (Button) view.findViewById(R.id.startBrokerButton);
        this.stopButton = (Button) view.findViewById(R.id.stopBrokerButton);
        this.brokerPort = (EditText) view.findViewById(R.id.brokerPort);
        this.brokerUsername = (EditText) view.findViewById(R.id.brokerUsername);
        this.brokerPassword = (EditText) view.findViewById(R.id.brokerPassword);
        this.serverStatusTextView = (TextView) view.findViewById(R.id.serverStatusTextView);
        this.authRadioGroup = (RadioGroup) view.findViewById(R.id.authRadioGroup);
        this.statusCommandLine = getResources().getString(R.string.serverStatusText);
        this.authRadio = (RadioButton) view.findViewById(R.id.AuthRadio);
        this.noAuthRadio = (RadioButton) view.findViewById(R.id.noAuthRadio);
        this.loadBrokerConfig = (Button) view.findViewById(R.id.loadBrokerConfig);
        this.saveBrokerConfig = (Button) view.findViewById(R.id.saveBrokerConfig);
        this.delBrokerConfig = (Button) view.findViewById(R.id.delBrokerConfig);
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.AuthRadio /*2131296257*/:
                this.inputbrokerUsername.setVisibility(0);
                this.inputbrokerPassword.setVisibility(0);
                return;
            case R.id.noAuthRadio /*2131296400*/:
                this.inputbrokerUsername.setVisibility(8);
                this.inputbrokerPassword.setVisibility(8);
                return;
            default:
                return;
        }
    }

    private boolean askforPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return false;
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", 0).show();
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void startMqttService() {
        this.stopButton.setEnabled(false);
        if (!this.mBound || this.mService == null || !this.mService.getServerStatus()) {
            Intent startIntent = new Intent(getActivity(), MqttAppService.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("configFile", constructBrokerProperties());
            bundle.putSerializable("props", this.props);
            startIntent.putExtras(bundle);
            getActivity().startService(startIntent);
            getActivity().bindService(startIntent, this.mConnection, 64);
            return;
        }
        showDialog("Server Already Running");
    }

    /* access modifiers changed from: private */
    public void stopMqttService() {
        if (this.mBound) {
            getActivity().unbindService(this.mConnection);
            this.mBound = false;
        }
        getActivity().stopService(new Intent(getActivity(), MqttAppService.class));
        updateStoppedStatus();
    }

    /* access modifiers changed from: private */
    public boolean validateBrokerProperties() {
        if (this.brokerPort.getText() == null || !this.brokerPort.getText().toString().isEmpty()) {
            if (this.authRadioGroup.getCheckedRadioButtonId() == R.id.AuthRadio) {
                if (this.brokerUsername.getText() != null && this.brokerUsername.getText().toString().isEmpty()) {
                    showDialog("Username is Empty");
                    return false;
                } else if (this.brokerPassword.getText() != null && this.brokerPassword.getText().toString().isEmpty()) {
                    showDialog("Password is Empty");
                    return false;
                }
            }
            return true;
        }
        showDialog("Port is Empty");
        return false;
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(getActivity()).setTitle((CharSequence) "Error").setMessage((CharSequence) message).setCancelable(false).setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private File constructBrokerProperties() {
        File confFile = createConfigurationFile();
        if (this.authRadioGroup != null && this.authRadioGroup.getCheckedRadioButtonId() == R.id.AuthRadio) {
            File Passwordfile = new File(getContext().getDir("media", 0).getAbsolutePath() + Util.PASSWORD_FILE);
            try {
                if (Passwordfile.exists()) {
                    writeToPasswordFile(Passwordfile);
                } else if (Passwordfile.createNewFile()) {
                    writeToPasswordFile(Passwordfile);
                }
                FileOutputStream fileOutputStream = new FileOutputStream(confFile, true);
                fileOutputStream.write(("\n" + Util.ALLOW_ANONYMOUS + " false").getBytes());
                fileOutputStream.write(("\npassword_file " + Passwordfile.getAbsolutePath()).getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return confFile;
    }

    private StringBuilder getCommandBuilder() {
        return new StringBuilder(this.statusCommandLine);
    }

    /* access modifiers changed from: private */
    public void updateStartedStatus() {
        StringBuilder sb = getCommandBuilder().append("Server is started... - \n");
        sb.append(getCommandBuilder());
        sb.append("Broker URL - " + this.mService.getBrokerURL());
        this.serverStatusTextView.setText(sb.toString());
        hideEditViews();
        updateOrInsertLocalBroker(this.mService.getBrokerURL());
    }

    private void updateStoppedStatus() {
        this.serverStatusTextView.setText("");
        this.serverStatusTextView.append(getCommandBuilder().append("Server is stopped...\n").toString());
        unhideEditViews();
    }

    private void updateCheckingStatus() {
        this.serverStatusTextView.setText("");
        this.serverStatusTextView.append(getCommandBuilder().append("Checking Server Status...\n").toString());
    }

    public void onDestroy() {
        super.onDestroy();
        if (!this.mBound || this.mService == null) {
            try {
                getActivity().unbindService(this.mConnection);
            } catch (Exception e) {
                Log.d("UnBinding", "Unbinding failed..is service really started");
            }
        } else {
            getActivity().unbindService(this.mConnection);
        }
    }

    private void hideEditViews() {
        this.brokerPort.setEnabled(false);
        this.authRadio.setEnabled(false);
        this.noAuthRadio.setEnabled(false);
        this.startButton.setEnabled(false);
        this.brokerUsername.setEnabled(false);
        this.brokerPassword.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                BrokerFragment.this.stopButton.setEnabled(true);
            }
        }, 2000);
    }

    private void unhideEditViews() {
        this.brokerPort.setEnabled(true);
        this.authRadio.setEnabled(true);
        this.noAuthRadio.setEnabled(true);
        this.startButton.setEnabled(true);
        this.brokerUsername.setEnabled(true);
        this.brokerPassword.setEnabled(true);
    }

    private void updateOrInsertLocalBroker(String brokerUrl) {
        if (this.mService != null) {
            DBHelper.insertOrUpdateLocalBroker(getContext(), brokerUrl);
        }
    }

    private void writeToPasswordFile(File passwordFile) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(passwordFile);
        fileOutputStream.write(this.brokerUsername.getText().toString().getBytes());
        fileOutputStream.write(":".getBytes());
        fileOutputStream.write(this.brokerPassword.getText().toString().getBytes());
        fileOutputStream.close();
    }

    private File createConfigurationFile() {
        File confFile = new File(getContext().getDir("media", 0).getAbsolutePath() + Util.BROKER_CONF_FILE);
        try {
            if (confFile.exists()) {
                return writeToConfFile(confFile);
            }
            if (confFile.createNewFile()) {
                return writeToConfFile(confFile);
            }
            return confFile;
        } catch (Exception e) {
            e.printStackTrace();
            return confFile;
        }
    }

    private File writeToConfFile(File confFile) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(confFile);
        fileOutputStream.write(("port " + String.valueOf(this.brokerPort.getText().toString() + "\n")).getBytes());
        this.props.put(RtspHeaders.Values.PORT, String.valueOf(this.brokerPort.getText().toString()));
        fileOutputStream.write(("host " + Util.getBrokerURL(getContext()) + "\n").getBytes());
        this.props.put("host", Util.getBrokerURL(getContext()));
        fileOutputStream.write(("websocket_port " + Integer.toString(Constants.WEBSOCKET_PORT) + "\n").getBytes());
        fileOutputStream.close();
        return confFile;
    }
}
