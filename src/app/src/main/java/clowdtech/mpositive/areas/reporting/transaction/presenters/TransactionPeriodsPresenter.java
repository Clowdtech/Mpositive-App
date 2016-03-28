package clowdtech.mpositive.areas.reporting.transaction.presenters;

import android.os.AsyncTask;

import com.clowdtech.data.DateRange;
import com.clowdtech.data.DateTimeHelper;
import com.clowdtech.data.repository.ITransactionsRepository;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import clowdtech.mpositive.areas.reporting.DataRequestTaskListener;
import clowdtech.mpositive.areas.reporting.transaction.captions.CaptionsTransactionPeriods;
import clowdtech.mpositive.areas.reporting.transaction.viewModels.TransactionPeriodsViewModel;
import clowdtech.mpositive.areas.reporting.transaction.views.ITransactionPeriodsView;
import clowdtech.mpositive.data.ReportingHelper;
import clowdtech.mpositive.ui.BasePresenter;

public class TransactionPeriodsPresenter extends BasePresenter<ITransactionPeriodsView> implements ITransactionPeriodsPresenter {
    private ITransactionsRepository transactionRepo;

    private List<TransactionPeriodsViewModel> periodViewModels;

    public static final int NUMBER_DAYS_TO_LOAD = 10;

    private CaptionsTransactionPeriods captions;

    @Inject
    public TransactionPeriodsPresenter(ITransactionsRepository transactionRepo, CaptionsTransactionPeriods captions) {
        this.captions = captions;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public void bindView(ITransactionPeriodsView view) {
        super.bindView(view);

        loadInitialPeriods();
    }

    @Override
    public void unbindView() {
        super.unbindView();
    }

    private void loadInitialPeriods() {
        this.view.setLoading();

        LoadInitialPeriodsAsyncTask _loadInitialAsyncTask = new LoadInitialPeriodsAsyncTask(this.transactionRepo, new DataRequestTaskListener<DateRange>() {
            @Override
            public void onFinished(DateRange lastTenRange) {
                DateTime todayAtStartOfDay = DateTime.now().withTimeAtStartOfDay();

                periodViewModels = new ArrayList<>();

                periodViewModels.add(new TransactionPeriodsViewModel(captions.getRecent(), lastTenRange));

                periodViewModels.addAll(getAllDaysViewModel(todayAtStartOfDay));

                view.setPeriods(periodViewModels);

                view.setLoaded();
            }
        });

        _loadInitialAsyncTask.execute();
    }

    @Override
    public void morePeriodsSelected() {
        // surely we should only display periods that actually have data?

        Long lastTimeStamp = periodViewModels.get(periodViewModels.size() - 1).getLowerStamp();

        LoadPeriodsAsyncTask _loadMoreAsyncTask = new LoadPeriodsAsyncTask(this.transactionRepo, new DataRequestTaskListener<List<TransactionPeriodsViewModel>>() {
            @Override
            public void onFinished(List<TransactionPeriodsViewModel> data) {
                periodViewModels.addAll(data); // theres a chance that this will grow quite big (concern?) without paging a current set

                if (data.size() > 0) {
                    view.addMorePeriods(data);
                } else {
                    view.setNoMorePeriodsAvailable();
                }
            }
        }, lastTimeStamp);

        _loadMoreAsyncTask.execute();
    }

    @Override
    public void periodSelected(int position) {
        this.view.setSelectedPeriod(position);

        String header;

        TransactionPeriodsViewModel model = periodViewModels.get(position);

        if (model.getPeriodTitle().equals(captions.getRecent())) {
            header = captions.getRecent();
        } else {
            header = String.format("Transactions for %s", DateTimeHelper.getFriendlyDateString(model.getLowerStamp()));
        }

        this.view.getContainer().transactionPeriodSelected(header, model.getLowerStamp(), model.getUpperStamp());
    }

    private List<TransactionPeriodsViewModel> getAllDaysViewModel(DateTime dateTime) {
        List<TransactionPeriodsViewModel> dayViewModels = new ArrayList<>();
        for (int i = 0; i < NUMBER_DAYS_TO_LOAD; i++) {
            DateTime start = dateTime.minusDays(i);
            DateTime end = start.plusDays(1);
            dayViewModels.add(new TransactionPeriodsViewModel(start, end));
        }

        return dayViewModels;
    }

    private class LoadInitialPeriodsAsyncTask extends AsyncTask<Void, Void, DateRange> {
        private DataRequestTaskListener<DateRange> taskListener;
        private ITransactionsRepository transactionRepo;

        public LoadInitialPeriodsAsyncTask(ITransactionsRepository transactionRepo, DataRequestTaskListener<DateRange> taskListener) {
            this.transactionRepo = transactionRepo;
            this.taskListener = taskListener;
        }

        protected DateRange doInBackground(Void... unused) {
            return this.transactionRepo.getLastTenDateRange();
        }

        protected void onPostExecute(DateRange data) {
            this.taskListener.onFinished(data);
        }
    }

    private class LoadPeriodsAsyncTask extends AsyncTask<Void, Void, List<TransactionPeriodsViewModel>> {
        private DataRequestTaskListener<List<TransactionPeriodsViewModel>> taskListener;
        private Long lastTimeStamp;

        private ReportingHelper transactionsProvider;

        public LoadPeriodsAsyncTask(ITransactionsRepository transactionRepo, DataRequestTaskListener<List<TransactionPeriodsViewModel>> taskListener, Long lastTimeStamp) {
            this.taskListener = taskListener;
            this.lastTimeStamp = lastTimeStamp;

            this.transactionsProvider = new ReportingHelper(transactionRepo);
        }

        protected List<TransactionPeriodsViewModel> doInBackground(Void... unused) {
            DateTime lastDate = new DateTime(lastTimeStamp);
            Boolean haveMoreTransactions = transactionsProvider.transactionsOlderThan(lastDate.minusMillis(1).toDate().getTime());

            if (haveMoreTransactions) {
                return getAllDaysViewModel(lastDate.minusDays(1)); // counting 10 days back from current oldest??
            }

            return new ArrayList<>();
        }

        protected void onPostExecute(List<TransactionPeriodsViewModel> data) {
            this.taskListener.onFinished(data);
        }
    }
}
