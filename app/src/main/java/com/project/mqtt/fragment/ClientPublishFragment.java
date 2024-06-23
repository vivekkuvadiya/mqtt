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

import activity.PubCreateActivity;
import adaptor.PublisherAdaptor;
import model.ClientPublisher;
import model.ClientPublisherViewModel;
import server.com.mqtt.R;

public class ClientPublishFragment extends LifecycleFragment {
    String clientId;
    private ClientPublisherViewModel clientViewModel;
    private RecyclerView pubRecyclerView;
    /* access modifiers changed from: private */
    public PublisherAdaptor publisherAdaptor;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pub_fragment, container, false);
        setHasOptionsMenu(true);
        initResources(rootView);
        return rootView;
    }

    private void initResources(View view) {
        this.publisherAdaptor = new PublisherAdaptor(this.clientId, getContext());
        this.pubRecyclerView = (RecyclerView) view.findViewById(R.id.pubRecycler_view);
        this.publisherAdaptor.setPublisherList(new ArrayList());
        this.pubRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        this.pubRecyclerView.addItemDecoration(new DividerItemDecoration(this.pubRecyclerView.getContext(), 1));
        this.pubRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.pubRecyclerView.setAdapter(this.publisherAdaptor);
        ClientPublisherViewModel clientPublisherViewModel = this.clientViewModel;
        ClientPublisherViewModel.clientId = this.clientId;
        this.clientViewModel = (ClientPublisherViewModel) ViewModelProviders.of((Fragment) this).get(ClientPublisherViewModel.class);
        this.clientViewModel.getClientLiveData(this.clientId).observe(this, new Observer<List<ClientPublisher>>() {
            public void onChanged(@Nullable List<ClientPublisher> clientList) {
                if (clientList != null && clientList.size() > 0) {
                    ClientPublishFragment.this.publisherAdaptor.setPublisherList(clientList.get(0).publisherList);
                    ClientPublishFragment.this.publisherAdaptor.notifyDataSetChanged();
                }
            }
        });
        ((AdView) view.findViewById(R.id.pubAdView)).loadAd(new AdRequest.Builder().build());
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
                Intent intent = new Intent(getContext(), PubCreateActivity.class);
                intent.putExtra("pubClientId", this.clientId);
                getContext().startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
