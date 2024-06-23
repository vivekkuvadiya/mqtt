package com.project.mqtt.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import activity.ClientActivity;
import adaptor.ClientAdapter;
import model.Client;
import model.ClientViewModel;
import server.com.mqtt.R;
import util.ClientConnectionThread;
import util.DBHelper;

public class ManageClientFragment extends Fragment {
    /* access modifiers changed from: private */
    public ClientAdapter clientAdapter;
    private ClientViewModel clientViewModel;
    private Menu menu;
    private MenuInflater menuInflater;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            ManageClientFragment.this.startActivity(new Intent(ManageClientFragment.this.getActivity(), ClientActivity.class));
        }
    };
    private RecyclerView recyclerView;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.manage_client_fragment, container, false);
        setHasOptionsMenu(true);
        initResources(rootView);
        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initResources(View rootView) {
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        this.clientAdapter = new ClientAdapter(getContext());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.recyclerView.getContext(), 1));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.clientAdapter.setClientList(new ArrayList());
        this.recyclerView.setAdapter(this.clientAdapter);
        this.clientViewModel = (ClientViewModel)new ViewModelProvider(this).get(ClientViewModel.class);
        this.clientViewModel.getClientLiveData().observe(this, new Observer<List<Client>>() {
            public void onChanged(@Nullable List<Client> clientList) {
                ManageClientFragment.this.clientAdapter.setClientList(clientList);
                ManageClientFragment.this.clientAdapter.notifyDataSetChanged();
            }
        });
        setEventListener();
    }

    private void setEventListener() {
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
        if (this.menu != null && this.menuInflater != null && this.clientAdapter != null) {
            View selectedView = this.clientAdapter.getSelectedClientId();
            if (selectedView != null) {
                selectedView.setSelected(false);
                selectedView.setBackgroundColor(0);
                this.clientAdapter.setSelectedClientId((View) null);
            }
            this.menu.clear();
            this.menuInflater.inflate(R.menu.menu_list, this.menu);
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!(isVisibleToUser || this.menu == null || this.menuInflater == null || this.clientAdapter == null)) {
            View selectedView = this.clientAdapter.getSelectedClientId();
            if (selectedView != null) {
                selectedView.setSelected(false);
                selectedView.setBackgroundColor(0);
                this.clientAdapter.setSelectedClientId((View) null);
            }
            this.menu.clear();
            this.menuInflater.inflate(R.menu.menu_list, this.menu);
        }
        if (getContext() != null) {
            try {
                new Thread(new ClientConnectionThread(getContext())).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu2, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu2);
        this.menu = menu2;
        this.menuInflater = inflater;
        super.onCreateOptionsMenu(menu2, inflater);
        this.clientAdapter.setMenu(menu2);
        this.clientAdapter.setMenuInflater(this.menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        View selected;
        TextView clientId;
        View selected2;
        switch (item.getItemId()) {
            case R.id.add /*2131296284*/:
                startActivity(new Intent(getActivity(), ClientActivity.class));
                break;
            case R.id.delete /*2131296332*/:
                if (!(this.clientAdapter == null || this.clientAdapter.getSelectedClientId() == null || (selected2 = this.clientAdapter.getSelectedClientId()) == null)) {
                    TextView clientId2 = (TextView) selected2.findViewById(R.id.clientId);
                    if (clientId2 != null) {
                        showDialog("Are You Sure", clientId2.getText().toString());
                    }
                    this.clientAdapter.unSelectView(selected2);
                    break;
                }
            case R.id.edit /*2131296343*/:
                if (!(this.clientAdapter == null || this.clientAdapter.getSelectedClientId() == null || (selected = this.clientAdapter.getSelectedClientId()) == null || (clientId = (TextView) selected.findViewById(R.id.clientId)) == null)) {
                    List<Client> clientList = DBHelper.findClientbyId(getContext(), clientId.getText().toString());
                    this.clientAdapter.unSelectView(selected);
                    if (clientList != null && clientList.size() > 0) {
                        Intent intent = new Intent(getActivity(), ClientActivity.class);
                        intent.putExtra("menuType", "edit");
                        intent.putExtra("client", clientList.get(0));
                        startActivity(intent);
                        break;
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(String message, final String clientId) {
        new AlertDialog.Builder(getActivity()).setTitle((CharSequence) "Error").setMessage((CharSequence) message).setCancelable(false).setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ManageClientFragment.this.sendDeleteSelectedClient(clientId);
            }
        }).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void sendDeleteSelectedClient(String clientId) {
        if (clientId != null) {
            DBHelper.deleteClient(getContext(), clientId);
        }
        this.clientAdapter.setSelectedClientId((View) null);
        this.menu.clear();
        this.menuInflater.inflate(R.menu.menu_list, this.menu);
    }
}
