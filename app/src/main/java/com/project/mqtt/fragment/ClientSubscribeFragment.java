package com.project.mqtt.fragment;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import activity.SubCreateActivity;
import adaptor.SubscriberAdaptor;
import model.ClientSubscriber;
import model.ClientSubscriberViewModel;
import server.com.mqtt.R;

public class ClientSubscribeFragment extends LifecycleFragment {
    String clientId;
    ClientSubscriberViewModel clientSubscriberViewModel;
    RecyclerView subRecyclerView;
    SubscriberAdaptor subscriberAdaptor;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sub_fragment, container, false);
        setHasOptionsMenu(true);
        initResources(rootView);
        return rootView;
    }

    private void initResources(View view) {
        this.subRecyclerView = (RecyclerView) view.findViewById(R.id.subRecycler_view);
        this.subscriberAdaptor = new SubscriberAdaptor(getContext(), this.clientId);
        this.subscriberAdaptor.setSubscriberList(new ArrayList());
        this.subRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        this.subRecyclerView.addItemDecoration(new DividerItemDecoration(this.subRecyclerView.getContext(), 1));
        this.subRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.subRecyclerView.setAdapter(this.subscriberAdaptor);
        ClientSubscriberViewModel clientSubscriberViewModel2 = this.clientSubscriberViewModel;
        ClientSubscriberViewModel.clientId = this.clientId;
        this.clientSubscriberViewModel = (ClientSubscriberViewModel) ViewModelProviders.of((Fragment) this).get(ClientSubscriberViewModel.class);
        this.clientSubscriberViewModel.getClientLiveData(this.clientId).observe(this, new Observer<List<ClientSubscriber>>() {
            public void onChanged(@Nullable List<ClientSubscriber> clientList) {
                if (clientList != null && clientList.size() > 0) {
                    ClientSubscribeFragment.this.subscriberAdaptor.setSubscriberList(clientList.get(0).subscriberList);
                    ClientSubscribeFragment.this.subscriberAdaptor.notifyDataSetChanged();
                }
            }
        });
        ((AdView) view.findViewById(R.id.subAdView)).loadAd(new AdRequest.Builder().build());
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId2) {
        this.clientId = clientId2;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add /*2131296284*/:
                Intent intent = new Intent(getContext(), SubCreateActivity.class);
                intent.putExtra("subClientId", this.clientId);
                getContext().startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
