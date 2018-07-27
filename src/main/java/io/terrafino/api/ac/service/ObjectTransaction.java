package io.terrafino.api.ac.service;

import com.ac.api.error.ServerException;
import com.ac.api.transaction.ObjectTransactionResult;

public class ObjectTransaction implements Transaction {

    private com.ac.api.transaction.ObjectTransaction transaction;

    public ObjectTransaction(com.ac.api.transaction.ObjectTransaction transaction) {
        this.transaction = transaction;
    }


    @Override
    public ObjectTransactionResult executeUsingConnection(AcConnection conn) throws ServerException {
        return conn.serverInterface.objectTransaction(transaction);
    }
}
