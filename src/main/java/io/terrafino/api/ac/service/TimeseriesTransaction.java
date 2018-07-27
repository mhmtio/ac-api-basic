package io.terrafino.api.ac.service;

import com.ac.api.error.ServerException;
import com.ac.api.transaction.TimeSeriesTransactionResult;

public class TimeseriesTransaction implements Transaction {

    private com.ac.api.transaction.TimeSeriesTransaction transaction;

    public TimeseriesTransaction(com.ac.api.transaction.TimeSeriesTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public TimeSeriesTransactionResult executeUsingConnection(AcConnection conn) throws ServerException {
        return conn.serverInterface.timeSeriesTransaction(transaction);
    }
}
