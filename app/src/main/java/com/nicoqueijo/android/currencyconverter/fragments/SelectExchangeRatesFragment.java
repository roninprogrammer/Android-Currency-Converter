package com.nicoqueijo.android.currencyconverter.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.nicoqueijo.android.currencyconverter.R;
import com.nicoqueijo.android.currencyconverter.adapters.SelectExchangeRatesRecyclerViewAdapter;
import com.nicoqueijo.android.currencyconverter.helpers.Constants;
import com.nicoqueijo.android.currencyconverter.interfaces.ICommunicator;
import com.nicoqueijo.android.currencyconverter.models.Currency;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;
import com.turingtechnologies.materialscrollbar.DragScrollBar;

import java.util.ArrayList;

/**
 * Fragment used to search and add exchange rates to the ActiveExchangeRatesFragment.
 */
public class SelectExchangeRatesFragment extends Fragment {

    public static final String TAG = SelectExchangeRatesFragment.class.getSimpleName();

    ArrayList<Currency> mAllCurrencies;

    private RecyclerView mRecyclerView;
    private SelectExchangeRatesRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private DragScrollBar mDragScrollBar;
    private SearchView mSearchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAllCurrencies = getArguments().getParcelableArrayList(Constants.ARG_ALL_CURRENCIES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_exchange_rate, container, false);
        initViewsAdaptersAndListeners(view);
        return view;
    }

    /**
     * Initializes the views, sets up the adapters, and sets the setOnQueryTextListener listener
     * for the SearchView.
     *
     * @param view the root view of the inflated hierarchy
     */
    private void initViewsAdaptersAndListeners(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_select_rates);
        mToolbar = view.findViewById(R.id.toolbar_search);
        mToolbar.inflateMenu(R.menu.menu_search);
        mDragScrollBar = view.findViewById(R.id.drag_scroll_bar);
        mDragScrollBar.setIndicator(new AlphabetIndicator(getContext()), true);
        mAdapter = new SelectExchangeRatesRecyclerViewAdapter(this, mAllCurrencies);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSearchView = (SearchView) mToolbar.getMenu().findItem(R.id.search).getActionView();
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_GO);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /**
     * Factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return a new instance of fragment
     */
    public static SelectExchangeRatesFragment newInstance(ArrayList<Currency> allCurrencies) {
        SelectExchangeRatesFragment selectExchangeRatesFragment = new SelectExchangeRatesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.ARG_ALL_CURRENCIES, allCurrencies);
        selectExchangeRatesFragment.setArguments(args);
        return selectExchangeRatesFragment;
    }

    /**
     * Passes the newly selected currency to the ActiveExchangeRatesFragment via an interface.
     *
     * @param currency the new currency that was selected
     */
    public void sendActiveCurrency(Currency currency) {
        ICommunicator communicator = (ICommunicator) getActivity();
        communicator.passSelectedCurrency(currency);
    }
}