package clowdtech.mpositive.areas.reporting.product.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.clowdtech.data.repository.ITransactionsRepository;
import com.clowdtech.data.repository.RepositoryProvider;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import clowdtech.mpositive.R;
import clowdtech.mpositive.areas.reporting.IReportingDaySelected;
import clowdtech.mpositive.areas.reporting.transaction.adapters.TransactionPeriodsArrayAdapter;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionPeriodsViewModel;
import clowdtech.mpositive.data.ReportingHelper;
import clowdtech.mpositive.ui.fragments.BaseFragment;

public class ReportingDaysFragment extends BaseFragment {

    public static final int NUMBER_DAYS_TO_LOAD = 10;
    private TransactionPeriodsArrayAdapter _adapter;
    private List<TransactionPeriodsViewModel> _viewModel;
    private IReportingDaySelected _selectionController;
    private ReportingHelper reportingHelper;
    private long _lastTimeStamp;
    private ListView _listView;
    private Button _btnLoadExtra;
    private loadMoreListView _loadMoreAsyncTask;

    private static ITransactionsRepository transactionRepo = RepositoryProvider.getTransactionsRepository();

    public static ReportingDaysFragment newInstance() {
        return new ReportingDaysFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reportingHelper = new ReportingHelper(transactionRepo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reporting_product_recent_column, container, false);

        _listView = (ListView) view.findViewById(R.id.reporting_days_list);
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransactionPeriodsArrayAdapter.ViewHolder viewholder = (TransactionPeriodsArrayAdapter.ViewHolder)view.getTag();

                TransactionPeriodsViewModel model = viewholder.model;


                _listView.setItemChecked(position, true);
                _selectionController.ItemSelected(model.getLowerStamp(), model.getUpperStamp());
            }
        });

        _btnLoadExtra = new Button(getActivity());
        _btnLoadExtra.setText(getActivity().getString(R.string.load_more_button_caption));
        _listView.addFooterView(_btnLoadExtra);
        _btnLoadExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                _loadMoreAsyncTask = new loadMoreListView();
                _loadMoreAsyncTask.execute();
            }
        });

        setInitialViewModel();

        _adapter = new TransactionPeriodsArrayAdapter(getActivity(), _viewModel);

        _listView.setAdapter(_adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //trackScreenView(TrackingConstants.ScreenNames.ReportProductPeriods);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        _selectionController = (IReportingDaySelected)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        _selectionController = new IReportingDaySelected() {
            @Override
            public void ItemSelected(long lower, long upper) {

            }
        };
    }

    private void setInitialViewModel() {
        DateTime todayAtStartOfDay = DateTime.now().withTimeAtStartOfDay();
        _viewModel = getAllDaysViewModel(todayAtStartOfDay);
        _lastTimeStamp = _viewModel.get(_viewModel.size() - 1).getLowerStamp();
    }

    private List<TransactionPeriodsViewModel> getAllDaysViewModel(DateTime dateTime) {
        List<TransactionPeriodsViewModel> dayViewModels = new ArrayList<>();
        for(int i = 0; i < NUMBER_DAYS_TO_LOAD; i++) {
            DateTime start = dateTime.minusDays(i);
            DateTime end = start.plusDays(1);
            dayViewModels.add(new TransactionPeriodsViewModel(start, end));
        }

        return dayViewModels;
    }

    private class loadMoreListView extends AsyncTask<Void, Void, List<TransactionPeriodsViewModel>> {
        protected List<TransactionPeriodsViewModel> doInBackground(Void... unused) {
            DateTime lastDate = new DateTime(_lastTimeStamp);
            Boolean haveMoreTransactions = reportingHelper.transactionsOlderThan(lastDate.minusMillis(1).toDate().getTime());

            List<TransactionPeriodsViewModel> data = null;

            if (haveMoreTransactions) {
                data = getAllDaysViewModel(lastDate.minusDays(1));
            }

            return data;
        }

        protected void onPostExecute(List<TransactionPeriodsViewModel> data) {
            if (data != null) {
                _adapter.AddMoreViewModels(data);
                _lastTimeStamp = data.get(data.size() - 1).getLowerStamp();
            } else {
                if (_listView.getFooterViewsCount() > 0)
                    _listView.removeFooterView(_btnLoadExtra);
            }
        }
    }
}
